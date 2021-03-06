package smartcity.begrouped.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import smartcity.begrouped.activity.MainActivity;

/**
 * Created by Anes on 01/05/2015.
 */
public class GlobalMethodes {

    public static boolean isNumeric(char n) {
        return (n=='0' || n=='1'|| n=='2'|| n=='3'|| n=='4'|| n=='5'|| n=='6'|| n=='7'|| n=='8'|| n=='9');

    }


    public static String getFromUrl(String url) {
        String chaine="";
        InputStream is = null;
        try {
            Log.v("aymen","hello from getFromUrl");
            chaine = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            Log.v("aymen","hello from getFromUrl before httpPost");
            HttpPost httpPost = new HttpPost(url);
            Log.v("aymen","hello from getFromUrl after httpPost");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.v("aymen","hello from getFromUrl after httpResponse");
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                chaine = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return chaine;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.v("aymen","Probleme here : "+e.getMessage());
            e.printStackTrace();
        }
        return "0001"; //now, any problem will be treated as a network problem
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRecipientId(String recipientName){
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", recipientName);
        List<ParseUser> users= null;
        String recipientId="";
        try {
            users = userQuery.find();
            for(int i=0;i<users.size();i++){
                if(users.get(i).getUsername().equals(recipientName)){
                    recipientId = users.get(i).getObjectId();
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return recipientId;
    }
}
