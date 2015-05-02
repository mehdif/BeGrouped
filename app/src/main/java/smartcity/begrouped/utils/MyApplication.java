package smartcity.begrouped.utils;

import android.app.Application;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.parse.Parse;

import java.util.LinkedList;

import smartcity.begrouped.model.Date;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.model.User;

public final class MyApplication extends Application {

    public   static User myIdentity;
    public   static Group currentGroup;
    public static boolean requestingMemberPositions=false;

    public static LocationManager locationManager=null;
    public static LocationListener locationListener;
    public static LinkedList<POI> listOfCurrentPOIS;
    public static Date dateOfCurrentProgram;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "o0vvZbqThRgTotm9VKxeSfl7yaDebOfOa51sLXNc", "PMz0wBtgfmQVSJtINeBP85L1GwwbooeEMGu4tkMc");
    }
}
