package com.toch.spotalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.toch.utils.DatabaseOperations3;

public class Todo extends Activity {

	private CheckBox chkIos;
    EditText txtphoneNo;
    String isc="n";
    String phoneNo="000";
    String name="000";
    Context mcontect =this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		chkIos = (CheckBox) findViewById(R.id.checkBox);
        txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        SharedPreferences pref = getSharedPreferences("lin", 0);
        name = pref.getString("KEY", null);
        //database_3
        DatabaseOperations3 DO  = new DatabaseOperations3(mcontect);
        Cursor CR = DO.getInformation(DO);
        Log.d("=====>", "todo");
        if (CR.moveToFirst()) {
            Log.d("=====>", "todo");

            do {
                if (name.equals(CR.getString(0))) {
                    Log.d(name, CR.getString(0));
                    phoneNo = (CR.getString(1));
                    Log.d(name, CR.getString(0));
                    Log.d(phoneNo, CR.getString(1));
                    Log.d(isc, CR.getString(2));
                    isc = (CR.getString(2));
                    txtphoneNo.setText(phoneNo);
                }
            } while (CR.moveToNext());
            check();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
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
	
	public void check() {
        String s="c";
        if(isc.equals(s)){
            Log.d("=====>", "check");

            chkIos.setChecked(true);
            SharedPreferences xpreff = getSharedPreferences("cbdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor xeditor = xpreff.edit();
            xeditor.putString("cb",("y"));
            xeditor.putString("ph",(phoneNo));
            xeditor.putString("nm",(name));
            xeditor.commit();

        }
        else{

            chkIos.setChecked(false);
            isc = "n";
            SharedPreferences xpreff = getSharedPreferences("cbdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor xeditor = xpreff.edit();
            xeditor.putString("cb",("n"));
            xeditor.putString("ph",(phoneNo));
            xeditor.putString("nm",(name));
            xeditor.commit();


        }
    }
    public void cbonclick(View v) {
        if (chkIos.isChecked()) {
            isc = "c";
            SharedPreferences xpreff = getSharedPreferences("cbdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor xeditor = xpreff.edit();
            xeditor.putString("cb",("y"));
            xeditor.putString("ph",(phoneNo));
            xeditor.putString("nm",(name));
            xeditor.commit();
        }else
        {
            isc = "n";
            SharedPreferences xpreff = getSharedPreferences("cbdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor xeditor = xpreff.edit();
            xeditor.putString("cb",("n"));
            xeditor.putString("ph",(phoneNo));
            xeditor.putString("nm",(name));
            xeditor.commit();
        }
    }
    public void xxx(View v) {
        phoneNo = txtphoneNo.getText().toString();
        DatabaseOperations3 DO  = new DatabaseOperations3(mcontect);

        DO.putInformation(DO,name,phoneNo,isc);
        Log.d("=====>", "Database_3_7");
        Intent intenti = new Intent(this, History.class);
        startActivity(intenti);
//        finish();
    }
}
