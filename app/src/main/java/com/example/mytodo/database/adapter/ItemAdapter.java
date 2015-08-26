package com.example.mytodo.database.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mytodo.R;
import com.example.mytodo.database.model.Item;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;

/**
 * Created by ssaraf on 8/24/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        private TextView item;
        private TextView id;
        private TextView dueDate;

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
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);

            viewHolder.id.setVisibility(View.INVISIBLE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);
        viewHolder.item.setText(item.getValue());
        viewHolder.item.setTextColor(Color.BLACK);
        viewHolder.id.setText(item.getId().toString());
        if(item.getDueDate() != null) {
            int days = Days.daysBetween(new DateTime(), new DateTime(item.getDueDate())).getDays();
            if(days < 0) {
                viewHolder.dueDate.setText("Already due");
                convertView.setBackgroundColor(0xFFFFE5E5);
            } else if (days == 0) {
                viewHolder.dueDate.setText("Due today");
                convertView.setBackgroundColor(0xFFFFFDD5);
            } else if (days == 1) {
                viewHolder.dueDate.setText("Due tomorrow");
                convertView.setBackgroundColor(0xFFFFFDD5);
            } else {
                viewHolder.dueDate.setText("Due in " + days + " days");
                convertView.setBackgroundColor(0xFFDFFFE4);
            }
            viewHolder.dueDate.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dueDate.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(0xFFEBEBEB);
        }
        return convertView;
    }
}
