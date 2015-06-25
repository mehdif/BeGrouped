package smartcity.begrouped.utils;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import smartcity.begrouped.model.Group;

/**
 * Created by a on 20/06/2015.
 */
public class PushNotificationService{
    private static String objectId=null;

    public static void sendNotification(String title, String content, String userName, String groupName){
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("alert", content);
            if(groupName == null){
                jsonObject.put("intent", "HomeFragment");
                jsonObject.put("action","accept");
            }else{
                jsonObject.put("intent", "MembersWaitingFragment");
                jsonObject.put("action","join");
                jsonObject.put("group",groupName);
            }


            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("username", userName);
            List<ParseUser> users=userQuery.find();
            for(int i=0;i<users.size();i++){
                if(users.get(i).getUsername().equals(userName)){
                    objectId=users.get(i).getObjectId();
                    break;
                }
            }
            ParsePush push = new ParsePush();
            ParseQuery query = ParseInstallation.getQuery();
            query.whereEqualTo("deviceType", "android");
            query.whereEqualTo("userName",objectId );
            push.setQuery(query);
            push.setData(jsonObject);
            push.sendInBackground();
        }catch(Exception e){
            Log.v("Parse","An exception has occured!");
        }
    }

    public static void sendNotification(String title,String content,String recipientId){
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("alert", content);
            jsonObject.put("title", title);
            jsonObject.put("intent","ChatActivity");
            jsonObject.put("action","chat");

            ParsePush push = new ParsePush();
            ParseQuery query = ParseInstallation.getQuery();
            query.whereEqualTo("deviceType", "android");
            query.whereEqualTo("userName",recipientId);
            push.setQuery(query);
            push.setData(jsonObject);
            push.sendInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
