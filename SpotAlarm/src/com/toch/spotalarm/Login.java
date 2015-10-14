package com.toch.spotalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toch.utils.DatabaseOperations;

public class Login extends Activity {

	Button Login,Register;
    EditText USERNAME,USERPASS;
    String username,userpass;
    Context CTX = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Login = (Button) findViewById(R.id.Login);
        Login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        Register = (Button) findViewById(R.id.Register);
        USERNAME = (EditText) findViewById(R.id.user_name);
        USERPASS = (EditText) findViewById(R.id.user_pass);
        USERNAME.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	
	public void vcheck(View view) {
        username = USERNAME.getText().toString();
        userpass = USERPASS.getText().toString();
        DatabaseOperations DOP  = new DatabaseOperations(CTX);
        Cursor CR = DOP.getInformation(DOP);
        SharedPreferences pref = getSharedPreferences("lin", Context.MODE_PRIVATE);
        SharedPreferences.Editor meditor = pref.edit();
        boolean login_status = false;
        if (CR.moveToFirst()) {
            do {
                if (username.equals(CR.getString(0)) && userpass.equals(CR.getString(1))) {
                    login_status = true;
                    meditor.putString("KEY", username); // Storing string
                    meditor.commit();
                }
            } while (CR.moveToNext());
        }
        if(login_status)
        {
            Toast.makeText(getBaseContext(),"Login Success",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, History.class);
            startActivity(intent);
//            finish();
        }
        else
        {
            Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
//            finish();
        }
    }



    public void regnew(View view) {
        Intent intenti = new Intent(this, NewUser.class);
        startActivity(intenti);
//        finish();
    }
}
