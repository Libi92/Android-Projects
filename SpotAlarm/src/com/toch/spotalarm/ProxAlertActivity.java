package com.toch.spotalarm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.toch.utils.GPSTracker;
import com.toch.utils.ProximityIntentReceiver;

public class ProxAlertActivity extends Activity implements LocationListener {
    private static final long POINT_RADIUS = 1000; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;
    private static final String PROX_ALERT_INTENT =
            "com.toch.spotalarm";
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText dalat;
    private EditText dalng;
    private LocationManager locationManager;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prox_alert);
        Log.d("===", "PROX1");
        
        sp=getSharedPreferences("lin", 0);
        latitudeEditText = (EditText) findViewById(R.id.point_latitude);
        longitudeEditText = (EditText) findViewById(R.id.point_longitude);
        dalat = (EditText) findViewById(R.id.dlat);
        dalng = (EditText) findViewById(R.id.dlng);
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            latitudeEditText.setText(stringLatitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            longitudeEditText.setText(stringLongitude);
        }
        calc();
    };



    public void stp(View view) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.cancel();
        GPSTracker gpsTracker = new GPSTracker(this);
        gpsTracker.stopUsingGPS();
//        BroadcastReceiver mybroadcast = new ProximityIntentReceiver();
//        unregisterReceiver(mybroadcast);
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
        finish();
    }




    private void calc() {
        SharedPreferences preff = getSharedPreferences("ll", 0);
        String lal = preff.getString("la", null);
        String lnl = preff.getString("ln", null);
        Log.d("=====>", lal);
        Log.d("=====>", lnl);
        double la = Double.parseDouble(lal);
        double ln = Double.parseDouble(lnl);
        dalat.setText(lal);
        dalng.setText(lnl);
        addProximityAlert(la, ln);
    }



    private void addProximityAlert(double latitude, double longitude) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Log.d("===","addprOX");

        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );
        Log.d("===", "PROXadddone");

        
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(), filter);
        Log.d("===", "PROXadddone...2");
        Location l=new Location("");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        
        Location l2=new Location("");
        l2.setLatitude(Double.parseDouble(sp.getString("c_lat", "9.96")));
        l2.setLongitude(Double.parseDouble(sp.getString("c_lng", "75")));
        
        float dist=l.distanceTo(l2);
        Log.d("Distance", dist+"");
        if(dist <= POINT_RADIUS){
        vibrate();
        }

    }




    public void vibrate()
    {
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Log.d("=====>", "Viberating");
        long[] pattern = {0, 100, 1000};
        v.vibrate(pattern, 1);
        CharSequence text = "Viberating";

        SharedPreferences xpreff = getSharedPreferences("cbdata", 0);
        String name = xpreff.getString("nm", null);
        String phoneNo = xpreff.getString("ph", null);
        String cb = xpreff.getString("cb", null);
        String message = "Reached";
        Log.d("===", "mesp");
        if (cb!=null) {
            if(cb.equals("y")){
                Log.d("===","y.....try");

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(this, "SMS sent.",
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(this,
                            "SMS faild, please try again.",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }}

        }
        
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }




}