package com.example.drice.parserandasynctaskwithgenerics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.drice.parserandasynctaskwithgenerics.R;
import com.example.drice.parserandasynctaskwithgenerics.model.SomeListEntry;

import java.util.List;

/**
 * Adapter for listview of exampleActivity
 */
public class ExampleAdapter extends ArrayAdapter<SomeListEntry> {

    private Context mContext;
    private List<SomeListEntry> mListEntries;

    public ExampleAdapter(Context context, int resource, List<SomeListEntry> listEntries) {
        super(context, resource, listEntries);
        mContext = context;
        mListEntries = listEntries;
    }

    public class ViewHolder {
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
            holder.title = (TextView)convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        SomeListEntry entry = mListEntries.get(position);

        holder.title.setText(entry.getTitle());

        return convertView;
    }
}
