package com.toch.spotalarm;

import android.app.Activity;
import android.content.Context;
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

public class NewUser extends Activity {

	EditText USER_NAME, USER_PASS, CON_PASS;
    String user_name, user_pass, con_pass;
    Button REG;
    Context CTX = this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
		USER_NAME = (EditText) findViewById(R.id.reg_user);
        USER_PASS = (EditText) findViewById(R.id.reg_pass);
        CON_PASS = (EditText) findViewById(R.id.con_pass);
        REG = (Button) findViewById(R.id.user_reg);
        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USER_NAME.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                if ((USER_NAME.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && USER_NAME.length() > 0)) {
                    user_name = USER_NAME.getText().toString();
                    user_pass = USER_PASS.getText().toString();
                    con_pass = CON_PASS.getText().toString();
                    if (!(user_pass.equals(con_pass)))
                    {
                        Toast.makeText(getBaseContext(), "Passwords are not matching", Toast.LENGTH_LONG).show();
                        USER_NAME.setText("");
                        USER_PASS.setText("");
                        CON_PASS.setText("");
                    } else {
                        int poss = 0;
                        DatabaseOperations DB = new DatabaseOperations(CTX);
                        Cursor CR = DB.getInformation(DB);
                        if (CR.moveToFirst())
                        {
                            do {
                                if (user_name.equals(CR.getString(0))) {
                                    poss = 1;
                                }
                            } while (CR.moveToNext());
                        }if (poss == 0)
                        {
                            DB.putInformation(DB, user_name, user_pass);
                            Toast.makeText(getBaseContext(), "Registration Sucess", Toast.LENGTH_LONG).show();
//                            Intent intenti = new Intent(getApplicationContext(), Login.class);
//                            startActivity(intenti);
                            finish();
                        } else
                        {
                            Toast.makeText(getBaseContext(), "Username Exists!!!", Toast.LENGTH_LONG).show();
                            USER_NAME.setText("");
                            USER_PASS.setText("");
                            CON_PASS.setText("");
                        }

                    }
//                    finish();
                }else{ Toast.makeText(getBaseContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                }


            }

        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
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
}
