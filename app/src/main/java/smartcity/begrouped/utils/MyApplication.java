package smartcity.begrouped.utils;

import android.app.Application;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.parse.Parse;

import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;

public final class MyApplication extends Application {

    public   static User myIdentity;
    public   static Group currentGroup;
    public static boolean requestingMemberPositions=false;

    public static LocationManager locationManager=null;
    public static LocationListener locationListener;


}
