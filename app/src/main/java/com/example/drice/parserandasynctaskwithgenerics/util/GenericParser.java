package com.example.drice.parserandasynctaskwithgenerics.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;


/**
 * Parser class that GenericAsyncTask calls to parse data and put it into a list to go back to the
 * GenericAsyncTask calling this (and then to whatever class that called the GenericAsyncTask)
 */
public class GenericParser<T extends Object> {

    public static final String TAG = "GenericParser";

    /**
     * Gets byte array value of urlSpec
     * @param urlSpec
     * @return
     * @throws IOException
     */
    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * Sets instance of XmlPullParser, and gets string value of data from url, then sets that as
     * input for parser to get full string value of url's xml
     * @param url
     * @param className
     * @return
     */
    public ArrayList<T> downloadItems(String url, String className, String itemKeyName) {
        ArrayList<T> items = new ArrayList<T>();

        try {
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            parseItems(items, parser, className, itemKeyName);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Failed to parse items", xppe);
        }
        return items;
    }

    /**
     * The method that goes through data string of xml and parses necessary data from it,
     * turning it into instances of object specified by className, and adding them to ArrayList items
     * @param items
     * @param parser
     * @param className
     * @throws XmlPullParserException
     * @throws IOException
     */
    void parseItems(ArrayList<T> items, XmlPullParser parser, String className, String itemKeyName)
            throws XmlPullParserException, IOException {
        int eventType = parser.next();
        try {
            //Get a Class instance of model that will contain data
            Class clazz = Class.forName(className);

            //Get the fields of the model in a list to check when we get to it in xml - Note that
            //field name of model must match the key containing the desired data in the xml
            List<String> objfieldlist = new ArrayList<String>();
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                objfieldlist.add(fields[i].getName());
            }

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        itemKeyName.equals(parser.getName())) {

                    try {
                        //Create new object instance of model to be used as container for desired
                        // data to be put in list for activity's listview's adapter
                        Object item = clazz.newInstance();

                        while (eventType != XmlPullParser.END_TAG || !itemKeyName.equals(parser.getName())) {
                            if (objfieldlist.contains(parser.getName())) {
                                String fieldName = parser.getName();
                            /*
                            Replaces all special chars in field name - in case they contain them
                             */
                                Log.d(TAG, "fieldname before - " + fieldName);
                                fieldName = fieldName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                                Log.d(TAG, "fieldname with char replaced - " + fieldName);
                                String fieldValue = parser.nextText();

                                //Using more reflection to set specified field of object
                                Field field;
                                try {
                                    field = clazz.getDeclaredField(fieldName);
                                    field.setAccessible(true);
                                    Object objFieldValue;
                                    Log.d(TAG, "fieldname type - " + field.getType().toString());
                                    objFieldValue = fieldValue;
                                    field.set(item, objFieldValue);
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            }
                            eventType = parser.next();
                        }

                        //Getting generic instance of Object item, to add to list which will go back
                        //to activity originally calling asynctask that called this parser
                        T item2 = (T) item;
                        T t = item2;
                        items.add(t);
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (InstantiationException e2) {
                        e2.printStackTrace();
                    }
                }

                eventType = parser.next();
            }
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    /**
     * fetch items returning array list with Contents of Type T
     * @return
     */
    public ArrayList<T> fetchItems(Map<String, String> endpointKey) {
        String url = endpointKey.get(CoreConstants.DATA_URL);
        String className = endpointKey.get(CoreConstants.CLASS_NAME);
        String itemKeyName = endpointKey.get(CoreConstants.PARSER_KEY);
        return downloadItems(url, className, itemKeyName);
    }
}
