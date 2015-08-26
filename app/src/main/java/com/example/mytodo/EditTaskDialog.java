package com.example.mytodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditTaskDialog extends DialogFragment implements TextView.OnClickListener {

    Long dueDate = null;

    public EditTaskDialog() {
    }

    public interface EditTaskDialogListener {
        void onFinishEditDialog(String taskValue, int pos, Long dueDate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_edit_task, container);

        EditText etEditTask = (EditText) view.findViewById(R.id.etEditTask);
        String taskValue = getArguments().getString("task");
        etEditTask.setText(taskValue);
        etEditTask.setSelection(taskValue.length());

        Button btnEditTask = (Button) view.findViewById(R.id.btnEditTask);

        btnEditTask.setOnClickListener(this);

        CalendarView cal = (CalendarView) view.findViewById(R.id.calendarView);
        if(getArguments().getLong("dueDate") != 0) {
            cal.setDate(getArguments().getLong("dueDate"));
        }
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = new GregorianCalendar(year, month, day);
                dueDate = c.getTimeInMillis();
            }
        });
        return view;
    }

    public static EditTaskDialog newInstance(String taskValue, int pos, Date dueDate) {
        EditTaskDialog frag = new EditTaskDialog();
        Bundle args = new Bundle();
        args.putString("task", taskValue);
        args.putInt("pos", pos);
        if(dueDate != null) {
            args.putLong("dueDate", dueDate.getTime());
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onClick(View view) {

        EditText etEditTask = (EditText) this.getView().findViewById(R.id.etEditTask);
        if (TextUtils.isEmpty(etEditTask.getText().toString())) {
            Toast.makeText(view.getContext(), R.string.enter_valid_task, Toast.LENGTH_SHORT).show();
        }
        EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
        listener.onFinishEditDialog(
                etEditTask.getText().toString(), getArguments().getInt("pos"), dueDate);
        dismiss();
    }
}
