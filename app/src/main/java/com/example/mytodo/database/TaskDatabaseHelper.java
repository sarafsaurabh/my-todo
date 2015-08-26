package com.example.mytodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mytodo.database.model.Task;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by ssaraf on 8/24/15.
 */
public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static TaskDatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_TASK = "task";

    // Task Table Columns
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_VALUE = "value";
    private static final String KEY_TASK_DUEDATE = "dueDate";

    private TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TaskDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new TaskDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TASK_TABLE_SQL = "CREATE TABLE " + TABLE_TASK +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TASK_VALUE + " TEXT," +
                KEY_TASK_DUEDATE + " TEXT" +
                ")";

        db.execSQL(CREATE_TASK_TABLE_SQL);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
            onCreate(db);
        }
    }

    // Insert an task into the database
    // If the task id is null, we add the task, else update
    public Long addOrUpdateTask(Task task) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

//        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_VALUE, task.getValue());
            if(task.getDueDate() != null) {
                values.put(KEY_TASK_DUEDATE, task.getDueDate().getTime());
            }

            if(task.getId() == null) {
                return db.insertOrThrow(TABLE_TASK, null, values);
            } else {
                int rows = db.update(TABLE_TASK, values, KEY_TASK_ID + "= ?",
                        new String[]{task.getId().toString()});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to add task to database");
        }

        return null;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        final String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TASK);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getLong(cursor.getColumnIndex(KEY_TASK_ID)));
                    task.setValue(cursor.getString(cursor.getColumnIndex(KEY_TASK_VALUE)));
                    long dueDate = cursor.getLong(cursor.getColumnIndex(KEY_TASK_DUEDATE));
                    if(dueDate != 0) {
                        task.setDueDate(new Date(dueDate));
                    }
                    tasks.add(task);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int row = db.delete(TABLE_TASK, KEY_TASK_ID + "= ?",
                    new String[]{task.getId().toString()});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to delete task");
        }
    }
}