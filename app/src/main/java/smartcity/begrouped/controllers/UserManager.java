package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.spec.ECField;
import java.util.List;

import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;

/**
 * Created by a on 27/04/2015.
 */
public class UserManager {

    static InputStream is = null;
    static String chaine = "";

    public final static String LAST_NAME = "lastName";
    public final static String FIRST_NAME = "firstName";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "hashPwd";
    public final static String PHONE_NUMBER = "phone";

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;


    public static User myIdentity;


    public static void insertUser(String firstname, String lastname, String username, String password, String phoneNumber) {
        User user = new User(firstname, lastname, username, password, phoneNumber);
        //
    }



    public static void createMeForTest() {
        myIdentity = new User("Gerard", "Pique", "GerPique", "passwd", "777777777");
        myIdentity.setLocalisation(new Location(45, 6));
    }

    public static User signIn(String userName, String password) {
        createMeForTest();
        return myIdentity;
    }

    public static User signUp(String firstname, String lastname, String username, String password) {
        createMeForTest();
        return myIdentity;
    }

    public static String getFromUrl(String url) {
        try {

            chaine = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

    public static User createUserFromJson(){

        String jsonFileUrl = getFromUrl("http://smartpld-001-site1.smarterasp.net/index.php/login/ba_belfodil/0000");

        Log.v("Jsonfile : " , jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);

            String firstname = (String) jsonObject.get(FIRST_NAME);
            String lastname = (String) jsonObject.get(LAST_NAME);
            String username = (String) jsonObject.get(USERNAME);
            String password = (String) jsonObject.get(PASSWORD);
            String phoneNumber = (String) jsonObject.get(PHONE_NUMBER);

            return new User(firstname, lastname, username, password, phoneNumber);

        }
        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return null;
        }
    }


    public static void createTask(){
        new TaskGetJson().execute();
    }

    public static class TaskGetJson extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {

            createUserFromJson();
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //GroupManager.createTask();
        }
    }



    public static void updateMyLocationToServer(LatLng location){
        TaskUpdateMyLocation task=new TaskUpdateMyLocation(location);
        task.execute();

    }

    public static class TaskUpdateMyLocation extends AsyncTask {

        LatLng location;
        public TaskUpdateMyLocation(LatLng location){
            super();
            this.location=location;
        }
        @Override
        protected Object doInBackground(Object[] params) {

            Log.i("send position", "send position to server" );
            String jsonFileUrl = getFromUrl("http://smartpld-001-site1.smarterasp.net/index.php/position_controller/sendmyposition/"+location.latitude+"/"+location.longitude);
            return null;
        }
    }



    // jdid
    public static void sendAptToServer(Appointment appointment){
        TaskSendApt task=new TaskSendApt(appointment);
        task.execute();
    }

    public static class TaskSendApt extends AsyncTask {

        Appointment apt;
        public TaskSendApt(Appointment apt){
            super();
            this.apt=apt;
        }
        @Override
        protected Object doInBackground(Object[] params) {

            Log.i("send Apt", "send appointment to server" );
            //String jsonFileUrl = getFromUrl("URL pour envoyer l'APT");
            return null;
        }
    }


}




