package com.example.drice.parserandasynctaskwithgenerics.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.drice.parserandasynctaskwithgenerics.R;
import com.example.drice.parserandasynctaskwithgenerics.model.SomeListEntry;
import com.example.drice.parserandasynctaskwithgenerics.adapter.ExampleAdapter;
import com.example.drice.parserandasynctaskwithgenerics.util.CoreConstants;
import com.example.drice.parserandasynctaskwithgenerics.util.GenericAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Examlple activity that uses generic async task to fetch data to put into
 * a Listview
 */
public class ExampleActivity extends Activity implements GenericAsyncTask.FetchTaskListener<SomeListEntry> {

    private ListView listView;
    private ExampleAdapter adapter;
    private ArrayList<SomeListEntry> someListItems;
    private Map<String, String> parserKeys;
    private Map<String, String> objectKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        listView = (ListView)findViewById(R.id.list_view);

        someListItems = new ArrayList<SomeListEntry>();

        //Parserkeys contains dataurl, classname of object to put data into, and name of key in
        //data which signifies the start of the item in data
        parserKeys = new HashMap<String, String>();
        parserKeys.put(CoreConstants.DATA_URL, CoreConstants.BLOG_URL);
        parserKeys.put(CoreConstants.CLASS_NAME, CoreConstants.SOMELISTENTRY_CLASS_NAME);
        parserKeys.put(CoreConstants.PARSER_KEY, CoreConstants.PARSER_ITEM_NAME);

        new GenericAsyncTask<SomeListEntry>(this).execute(parserKeys);

        adapter = new ExampleAdapter(this, android.R.layout.simple_list_item_1, someListItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(ExampleActivity.this, DetailActivity.class);
                intent.putExtra("url", someListItems.get(position).getLink());
                startActivity(intent);
            }
        });
    }

    /**
     * Listener method of FetchTaskListener of GenericAsyncTask, allows ExampleActivity to set
     * adapter with GenericAsyncTasks result list of items
     * @param itemList
     */
    @Override
    public void onPostExecute(ArrayList<SomeListEntry>itemList) {
        someListItems = itemList;
        adapter = new ExampleAdapter(this, android.R.layout.simple_list_item_1, someListItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
