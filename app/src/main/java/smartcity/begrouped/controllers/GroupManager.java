package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.MyApplication;

/**
 * Created by a on 27/04/2015.
 */
public class GroupManager {




    static InputStream is = null;
    static String chaine = "";


    public static Group getGroupByName(String groupName){
        return null;
    }

    //updated
    public static LinkedList<Group> getGroupsOfUser(String username){
        return null;
    }


    /**
     * Method that request the server and updates the locations of the group members
     * @param group
     */
    public static Group updateGroupUserLocations(Group group){

        JSONArray members;
        LinkedList<User> membersList = group.getMembers();

        String jsonFileUrl = getFromUrl("http://smartpld-001-site1.smarterasp.net/index.php/position_controller/getGroupPosition/algertour/"+ MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                members = jsonObj.getJSONArray(Constants.POSITIONS);

                // looping through All members
                for (int i = 0; i < members.length(); i++) {
                    JSONObject jsonObject = members.getJSONObject(i);

                    String username = (String) jsonObject.get(Constants.USERNAME);
                    String longitude = (String) jsonObject.get(Constants.LONGITUDE);
                    String latitude = (String) jsonObject.get(Constants.LATITUDE);

                    User user = findUserByUsername(membersList, username);
                    if(user != null){
                        addLocationToUser(user, Double.parseDouble(longitude) , Double.parseDouble(latitude));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return group;
    }

    /**
     * Method that get the user object from the username
     * @param members : list of the members of the group
     * @param username : username of the user to compare with
     * @return
     */
    public static User findUserByUsername(LinkedList<User> members, String username){

        for (int i = 0; i < members.size(); i++) {
            if(members.get(i).getUsername().equals(username)){
                return members.get(i);
            }
        }
        return null;
    }

    public static void addLocationToUser(User user, Double longitude, Double latitude){
        user.setLocalisation(new Location(latitude, longitude));
    }
    public static void updateGroupUserLocationsForTest(Group group){
        /*for (int i=0;i<group.getMembers().size();i++){
            group.getMembers().get(i).getLocalisation().setLatitude(group.getMembers().get(i).getLocalisation().getLatitude()+0.01);
            group.getMembers().get(i).getLocalisation().setLongitude(group.getMembers().get(i).getLocalisation().getLongitude() + 0.01);
        }*/
    }
/*
    public static Group createGroupForTest(){
        User supervsr=null;
        double f;
        LinkedList<User> listOfUsers=new LinkedList<User>();
        for (int i=0;i<5;i++){
            f=i;
            User usr=new User("usr "+i,"usr "+i,"usr "+i,"usr "+i,"usr "+i);
            usr.setLocalisation(new Location(46+f*0.1,7+f*0.1));
            listOfUsers.add(usr);
            if (i==2) supervsr=usr;
        }
        listOfUsers.add(UserManager.myIdentity);
        return new Group(supervsr,listOfUsers,null,"testGroup","lyon");

    }
*/
    /**
     * Method that request the server with URL and gets a Json string in response
     * @param url
     * @return
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

    public static Group getGroupInformations(Group group){

        String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_INFORMATIONS + group.getName() +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json group info : ", jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);

            String groupName = (String) jsonObject.get(Constants.GROUP_NAME);
            String supervisorName = (String) jsonObject.get(Constants.SUPERVISOR_NAME);
            String regionName = (String) jsonObject.get(Constants.REGION_NAME);
            String expirationDate = (String) jsonObject.get(Constants.EXPIRATION_DATE);


            group.setLocationName(regionName);
            group.setExpirationDate(expirationDate);
            addSupervisorGroup(group,supervisorName);

            return group;

        }
        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return null;
        }
    }

    /**
     * Method that get all members of a group
     * @return Members list
     */
    public static Group createMembersFromJson(String groupName) {

        LinkedList<User> membersList = new LinkedList<>();

        JSONArray members;

        String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_MEMBERS + groupName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json members group: ", jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                members = jsonObj.getJSONArray(Constants.MEMBERS);

                // looping through All members
                for (int i = 0; i < members.length(); i++) {
                    JSONObject jsonObject = members.getJSONObject(i);

                    String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
                    String lastname = (String) jsonObject.get(Constants.LAST_NAME);
                    String username = (String) jsonObject.get(Constants.USERNAME);
                    String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

                    membersList.add(new User(firstname, lastname, username, null, phoneNumber));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        Group group=new Group(null,membersList,null,"AlgerTour",null);

        return getGroupInformations(group);
    }


    /**
     * Method that adds the supervisor to a group
     * @param group
     * @param supervisorName
     * @return
     */
    public static Group addSupervisorGroup(Group group, String supervisorName){
        for(int i = 0; i < group.getMembers().size(); i++){
            if(group.getMembers().get(i).getFirstname().equals(supervisorName)){
                group.setSupervisor(group.getMembers().get(i));
            }
        }
        return group;
    }

    public static Group getGroupMembersFromName(String groupName){
        try {
            return ((Group)new TaskGetJsonMembers(groupName).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class TaskGetJsonMembers extends AsyncTask {

        String groupName;
        public  TaskGetJsonMembers(String groupName){
            this.groupName = groupName;
        }

        @Override
        protected Group doInBackground(Object[] params) {

            return createMembersFromJson(groupName);

        }
    }



    public static void callTaskUpdateGroupMemberLocations(Group group){
        TaskGetMemberPositions task=new TaskGetMemberPositions(group);

            task.execute();

    }

    public static class TaskGetMemberPositions extends AsyncTask {

        Group group;
        public TaskGetMemberPositions(Group group){
            this.group=group;
        }
        @Override
        protected Group doInBackground(Object[] params) {


            return updateGroupUserLocations(group);

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (MapsActivity.markerManager!=null){
                MapsActivity.markerManager.updateMarkerPositions();
            }
        }
    }


    //new methode
    /*
    public static Group createNewGroup(String name ,String locationName){
        if (UserManager.myIdentity==null) return null;
        Group newGroup=new Group(UserManager.myIdentity,name,locationName);
        // la on envoie une requete au serveur pour l'ajouter a la BDD, ensuite:
        return newGroup;
    }*/
    // new methode
    public static boolean addMeToThisGroup(String groupName){
        return false;
    }


}
