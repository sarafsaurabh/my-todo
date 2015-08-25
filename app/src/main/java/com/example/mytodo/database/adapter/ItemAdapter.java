package com.example.mytodo.database.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mytodo.R;
import com.example.mytodo.database.model.Item;

import java.util.ArrayList;

/**
 * Created by ssaraf on 8/24/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        private TextView item;
        private TextView id;
    }

    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.item, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.item = (TextView) convertView.findViewById(R.id.tvValue);
            viewHolder.id = (TextView) convertView.findViewById(R.id.tvId);
            viewHolder.id.setVisibility(View.INVISIBLE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);
        viewHolder.item.setText(item.getValue());
        viewHolder.id.setText(item.getId().toString());

        return convertView;
    }
}
