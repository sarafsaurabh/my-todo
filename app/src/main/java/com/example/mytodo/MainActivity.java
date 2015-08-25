package com.example.mytodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

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
        // Get singleton instance of database
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
                        //bring up edit item activity
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("item", itemsAdapter.getItem(pos).getValue());
                        i.putExtra("pos", pos);
                        startActivityForResult(i, 0); // not using request code as of now
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
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode == RESULT_OK) {
            String itemValue = i.getStringExtra("item");
            int pos = i.getIntExtra("pos", 0);

            //get and replace item at position
            Item item = items.get(pos);
            item.setValue(itemValue);
            items.set(pos, item);

            itemsAdapter.notifyDataSetChanged();
            itemDbHelper.addOrUpdateItem(item);
            Toast.makeText(this, "Item successfully edited", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Unable to Edit Item", Toast.LENGTH_SHORT).show();
        }
    }


    private void readItems() {
        items = itemDbHelper.getItems();
    }
}
