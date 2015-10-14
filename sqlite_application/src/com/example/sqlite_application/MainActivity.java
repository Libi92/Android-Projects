package com.example.sqlite_application;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        
        ((Button)findViewById(R.id.buttonFind)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String name = ((EditText)findViewById(R.id.editText1)).getText().toString();
				
				List<String> data = sqLiteHelper.getAllRecord(name);
				
				Adapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, data);
				((ListView)findViewById(R.id.listView1)).setAdapter((ListAdapter) adapter);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
