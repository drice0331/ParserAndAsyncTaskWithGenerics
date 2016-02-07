package com.example.drice.parserandasynctaskwithgenerics.util;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Map;

/**
 * AsyncTask using generics, so that the type of item being returned in list can be specified when
 * instantiating this AsyncTask, so all it has to know is the name of the item's class, the url of
 * the data, and key name (in the data that specifies where the item and its contents start) when
 * executing this AsyncTask
 * @param <T>
 */
public class GenericAsyncTask<T extends Object> extends AsyncTask<Map<String, String>, Void, ArrayList<T>> {

    FetchTaskListener<T>listener;
    Context context;

    public GenericAsyncTask(Context listener) {
        this.context = listener;
        this.listener = (FetchTaskListener<T>) listener;
    }

    /**
     * Takes in a Map to contain keys of DATA_URL, CLASS_NAME, and PARSER_KEY from
     * CoreConstants
     * @param params
     * @return
     */
    @Override
    protected ArrayList<T> doInBackground(Map<String,String>... params) {
        Map<String, String> dataRetrieveKey = params[0];
        ArrayList<T> list = new GenericParser<T>().fetchItems(dataRetrieveKey);
        return list;
    }

    /**
     * AsyncTasks post execute method, also calls listener method to class implementing
     * FetchTaskListener
     * @param itemList
     */
    @Override
    protected void onPostExecute(ArrayList<T> itemList) {
        listener.onPostExecute(itemList);
        super.onPostExecute(itemList);
    }

    /**
     * Listener for a class using GenericAsyncTask, solely for that class to implement its own
     * postexecute method for now
     * @param <T>
     */
    public interface FetchTaskListener<T extends Object> {
        void onPostExecute(ArrayList<T> itemList);
    }


}