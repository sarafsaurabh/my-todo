package com.example.mytodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mytodo.database.model.Item;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by ssaraf on 8/24/15.
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {

    private static ItemDatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ITEM = "item";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_VALUE = "value";
    private static final String KEY_ITEM_DUEDATE = "dueDate";

    private ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ItemDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new ItemDatabaseHelper(context.getApplicationContext());
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
        final String CREATE_ITEM_TABLE_SQL = "CREATE TABLE " + TABLE_ITEM +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_VALUE + " TEXT," +
                KEY_ITEM_DUEDATE + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEM_TABLE_SQL);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            onCreate(db);
        }
    }

    // Insert an item into the database
    // If the item id is null, we add the item, else update
    public Long addOrUpdateItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

//        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_VALUE, item.getValue());
            if(item.getDueDate() != null) {
                values.put(KEY_ITEM_DUEDATE, item.getDueDate().getTime());
            }

            if(item.getId() == null) {
                return db.insertOrThrow(TABLE_ITEM, null, values);
            } else {
                int rows = db.update(TABLE_ITEM, values, KEY_ITEM_ID + "= ?",
                        new String[]{item.getId().toString()});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to add item to database");
        }

        return null;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();

        final String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEM);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setId(cursor.getLong(cursor.getColumnIndex(KEY_ITEM_ID)));
                    item.setValue(cursor.getString(cursor.getColumnIndex(KEY_ITEM_VALUE)));
                    long dueDate = cursor.getLong(cursor.getColumnIndex(KEY_ITEM_DUEDATE));
                    if(dueDate != 0) {
                        item.setDueDate(new Date(dueDate));
                    }
                    items.add(item);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int row = db.delete(TABLE_ITEM, KEY_ITEM_ID + "= ?",
                    new String[]{item.getId().toString()});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(getClass().toString(), "Error while trying to delete item");
        }
    }
}