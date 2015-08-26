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

import com.example.mytodo.database.ItemDatabaseHelper;
import com.example.mytodo.database.adapter.ItemAdapter;
import com.example.mytodo.database.model.Item;

import java.sql.Date;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity
        implements EditItemDialog.EditItemDialogListener {

    ArrayList<Item> items;
    ItemAdapter itemsAdapter;
    ListView lvItems;
    ItemDatabaseHelper itemDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemDbHelper = ItemDatabaseHelper.getInstance(this);
        lvItems = (ListView)  findViewById(R.id.lvItems);
        items = new ArrayList<Item>();
        readItems();
        itemsAdapter = new ItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        //delete listener
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> adapter, View item, int pos, long id) {
                        itemDbHelper.deleteItem(items.get(pos));
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        //edit listener
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {

                        //show edit item dialog
                        FragmentManager fm = getSupportFragmentManager();
                        EditItemDialog dialog =
                                EditItemDialog.newInstance(
                                        itemsAdapter.getItem(pos).getValue(),
                                        pos,
                                        itemsAdapter.getItem(pos).getDueDate());
                        dialog.show(fm, getClass().toString());
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        if(TextUtils.isEmpty(etNewItem.getText().toString())) {
            Toast.makeText(this, "Item value is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        Item item = new Item();
        item.setValue(etNewItem.getText().toString());
        Long id = itemDbHelper.addOrUpdateItem(item);
        item.setId(id);
        itemsAdapter.add(item);
        etNewItem.setText("");
    }

    @Override
    public void onFinishEditDialog(String itemValue, int pos, Long dueDate) {
        //get and replace item at position
        Item item = items.get(pos);
        item.setValue(itemValue);
        if(dueDate != null) {
            item.setDueDate(new Date(dueDate));
        }
        items.set(pos, item);

        itemsAdapter.notifyDataSetChanged();
        itemDbHelper.addOrUpdateItem(item);
        Toast.makeText(this, "Item successfully edited", Toast.LENGTH_SHORT).show();
    }

    private void readItems() {
        items = itemDbHelper.getItems();
    }
}
