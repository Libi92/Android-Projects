package com.toch.utils;


        import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jibin on 10-04-2015.
 */


public class ProximityIntentReceiver extends BroadcastReceiver {
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("===","PROX2");
        mContext = context;
        Vibrator v = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        Log.d("=====>", "Viberating");
        long[] pattern = {0, 100, 1000};
        v.vibrate(pattern, 1);
        
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
        CharSequence text = "Viberating";

        SharedPreferences xpreff = mContext.getSharedPreferences("cbdata", 0);
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
                    Toast.makeText(mContext, "SMS sent.",
                            Toast.LENGTH_LONG).show();
                    

                } catch (Exception e) {
                    Toast.makeText(mContext,
                            "SMS faild, please try again.",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }}

        }
    }
}

