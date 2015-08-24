package com.example.mytodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.Strings;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemValue = getIntent().getStringExtra("item");
        etEditItem.setText(itemValue);
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

    public void onEditItem(View view) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        if(Strings.isNullOrEmpty(etEditItem.getText().toString())) {
            Toast.makeText(this, "Item value is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data intent
        Intent i = new Intent();
        // Pass relevant data back as a result
        i.putExtra("item", etEditItem.getText().toString());
        i.putExtra("pos", getIntent().getIntExtra("pos", 0));

        // Activity finished ok, return the data
        setResult(RESULT_OK, i); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
