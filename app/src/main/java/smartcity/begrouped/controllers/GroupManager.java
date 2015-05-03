package smartcity.begrouped.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import smartcity.begrouped.activity.AuthentificationActivity;
import smartcity.begrouped.activity.CreateGroupFragment;
import smartcity.begrouped.activity.HomeFragment;
import smartcity.begrouped.activity.JoinGroupFragment;
import smartcity.begrouped.activity.ManageGroupFragment;
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
        Log.v("Jsonfile : ", " " + jsonFileUrl);

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

    public static Group getGroupInformation(Group group){

        String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_INFORMATIONS + group.getName() +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json group info : ", " " + jsonFileUrl);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);

            String groupName = (String) jsonObject.get(Constants.GROUP_NAME);
            String supervisorName = (String) jsonObject.get(Constants.SUPERVISOR_NAME);
            String regionName = (String) jsonObject.get(Constants.REGION_NAME);
            String expirationDate = (String) jsonObject.get(Constants.EXPIRATION_DATE);


            group.setSupervisor(new User("","",supervisorName,"",""));
            group.setLocationName(regionName);
            group.setExpirationDate(expirationDate);
            //addSupervisorGroup(group,supervisorName);

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

        Log.v("Json members group: ", " " + jsonFileUrl);

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

        Group group=new Group(null,membersList,null,null,null);

        return group;
       // return //getGroupInformation(group);
    }

    public static LinkedList<Group> getMyGroups() {

        LinkedList<Group> groupsList = new LinkedList<>();

        JSONArray groups;

        String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_INFORMATIONS+ MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("group",AllUrls.GET_GROUP_INFORMATIONS+ MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("group : ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                groups = jsonObj.getJSONArray(Constants.GROUPS);

                // looping through All members
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject jsonObject = groups.getJSONObject(i);

                    String groupName = (String) jsonObject.get("groupename");
                    String regionName = (String) jsonObject.get("regionname");
                    String supervisorName = (String) jsonObject.get("supervisorname");

                    User user=new User("","",supervisorName,"","");
                    Group group=new Group(user,groupName,regionName);
                    groupsList.add(group);
                    Log.v("getgroupSuccess:",group.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return groupsList;
    }


    public static Group createNewGroup(String name ,String locationName){


        String jsonFileUrl = getFromUrl(AllUrls.CREATE_GROUP + name +"/" + locationName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json create group : ", " " + jsonFileUrl);

        if(jsonFileUrl != null && jsonFileUrl.equals("GROUP_EXIST_BEFORE")){
            return null;
        }
        else{

            //Json file parser
            try {

                JSONObject jsonObject = new JSONObject(jsonFileUrl);

                String groupName = (String) jsonObject.get(Constants.GROUP_NAME);
                String supervisorName = (String) jsonObject.get(Constants.SUPERVISOR_NAME);
                String regionName = (String) jsonObject.get(Constants.REGION_NAME);
                String expirationDate = (String) jsonObject.get(Constants.EXPIRATION_DATE);

                return new Group(MyApplication.myIdentity, groupName,regionName);
            }
            catch(Exception e){
                Log.e("Error : ", e.getMessage());
                return null;
            }
        }
    }

    /**
     * Method that adds the supervisor to a group
     * @param group
     * @param supervisorName
     * @return
     */
    public static Group addSupervisorGroup(Group group, String supervisorName){
        for(int i = 0; i < group.getMembers().size(); i++){
            if(group.getMembers().get(i).getUsername().equals(supervisorName)){
                group.setSupervisor(group.getMembers().get(i));
            }
        }
        return group;
    }

    public static boolean deleteGroup(String groupName){
        Log.v("TAG",groupName);
        String jsonFileUrl = getFromUrl(AllUrls.DELETE_GROUP + groupName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json delete group : ", " " + jsonFileUrl);

        if(jsonFileUrl != null && jsonFileUrl.equals("FAILED")){
            return false;
        }
        else if(jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")){
            return true;
        }
        return false;
    }

    public static LinkedList<User> getPendingDemands(String groupName){


        LinkedList<User> waitingMembers = new LinkedList<>();

        JSONArray members;

        String jsonFileUrl = getFromUrl(AllUrls.GET_PENDING_DEMANDS + groupName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json pending demands: ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                members = jsonObj.getJSONArray(Constants.PENDING_DEMANDS);

                // looping through All members
                for (int i = 0; i < members.length(); i++) {
                    JSONObject jsonObject = members.getJSONObject(i);

                    String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
                    String lastname = (String) jsonObject.get(Constants.LAST_NAME);
                    String username = (String) jsonObject.get(Constants.USERNAME);
                    String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

                    waitingMembers.add(new User(firstname, lastname, username, null, phoneNumber));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return waitingMembers;

    }

      public static boolean expulseMember(String groupName,String userName)
      {
          String jsonFileUrl = getFromUrl(AllUrls.EXPULSER_GROUP_SUPERVISOR +groupName+"/"+ userName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

          Log.v("Json delete member : ", " " + jsonFileUrl);

          if(jsonFileUrl != null && jsonFileUrl.equals("FAILED")){
              return false;
          }
          else if(jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")){
              return true;
          }
          return false;
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

    public static Group callTaskGetGroupInformation(Group group)
    {
        try {
            return (Group)new TaskGroupInformation().execute(group).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static void getGroups(HomeFragment fragment)
    {
        new TaskGetMyGroups(fragment).execute();
    }

    public static Boolean joinGroup(String groupName){

        String jsonFileUrl = getFromUrl(AllUrls.ASK_JOIN_GROUP + groupName +"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json join group : ", " " + jsonFileUrl);

        if(jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")){
            return true;
        }
        return false;
    }

    public static Boolean acceptMember(String groupName, String memberUsername){

        String jsonFileUrl = getFromUrl(AllUrls.ACCEPT_MEMBER_TO_GROUP + groupName +"/" +"/" + memberUsername + "/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());

        Log.v("Json accept demand : ", jsonFileUrl);

        if(jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")){
            return true;
        }
        return false;
    }

    public static class TaskAcceptMember extends AsyncTask {

        String groupName;
        String memberUsername;

        public TaskAcceptMember(String groupName, String memberUsername){
            this.groupName = groupName;
            this.memberUsername = memberUsername;
        }
        @Override
        protected Boolean doInBackground(Object[] params)
        {
            return acceptMember(groupName, memberUsername);
        }

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

    public static class TaskGetPendingDemands extends AsyncTask {

        String groupName;
        public  TaskGetPendingDemands(String groupName){
            this.groupName = groupName;
        }

        @Override
        protected LinkedList<User> doInBackground(Object[] params) {

            return getPendingDemands(groupName);

        }
    }


    public static void callTaskUpdateGroupMemberLocations(Group group){
        TaskGetMemberPositions task=new TaskGetMemberPositions(group);

            task.execute();

    }

    public static Boolean callTaskJoinGroup(String groupName, JoinGroupFragment fragment){

        try {
            return  (Boolean) new TaskJoinGroup(groupName, fragment).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static Boolean callTaskAcceptMember(String groupName, String memberUsername){

        try {
            return  (Boolean) new TaskAcceptMember(groupName, memberUsername).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean callTaskDeleteGroup(String groupName){

        try {
            return  (Boolean) new TaskDeleteGroup(groupName).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean callTaskDeleteMember(String groupName,String userName){

        try {
            return  (Boolean) new TaskDeleteMember().execute(groupName,userName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void callTaskCreateNewGroup(String name, String locationName, CreateGroupFragment fragment){


        new TaskCreateNewGroup(name, locationName, fragment).execute();

    }

    public static LinkedList<User> callTaskGetPendingDemands(String groupName){

        try {
            return (LinkedList<User>) new TaskGetPendingDemands(groupName).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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

    public static class TaskGetMyGroups extends AsyncTask {

        HomeFragment fragment;

       public TaskGetMyGroups(HomeFragment fragment){
           this.fragment = fragment;
       }


        @Override
        protected LinkedList<Group> doInBackground(Object[] params) {
            return getMyGroups();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fragment.getGroup((LinkedList<Group>) o);
        }
    }

    public static class TaskJoinGroup extends AsyncTask {

        private String groupName;
        private JoinGroupFragment fragment;

        public TaskJoinGroup(String groupName, JoinGroupFragment fragment){
            this.groupName=groupName;
            this.fragment = fragment;
        }

        @Override
        protected Boolean doInBackground(Object[] params) {
            return joinGroup(groupName);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fragment.joinGroup((Boolean) o);
        }
    }

    public static class TaskCreateNewGroup extends AsyncTask {

        private String name;
        private String locationName;
        private CreateGroupFragment fragment;

        public TaskCreateNewGroup(String name, String locationName, CreateGroupFragment fragment){
            this.name = name;
            this.locationName = locationName;
            this.fragment = fragment;
        }

        @Override
        protected Group doInBackground(Object[] params) {
            return createNewGroup(name, locationName);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fragment.createGroup((Group) o);
        }
    }

    public static class TaskDeleteGroup extends AsyncTask {

       String groupName;

        public TaskDeleteGroup(String groupName){ this.groupName=groupName;

        }

        @Override
        protected Boolean doInBackground(Object[] params) {

            return deleteGroup(groupName);

        }
    }
    public static class TaskDeleteMember extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] params) {

            return  expulseMember(params[0].toString(),params[1].toString());

        }
    }
    public static class TaskGroupInformation extends AsyncTask {

        @Override
        protected Group doInBackground(Object[] params) {

            return getGroupInformation((Group)params[0]);

        }
    }


    // new methode
    public static boolean addMeToThisGroup(String groupName){
        return false;
    }


}
