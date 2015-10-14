package com.toch.spotalarm;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.toch.utils.DatabaseOperations2;
import com.toch.utils.ItemModel;
import com.toch.utils.ListItemAdapter;

public class History extends Activity {

	DatabaseOperations2 databaseHelper;
    Context mcontext = this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		mcontext = History.this;
		
		databaseHelper = new DatabaseOperations2(this);
        Cursor cr = databaseHelper.getAllRecords();
        startManagingCursor(cr);
        String[] from = new String[]{ databaseHelper.HISTORY_COLUMN_LAT, databaseHelper.HISTORY_COLUMN_LNG,databaseHelper.HISTORY_COLUMN_NAME};
        int[] to = new int[]{R.id.item_lat, R.id.item_lng};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_layout, cr, from, to);
        
//        cr.moveToFirst();
        Log.d("Cursor Size", cr.getCount() + "");
        ArrayList<ItemModel> items = new ArrayList<ItemModel>();
        while (cr.moveToNext()) {
			ItemModel item = new ItemModel();
			item.setLatitude(cr.getString(cr.getColumnIndex(DatabaseOperations2.HISTORY_COLUMN_LAT)));
			item.setLongitude(cr.getString(cr.getColumnIndex(DatabaseOperations2.HISTORY_COLUMN_LNG)));
			item.setLocation(cr.getString(cr.getColumnIndex(DatabaseOperations2.HISTORY_COLUMN_LOCATION)));
			
			items.add(item);
		}
        
        ListItemAdapter adapter2 = new ListItemAdapter(mcontext, items, getResources());
        
        final ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter2);
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
                AlertDialog alertDialog = new AlertDialog.Builder(History.this)
				.setTitle("Delete Alert")
				.setMessage("Do you realy want to delete this alert..?")
				.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
						String lat = ((TextView) view.findViewById(R.id.item_lat)).getText().toString();
		                String lng = ((TextView) view.findViewById(R.id.item_lng)).getText().toString();
		                
		                Log.d("LatLng", lat + ", " + lng);
		                
		                DatabaseOperations2 db = new DatabaseOperations2(mcontext);
		                db.delete(lat, lng);
		                
//						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				})
				.create();
alertDialog.show();
                
				return false;
			}
		});
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

            	try{
                String lat = ((TextView) view.findViewById(R.id.item_lat)).getText().toString();
                String lng = ((TextView) view.findViewById(R.id.item_lng)).getText().toString();
                SharedPreferences preff = getSharedPreferences("ll", Context.MODE_PRIVATE);
                SharedPreferences.Editor mmeditor = preff.edit();
                mmeditor.putString("la",(lat)); // Storing string
                mmeditor.putString("ln",(lng));
                mmeditor.commit();
                Intent intentx = new Intent(getApplicationContext(), ProxAlertActivity.class);
                startActivity(intentx);
            	}
            	catch(Exception ex){
            		
            	}
//                finish();
            }
        });
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void vmap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
//        finish();
    }
	
	public void lgoff(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
//        finish();
    }
    public void todo(View view) {
        Intent intent = new Intent(this, Todo.class);
        startActivity(intent);
//        finish();
    }
}
