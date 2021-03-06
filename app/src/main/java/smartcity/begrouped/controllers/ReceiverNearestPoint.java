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
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        if (MyApplication.myIdentity != null) {
            Log.i("receiver", "receiver de nearest point");
            if (MyApplication.myPosition != null) {
                 POIManager.getNearestPoiByTask(MyApplication.myPosition.getLatitude(), MyApplication.myPosition.getLongitude(),this);
            }
            else {
                pi = PendingIntent.getBroadcast(context, 0, new Intent(
                        "com.authorwjf.nearestpoint"), 0);
                am = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + TWENTY_SECONDS, pi);
            }
        }
    }
    public void continuerTraitement(LinkedList<POI> listPOI){
        if (listPOI != null) {
            Intent intent1 = new Intent(context, MapsActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            intent1.putExtra("name",listPOI.get(0).getName());
            intent1.putExtra("type",listPOI.get(0).getType());
            intent1.putExtra("latitude",listPOI.get(0).getLocation().getLatitude());
            intent1.putExtra("longitude",listPOI.get(0).getLocation().getLongitude());

            Notification noti = new Notification.Builder(context)
                    .setContentTitle("You are near " + listPOI.get(0).getName())
                    .setContentText(listPOI.get(0).getType().replace("_"," ")).setSmallIcon(R.drawable.monument)
                    .setContentIntent(pIntent).getNotification();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
            // hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, noti);
        }
        pi = PendingIntent.getBroadcast(context, 0, new Intent(
                "com.authorwjf.nearestpoint"), 0);
        am = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWENTY_SECONDS, pi);
    }
}