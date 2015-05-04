package smartcity.begrouped.controllers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.getFromUrl;

/**
 * Created by a on 27/04/2015.
 */
public class UserManager {

    /**
     * authenticate the user by MAJ the myIdentity variable
     * @param json : the json file returned by the server
     */
    public static void authenticateUser(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
            String lastname = (String) jsonObject.get(Constants.LAST_NAME);
            String username = (String) jsonObject.get(Constants.USERNAME);
            String password = (String) jsonObject.get(Constants.PASSWORD);
            String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);
            MyApplication.myIdentity = new User(firstname, lastname, username, password, phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updateMyLocationToServer(LatLng location) {
        TaskUpdateMyLocation task = new TaskUpdateMyLocation(location);
        task.execute();
    }

    public static class TaskUpdateMyLocation extends AsyncTask {

        LatLng location;

        public TaskUpdateMyLocation(LatLng location) {
            super();
            this.location = location;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            Log.i("send position", "send position to server");
            try {
                String jsonFileUrl = getFromUrl("http://smartpld-001-site1.smarterasp.net/index.php/position_controller/sendmyposition/" + location.latitude + "/" + location.longitude + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
            } catch (NullPointerException e) {
                Log.e("update position", e.getMessage());
            }
            return null;
        }
    }
    // jdid
    public static void sendAptToServer(Appointment appointment,MapsActivity ac) {
        TaskSendApt task = new TaskSendApt(appointment,ac);
        task.execute();
    }

    public static class TaskSendApt extends AsyncTask {

        Appointment apt;
        private ProgressDialog progressDialog;
        MapsActivity ac;

        public TaskSendApt(Appointment apt,MapsActivity ac) {
            super();
            this.apt = apt;
            this.ac=ac;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            Log.i("send Apt", "send appointment to server");
            Log.i("send Apt", "send" + MyApplication.currentGroup.getAppointment().getLocation().getLatitude());
            String jsonFileUrl = getFromUrl(AllUrls.CREATE_RDV +
                    "/" + MyApplication.currentGroup.getName() + "/" +
                    MyApplication.currentGroup.getAppointment().getLocation().getLatitude() +
                    "/" + MyApplication.currentGroup.getAppointment().getLocation().getLongitude() +
                    "/" + MyApplication.currentGroup.getAppointment().getDateSousForme() +
                    "/" + MyApplication.currentGroup.getAppointment().getTemps().getHh() +
                    "/" + MyApplication.currentGroup.getAppointment().getTemps().getMm() +
                    "/0/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ac);
            progressDialog.setMessage("Creating Appointment");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MapsActivity.aptEnCreation = false;
            progressDialog.dismiss();
        }
    }

    public static void sendRemoveApt(String groupName) {
        TaskSendRemoveApt task = new TaskSendRemoveApt(groupName);
        task.execute();
    }

    public static class TaskSendRemoveApt extends AsyncTask {

        String groupName;

        public TaskSendRemoveApt(String groupName) {
            super();
            this.groupName = groupName;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            Log.i("send remove apt", "send remove apt");

            String jsonFileUrl = getFromUrl(AllUrls.REMOVE_RDV + groupName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MapsActivity.aptEnCreation = false;
        }
    }

    public static User getUserFromName(String username) {

        String jsonFileUrl = getFromUrl(AllUrls.GET_USER_INFO + username + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());

        Log.v("getuser: ", AllUrls.GET_USER_INFO + username + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
        Log.v("getuser : ", " " + jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);
            String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
            String lastname = (String) jsonObject.get(Constants.LAST_NAME);
            String userName = (String) jsonObject.get(Constants.USERNAME);
            String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

            User user = new User(firstname, lastname, userName, "", phoneNumber);
            Log.v("getuser: ", user.toString());
            return user;
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            return null;

        }
    }

    public static Object getUserFromUserName(String username) {
        Log.v("getusertask:", username);

        try {
            Log.v("getusertask1:", username);
            return new TaskGetUserFromUsername().execute(username).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class TaskGetUserFromUsername extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            getUserFromName(params[0].toString());
            Log.v("getuser", "inbackground");
            Log.v("getuser", params[0].toString());
            return null;
        }
    }
}




