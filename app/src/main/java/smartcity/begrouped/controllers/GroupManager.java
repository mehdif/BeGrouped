package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import smartcity.begrouped.activity.CreateGroupFragment;
import smartcity.begrouped.activity.HomeFragment;
import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.activity.MembersOnGroupFragment;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.getFromUrl;

/**
 * Created by a on 27/04/2015.
 */
public class GroupManager {


    private static Group parseGroup(JSONObject jsonGroup) {
        Group group = null;
        try {
            String groupName = (String) jsonGroup.get(Constants.GROUP_NAME);
            String regionName = (String) jsonGroup.get(Constants.REGION_NAME);
            String expirationDate = (String) jsonGroup.get(Constants.EXPIRATION_DATE);
            String supervisorName = (String) jsonGroup.get(Constants.SUPERVISOR_NAME);
            User supervisor = new User("", "", supervisorName, "", "");
            group = new Group(supervisor, groupName, regionName, expirationDate);
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
        }
        return group;
    }

    public static Group parseGroup(String jsonFile) {

        Group group = null;
        try {
            JSONObject jsonGroup = new JSONObject(jsonFile);
            String groupName = (String) jsonGroup.get(Constants.GROUP_NAME);
            String regionName = (String) jsonGroup.get(Constants.REGION_NAME);
            String expirationDate = (String) jsonGroup.get(Constants.EXPIRATION_DATE);
            String supervisorName = (String) jsonGroup.get(Constants.SUPERVISOR_NAME);
            User supervisor = new User("", "", supervisorName, "", "");
            group = new Group(supervisor, groupName, regionName, expirationDate);
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
        }
        return group;
    }

    private static LinkedList<User> parseGroupMembers(JSONArray jsonArrayMembers) {

        LinkedList<User> membersList = new LinkedList<>();

        //Json file parser
        try {
            // looping through All members
            for (int i = 0; i < jsonArrayMembers.length(); i++) {
                JSONObject jsonObject = jsonArrayMembers.getJSONObject(i);
                String firstname = (String) jsonObject.get(Constants.FIRST_NAME);
                String lastname = (String) jsonObject.get(Constants.LAST_NAME);
                String username = (String) jsonObject.get(Constants.USERNAME);
                String phoneNumber = (String) jsonObject.get(Constants.PHONE_NUMBER);

                membersList.add(new User(firstname, lastname, username, null, phoneNumber));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return membersList;
    }


    public static Group parseGroupAllInfo(String json) {
        Group group = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonAllInfo = jsonObject.getJSONObject(Constants.GROUP_ALL_INFO);
            JSONObject jsonGroup = jsonAllInfo.getJSONObject(Constants.GROUP_INFO);
            JSONObject jsonSupervisor = jsonAllInfo.getJSONObject(Constants.SUPERVISER_INFO);
            JSONArray members = jsonAllInfo.getJSONArray(Constants.MEMBERS_INFO);

            group = parseGroup(jsonGroup);
            group.setSupervisor(UserManager.parseUser(jsonSupervisor));
            group.setMembers(parseGroupMembers(members));

            MyApplication.currentGroup = group;
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
        }
        return group;
    }

    public static LinkedList<User> parsePendingDemands(String json) {
        LinkedList<User> waitingMembers = new LinkedList<>();
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray members = jsonObj.getJSONArray(Constants.PENDING_DEMANDS);
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
        return waitingMembers;
    }

    public static LinkedList<User> parseGroupMembers(String json) {
        LinkedList<User> membersList = new LinkedList<>();
        JSONArray members;
        try {
            JSONObject jsonObj = new JSONObject(json);
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
        return membersList;
    }

    /**
     * Method that request the server and updates the locations of the group members
     *
     * @param group
     */
    public static Group updateGroupUserLocations(Group group) {
        try {

            JSONArray members;
            LinkedList<User> membersList = group.getMembers();
            String encodedName = URLEncoder.encode(group.getName(), "utf-8").replace("+", "%20");

            String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_POSITION + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
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
                        if (user != null) {
                            addLocationToUser(user, Double.parseDouble(longitude), Double.parseDouble(latitude));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return group;
    }

    /**
     * Method that get the user object from the username
     *
     * @param members  : list of the members of the group
     * @param username : username of the user to compare with
     * @return
     */
    public static User findUserByUsername(LinkedList<User> members, String username) {

        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getUsername().equals(username)) {
                return members.get(i);
            }
        }
        return null;
    }

    public static void addLocationToUser(User user, Double longitude, Double latitude) {
        user.setLocalisation(new Location(latitude, longitude));
    }



    public static LinkedList<Group> getMyGroups() {

        LinkedList<Group> groupsList = new LinkedList<>();

        JSONArray groups;

        String jsonFileUrl = getFromUrl(AllUrls.GET_GROUP_INFORMATIONS + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());

        Log.v("group", AllUrls.GET_GROUP_INFORMATIONS + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
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

                    User user = new User("", "", supervisorName, "", "");
                    Group group = new Group(user, groupName, regionName);
                    groupsList.add(group);
                    Log.v("getgroupSuccess:", group.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return groupsList;
    }



    public static boolean deleteGroup(String groupName) {
        Log.v("TAG", groupName);
        String jsonFileUrl = getFromUrl(AllUrls.DELETE_GROUP + groupName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());

        Log.v("Json delete group : ", " " + jsonFileUrl);

        if (jsonFileUrl != null && jsonFileUrl.equals("FAILED")) {
            return false;
        } else if (jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")) {
            return true;
        }
        return false;
    }

    public static boolean leaveGroup(String groupName) {
        try {
            String encodedName = URLEncoder.encode(groupName, "utf-8").replace("+", "%20");
            String jsonFileUrl = getFromUrl(AllUrls.SORTIR_GROUP_USER + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());

            Log.v("Json leave group : ", " " + jsonFileUrl);

            if (jsonFileUrl != null && jsonFileUrl.equals("FAILED")) {
                return false;
            } else if (jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void getGroups(HomeFragment fragment) {
        new TaskGetMyGroups(fragment).execute();
    }


    public static Boolean acceptMember(String groupName, String memberUsername) {
        String encodedName = null;
        try {
            encodedName = URLEncoder.encode(groupName, "utf-8").replace("+", "%20");

            String jsonFileUrl = getFromUrl(AllUrls.ACCEPT_MEMBER_TO_GROUP + encodedName + "/" + "/" + memberUsername + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());

            Log.v("Json accept demand : ", jsonFileUrl);

            if (jsonFileUrl != null && jsonFileUrl.equals("SUCCEED")) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static class TaskAcceptMember extends AsyncTask {

        String groupName;
        String memberUsername;

        public TaskAcceptMember(String groupName, String memberUsername) {
            this.groupName = groupName;
            this.memberUsername = memberUsername;
        }

        @Override
        protected Boolean doInBackground(Object[] params) {
            return acceptMember(groupName, memberUsername);
        }

    }



    public static void callTaskUpdateGroupMemberLocations(Group group,ReceiverUpdatePositions rec) {
        TaskGetMemberPositions task = new TaskGetMemberPositions(group,rec);

        task.execute();

    }



    public static boolean callTaskDeleteGroup(String groupName) {

        try {
            return (Boolean) new TaskDeleteGroup(groupName).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean callTaskLeaveGroup(String groupName) {

        try {
            return (Boolean) new TaskLeaveGroup().execute(groupName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static class TaskGetMemberPositions extends AsyncTask {

        Group group;
        ReceiverUpdatePositions rec;

        public TaskGetMemberPositions(Group group,ReceiverUpdatePositions rec) {
            this.rec=rec;
            this.group = group;
        }

        @Override
        protected Group doInBackground(Object[] params) {
            return updateGroupUserLocations(group);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            rec.rappelerBroadCast();
            if (MapsActivity.markerManager != null) {
                MapsActivity.markerManager.updateMarkerPositions();
            }
        }

    }

    public static class TaskGetMyGroups extends AsyncTask {

        HomeFragment fragment;

        public TaskGetMyGroups(HomeFragment fragment) {
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



    public static class TaskDeleteGroup extends AsyncTask {

        String groupName;

        public TaskDeleteGroup(String groupName) {
            this.groupName = groupName;

        }

        @Override
        protected Boolean doInBackground(Object[] params) {

            return deleteGroup(groupName);

        }
    }


    public static class TaskLeaveGroup extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] params) {

            return leaveGroup(params[0].toString());

        }
    }

    public static User getUserByUsernameFromListAndRemove(String username, LinkedList<User> listUser){
        if (username==null) return null;
        if (listUser==null) return null;
        User usr;
        int i=0;
        while (i<listUser.size()){
            if (listUser.get(i).getUsername().equals(username)){
                usr=listUser.get(i);
                listUser.remove(i);
                return usr;
            }
            i++;
        }
        return null;
    }


}
