package com.example.mytodo.database.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mytodo.R;
import com.example.mytodo.database.model.Task;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;

/**
 * Created by ssaraf on 8/24/15.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    private static class ViewHolder {
        private TextView task;
        private TextView id;
        private TextView dueDate;

    }

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, R.layout.task, tasks);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.task, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.task = (TextView) convertView.findViewById(R.id.tvValue);
            viewHolder.id = (TextView) convertView.findViewById(R.id.tvId);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);

            viewHolder.id.setVisibility(View.INVISIBLE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task task = getItem(position);
        viewHolder.task.setText(task.getValue());
        viewHolder.task.setTextColor(Color.BLACK);
        viewHolder.id.setText(task.getId().toString());
        if(task.getDueDate() != null) {
            int days = Days.daysBetween(new DateTime(), new DateTime(task.getDueDate())).getDays();
            if(days < 0) {
                viewHolder.dueDate.setText(getContext().getString(R.string.already_due));
                convertView.setBackgroundColor(getContext().getColor(R.color.lightred));
            } else if (days == 0) {
                viewHolder.dueDate.setText(getContext().getString(R.string.due_today));
                convertView.setBackgroundColor(getContext().getColor(R.color.lightyellow));
            } else if (days == 1) {
                viewHolder.dueDate.setText(getContext().getString(R.string.due_tomorrow));
                convertView.setBackgroundColor(getContext().getColor(R.color.lightyellow));
            } else {
                viewHolder.dueDate.setText(
                        getContext().getString(R.string.due_in) +
                                days +
                                getContext().getString(R.string.days));
                convertView.setBackgroundColor(getContext().getColor(R.color.lightgreen));
            }
            viewHolder.dueDate.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dueDate.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(getContext().getColor(R.color.lightgrey));
        }
        return convertView;
    }
}
