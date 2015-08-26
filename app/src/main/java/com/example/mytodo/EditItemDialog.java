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

public class EditItemDialog extends DialogFragment implements TextView.OnClickListener {

    Long dueDate = null;

    public EditItemDialog() {
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(String itemValue, int pos, Long dueDate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_edit_item, container);

        EditText etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        String itemValue = getArguments().getString("item");
        etEditItem.setText(itemValue);
        etEditItem.setSelection(itemValue.length());

        Button btnEditItem = (Button) view.findViewById(R.id.btnEditItem);

        btnEditItem.setOnClickListener(this);

        CalendarView cal = (CalendarView) view.findViewById(R.id.calendarView);
        cal.setDate(getArguments().getLong("dueDate"));
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = new GregorianCalendar(year, month, day);
                dueDate = c.getTimeInMillis();
            }
        });
        return view;
    }

    public static EditItemDialog newInstance(String itemValue, int pos, Date dueDate) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString("item", itemValue);
        args.putInt("pos", pos);
        if(dueDate != null) {
            args.putLong("dueDate", dueDate.getTime());
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onClick(View view) {

        EditText etEditItem = (EditText) this.getView().findViewById(R.id.etEditItem);
        if (TextUtils.isEmpty(etEditItem.getText().toString())) {
            Toast.makeText(view.getContext(), "Item value is invalid", Toast.LENGTH_SHORT).show();
        }
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(
                etEditItem.getText().toString(), getArguments().getInt("pos"), dueDate);
        dismiss();
    }
}
