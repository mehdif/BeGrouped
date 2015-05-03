package smartcity.begrouped.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.utils.MyApplication;

/**
 * Created by Anes on 03/05/2015.
 */
public class ReceiverNearestPoint extends BroadcastReceiver {

    AlarmManager am;
    PendingIntent pi;
    final static private long ONE_MINUTE = 60000;
    final static private long TWENTY_SECONDS = 20000;
    final static private long FIVE_MINUTES = ONE_MINUTE * 5;
    final static private long TWO_MINUTES = ONE_MINUTE * 2;
    final static private long FIVE_SECONDS = 5000;
    final static private long TWO_SECONDS = 2000;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("receiver", "receiver de nearest point");
            // Wa rrougi dir traitement te3ek hna



        pi = PendingIntent.getBroadcast(context, 0, new Intent(
                "com.authorwjf.nearestpoint"), 0);
        am = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIVE_SECONDS, pi);


    }
}