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
import java.util.concurrent.ExecutionException;

import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.MyApplication;

/**
 * Created by a on 27/04/2015.
 */
public class UserManager {

    static InputStream is = null;
    static String chaine = "";


    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;


    //public static User myIdentity;




/*
    public static void createMeForTest() {
        myIdentity = new User("Gerard", "Pique", "GerPique", "passwd", "777777777");
        myIdentity.setLocalisation(new Location(45, 6));
    }*/
/*
    public static User signIn(String userName, String password) {
        createMeForTest();
        return myIdentity;
    }
*/
/*    public static User signUp(String firstname, String lastname, String username, String password) {
        createMeForTest();
        return myIdentity;
    }
*/
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


 //public static void createTask(){
       // new TaskGetJson().execute();
   // }


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
            String jsonFileUrl = getFromUrl("http://smartpld-001-site1.smarterasp.net/index.php/position_controller/sendmyposition/"+location.latitude+"/"+location.longitude+"/"+MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
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
            Log.i("send Apt", "send"+MyApplication.currentGroup.getAppointment().getLocation().getLatitude());
            String jsonFileUrl = getFromUrl(AllUrls.BASE +AllUrls.CREATE_RDV+
                    "/"+MyApplication.currentGroup.getName()+"/"+
                    MyApplication.currentGroup.getAppointment().getLocation().getLatitude()+
                    "/"+MyApplication.currentGroup.getAppointment().getLocation().getLongitude()+
                    "/"+MyApplication.currentGroup.getAppointment().getDateSousForme()+
                    "/"+MyApplication.currentGroup.getAppointment().getTemps().getHh()+
                    "/"+MyApplication.currentGroup.getAppointment().getTemps().getMm()+
                    "/0/"+MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
            return null;
        }
    }

    public static User createUserFromJson(String userName,String passWord){

        String jsonFileUrl = getFromUrl(AllUrls.AUTHENTIFICATE_USER_URL+userName+"/"+passWord);

        Log.v("Jsonfile : " , jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);

            String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
            String lastname = (String) jsonObject.get(Constants.LAST_NAME);
            String username = (String) jsonObject.get(Constants.USERNAME);
            String password = (String) jsonObject.get(Constants.PASSWORD);
            String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

            return new User(firstname, lastname, username, password, phoneNumber);

        }
        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return null;
        }
    }

    public static String insertUser(String userName,String passWord,String firstName,String lastName,String phonenumber){

        String jsonFileUrl = getFromUrl(AllUrls.REGISTER_USER_URL+userName+"/"+passWord+"/"+firstName+"/"+lastName+"/"+phonenumber);
        Log.v("Jsonfile : " , jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);

            String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
            String lastname = (String) jsonObject.get(Constants.LAST_NAME);
            String username = (String) jsonObject.get(Constants.USERNAME);
            String password = (String) jsonObject.get(Constants.PASSWORD);
            String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);
            MyApplication.myIdentity=new User(firstname,lastname,username,password,phoneNumber);

        }

        catch(Exception e){
            Log.e("Error : ", e.getMessage());






        }
        return jsonFileUrl;



    }
    public static String createUserFromJson1(){

        String jsonFileUrl = getFromUrl(AllUrls.AUTHENTIFICATE_ME+"/"+ MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("URL",AllUrls.AUTHENTIFICATE_ME+"/"+MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("username: ",MyApplication.myIdentity.getUsername());
        Log.v("pwd: ",MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile1 : " ,jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);
            String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
            String lastname = (String) jsonObject.get(Constants.LAST_NAME);
            String username = (String) jsonObject.get(Constants.USERNAME);
            String password = (String) jsonObject.get(Constants.PASSWORD);
            String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

            return  jsonFileUrl;

        }



        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return " ";

        }







    }


    public static Object authenticate(String username,String password)
    {
        try
        {
            return  new TaskAuthenticate().execute(username,password).get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void register(String username,String password,String firstname,String lastname,String phonenumber)
    {


        new TaskRegister().execute(username,password,firstname,lastname,phonenumber);


    }


    public static String authenticate1()
    {
        try
        {
            return  new TaskAuthenticate1().execute().get().toString();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static class TaskAuthenticate extends AsyncTask {

        @Override
        protected User doInBackground(Object[] params) {

            Log.v("JSON",params[0].toString());
            Log.v("JSON",params[1].toString());
            return createUserFromJson(params[0].toString(),params[1].toString());
        }









    }
    public static class TaskRegister extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            insertUser(params[0].toString(),params[1].toString(),params[2].toString(),params[3].toString(),params[4].toString());


            return null ;
        }
    }
    public static class TaskAuthenticate1 extends AsyncTask {

        @Override
        protected String doInBackground(Object[] params)
        {
            return createUserFromJson1();
        }

    }
}




