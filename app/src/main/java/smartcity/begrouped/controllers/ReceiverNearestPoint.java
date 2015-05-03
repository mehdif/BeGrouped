package smartcity.begrouped.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.activity.AuthentificationActivity;
import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.model.POI;
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
        if (MyApplication.myIdentity != null) {
            Log.i("receiver", "receiver de nearest point");

            pi = PendingIntent.getBroadcast(context, 0, new Intent(
                    "com.authorwjf.nearestpoint"), 0);
            am = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + FIVE_SECONDS, pi);

            // Prepare intent which is triggered if the
            // notification is selected
            Intent intent1 = new Intent(context, AuthentificationActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);

            // Build notification
            // Actions are just fake
            if (MyApplication.myPosition != null) {

                LinkedList<POI> listPOI = POIManager.getNearestPoiByTask(MyApplication.myPosition.getLatitude(), MyApplication.myPosition.getLongitude());

                if (listPOI != null) {
                    Notification noti = new Notification.Builder(context)
                            .setContentTitle("You are near " + listPOI.get(0).getName())
                            .setContentText(listPOI.get(0).getType()).setSmallIcon(R.drawable.monument)
                            .setContentIntent(pIntent).build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, noti);
                }

            }
        }
    }
}