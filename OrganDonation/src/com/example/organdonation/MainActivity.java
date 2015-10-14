package com.example.organdonation;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organdonation.utils.Globals;
import com.example.organdonation.utils.MessageBean;
import com.example.organdonation.utils.WebServiceClient;
import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity {

	private Socket socket;
	private Context mContext;
	private Button buttonStart;
	private Button buttonStop;
	Timer timer;
	SharedPreferences sp;
	Editor editor;
	boolean ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		
		sp=getSharedPreferences(Globals.PREFERENCE_NAME, Context.MODE_PRIVATE);

		buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStop = (Button) findViewById(R.id.buttonStop);
		
		buttonStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Service Started", Toast.LENGTH_SHORT)
						.show();
				
				Reminder(5);
			}
		});

		buttonStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Service Stoped", Toast.LENGTH_SHORT)
						.show();
				timer.cancel();
			}
		});
	}
	public void Reminder(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), 0, seconds*1000);
	}
	
	class RemindTask extends TimerTask {
        public void run() {
            System.out.format("Time's up!%n");
            new GetSMS().execute();
//            timer.cancel(); //Terminate the timer thread
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main_meu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.menu.main_meu){
			set_ip();
		}
		else {
			set_ip();
		}
		return true;
	}
	
	public void set_ip() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("IP Address");
		alert.setMessage("Set IP");
		// Create TextView
		final EditText input = new EditText(this);
		input.setText(sp.getString(Globals.SERVER_IP, "10.0.2.2"));
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String ip_addr = input.getText().toString();
				ip = Globals.ip_validate(ip_addr);
				if (ip == true) {
					editor=sp.edit();
					editor.putString(Globals.SERVER_IP, ip_addr);
					editor.commit();
					Globals.setServer(ip_addr);
				} else {

					Toast.makeText(getApplicationContext(), "ip not valid",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						dialog.dismiss();
					}
				});
		alert.show();

	}
	
	public class GetSMS extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			String result = WebServiceClient.GetSMS();
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.d("Result", result);
			
			MessageBean[] beans = new Gson().fromJson(result, MessageBean[].class);
			
			SmsManager manager = SmsManager.getDefault();
			for (MessageBean messageBean : beans) {
				
				manager.sendTextMessage(messageBean.getNumber(), null, messageBean.getMessage(), null, null);
			}

			super.onPostExecute(result);
		}
	}

}
