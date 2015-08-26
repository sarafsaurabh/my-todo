package com.example.mytodo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mytodo.database.TaskDatabaseHelper;
import com.example.mytodo.database.adapter.TaskAdapter;
import com.example.mytodo.database.model.Task;

import java.sql.Date;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity
        implements EditTaskDialog.EditTaskDialogListener {

    ArrayList<Task> tasks;
    TaskAdapter taskAdapter;
    ListView lvTasks;
    TaskDatabaseHelper taskDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskDbHelper = TaskDatabaseHelper.getInstance(this);
        lvTasks = (ListView)  findViewById(R.id.lvTasks);
        tasks = new ArrayList<Task>();
        readTasks();
        taskAdapter = new TaskAdapter(this, tasks);
        lvTasks.setAdapter(taskAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        //delete listener
        lvTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> adapter, View item, int pos, long id) {
                        taskDbHelper.deleteTask(tasks.get(pos));
                        tasks.remove(pos);
                        taskAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        //edit listener
        lvTasks.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {

                        //show edit task dialog
                        FragmentManager fm = getSupportFragmentManager();
                        EditTaskDialog dialog =
                                EditTaskDialog.newInstance(
                                        taskAdapter.getItem(pos).getValue(),
                                        pos,
                                        taskAdapter.getItem(pos).getDueDate());
                        dialog.show(fm, getClass().toString());
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds tasks to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar task clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddTask(View view) {
        EditText etNewTask = (EditText) findViewById(R.id.etNewTask);
        if(TextUtils.isEmpty(etNewTask.getText().toString())) {
            Toast.makeText(this, getString(R.string.enter_valid_task), Toast.LENGTH_SHORT).show();
            return;
        }
        Task task = new Task();
        task.setValue(etNewTask.getText().toString());
        Long id = taskDbHelper.addOrUpdateTask(task);
        task.setId(id);
        taskAdapter.add(task);
        etNewTask.setText("");
    }

    @Override
    public void onFinishEditDialog(String taskValue, int pos, Long dueDate) {
        //get and replace task at position
        Task task = tasks.get(pos);
        task.setValue(taskValue);
        if(dueDate != null) {
            task.setDueDate(new Date(dueDate));
        }
        tasks.set(pos, task);

        taskAdapter.notifyDataSetChanged();
        taskDbHelper.addOrUpdateTask(task);
        Toast.makeText(this, getString(R.string.task_edited), Toast.LENGTH_SHORT).show();
    }

    private void readTasks() {
        tasks = taskDbHelper.getTasks();
    }
}
