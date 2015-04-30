package smartcity.begrouped.utils;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;

public final class MyApplication extends Application {

    public   static User myIdentity;
    public   static Group currentGroup;


   /* @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "o0vvZbqThRgTotm9VKxeSfl7yaDebOfOa51sLXNc", "PMz0wBtgfmQVSJtINeBP85L1GwwbooeEMGu4tkMc");
    }*/
}
