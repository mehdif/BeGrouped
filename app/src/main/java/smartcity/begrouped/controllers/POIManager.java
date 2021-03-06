package smartcity.begrouped.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import smartcity.begrouped.activity.AddDestinationFragment;
import smartcity.begrouped.activity.GroupHomeFragment;
import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.model.Date;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.model.Temps;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.GlobalMethodes;
import smartcity.begrouped.utils.MyApplication;

/**
 * Created by a on 27/04/2015.
 */
public class POIManager {


    private static LinkedList<POI> searchPOIByName(String searchName) {
        try {

            JSONArray poiList;
            LinkedList<POI> listOfPOI = new LinkedList<POI>();
            String chaine = null;
            chaine = URLEncoder.encode(searchName, "utf-8").replace("+", "%20");
            String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.SEARCH_POI_BY_NAME + chaine + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
            Log.v("Jsonfile : ", " " + jsonFileUrl);

            //Json file parser
            if (jsonFileUrl != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonFileUrl);

                    // Getting JSON Array node
                    poiList = jsonObj.getJSONArray(Constants.POINTS);

                    // looping through All members
                    for (int i = 0; i < poiList.length(); i++) {
                        JSONObject jsonObject = poiList.getJSONObject(i);

                        String type = (String) jsonObject.get(Constants.TYPE);
                        String typeDetail = (String) jsonObject.get(Constants.TYPE_DETAIL);
                        String name = (String) jsonObject.get(Constants.NAME);
                        String latitude = (String) jsonObject.get(Constants.LATITUDE);
                        String longitude = (String) jsonObject.get(Constants.LONGITUDE);

                        String address = (String) jsonObject.get(Constants.ADDRESS);
                        String phone = (String) jsonObject.get(Constants.PHONE);
                        String email = (String) jsonObject.get(Constants.EMAIL);
                        String website = (String) jsonObject.get(Constants.WEBSITE);
                        String poiId = (String) jsonObject.get(Constants.ID);
                        listOfPOI.add(new POI(Integer.parseInt(poiId), type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                    }
                    return listOfPOI;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        } catch (UnsupportedEncodingException e) {
        }

        return null;
    }

    public static void searchPOIByNameByTask(String searchName, AddDestinationFragment fr) {
        TaskSearchPOIByName task = new TaskSearchPOIByName(searchName, fr);
        task.execute();
    }

    private static class TaskSearchPOIByName extends AsyncTask {

        String searchName;
        AddDestinationFragment fr;
        private ProgressDialog progressDialog;

        public TaskSearchPOIByName(String searchName, AddDestinationFragment fr) {
            this.searchName = searchName;
            this.fr = fr;
        }

        @Override
        protected LinkedList<POI> doInBackground(Object[] params) {
            return searchPOIByName(searchName);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(fr.getActivity());
            progressDialog.setMessage("Downloading");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            fr.afficherResultat((LinkedList<POI>) o);
        }
    }


    private static LinkedList<POI> getNearestPoint(double alatitude, double alongitude) {
        JSONArray poiList;
        LinkedList<POI> listOfPOI = new LinkedList<POI>();
        //Log.v("aymen", AllUrls.GET_NEAREST_POI+alatitude+"/"+alongitude+"/" + "ba_belfodil"+"/"+"4a7d1ed414474e4033ac29ccb8653d9b");
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_NEAREST_POI + alatitude + "/" + alongitude + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
        //Log.v("aymen", " " +jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList = jsonObj.getJSONArray(Constants.POINTS);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);

                    String type = (String) jsonObject.get(Constants.TYPE);
                    String typeDetail = (String) jsonObject.get(Constants.TYPE_DETAIL);
                    String name = (String) jsonObject.get(Constants.NAME);
                    String latitude = (String) jsonObject.get(Constants.LATITUDE);
                    String longitude = (String) jsonObject.get(Constants.LONGITUDE);

                    String address = (String) jsonObject.get(Constants.ADDRESS);
                    String phone = (String) jsonObject.get(Constants.PHONE);
                    String email = (String) jsonObject.get(Constants.EMAIL);
                    String website = (String) jsonObject.get(Constants.WEBSITE);
                    String poiId = (String) jsonObject.get(Constants.ID);
                    listOfPOI.add(new POI(Integer.parseInt(poiId), type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                }
                return listOfPOI;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    public static void getNearestPoiByTask(double latitude, double longitude, ReceiverNearestPoint receiver) {
        TaskGetNearestPoi task = new TaskGetNearestPoi(latitude, longitude, receiver);
        task.execute();

    }

    private static class TaskGetNearestPoi extends AsyncTask {

        double latitude;
        double longitude;
        ReceiverNearestPoint receiver;

        public TaskGetNearestPoi(double latitude, double longitude, ReceiverNearestPoint receiver) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.receiver = receiver;
        }


        @Override
        protected LinkedList<POI> doInBackground(Object[] params) {
            return getNearestPoint(latitude, longitude);
        }

        @Override
        protected void onPostExecute(Object o) {
            receiver.continuerTraitement((LinkedList<POI>) o);
        }
    }


    public static LinkedList<POI> getDayProgramOfGroup(Date date, String groupName) {

        JSONArray poiList;
        Log.i("entree2", "entree2");
        LinkedList<POI> listOfPOI = new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM + groupName + "/" + date.toString() + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList = jsonObj.getJSONArray(Constants.PROGRAM);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);
                    String dateSTR = (String) jsonObject.get(Constants.DATE);
                    String tempsSTR = (String) jsonObject.get(Constants.TEMPS);
                    String poiId = (String) jsonObject.get(Constants.POI_ID);
                    POI poi = getPOIById(Integer.parseInt(poiId));
                    poi.setDateOfVisite(Date.dateFromLittleString(dateSTR));
                    poi.setTempsOfVisite(Temps.tempsFromLittleString(tempsSTR));
                    listOfPOI.add(poi);
                }
                return listOfPOI;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    public static void getDayProgramOfGroupByTask(Date date, String groupName, Activity ac) {
        TaskGetDayProgramOfGroup task = new TaskGetDayProgramOfGroup(date, groupName, ac);
        task.execute();
    }

    public static class TaskGetDayProgramOfGroup extends AsyncTask {

        String groupName;
        Date date;
        Activity ac;
        private ProgressDialog progressDialog;

        public TaskGetDayProgramOfGroup(Date date, String groupName, Activity ac) {
            this.groupName = groupName;
            this.date = date;
            this.ac = ac;

        }

        @Override
        protected LinkedList<POI> doInBackground(Object[] params) {
            return getDayProgramOfGroup(date, groupName);
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            if (ac instanceof MapsActivity) {
                ((MapsActivity) ac).afficherLeProgramme((LinkedList<POI>) o);
            } else if (ac instanceof GroupHomeFragment) {

                ((GroupHomeFragment) ac).loadSchedule((LinkedList<POI>) o);
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ac);
            progressDialog.setMessage("Downloading");
            progressDialog.show();
        }
    }

    private static POI getPOIById(int poiId) {
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_POI_BY_ID + poiId + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());


        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);
            String type = (String) jsonObject.get(Constants.TYPE);
            String typeDetail = (String) jsonObject.get(Constants.TYPE_DETAIL);
            String name = (String) jsonObject.get(Constants.NAME);
            String latitude = (String) jsonObject.get(Constants.LATITUDE);
            String longitude = (String) jsonObject.get(Constants.LONGITUDE);
            String address = (String) jsonObject.get(Constants.ADDRESS);
            String phone = (String) jsonObject.get(Constants.PHONE);
            String email = (String) jsonObject.get(Constants.EMAIL);
            String website = (String) jsonObject.get(Constants.WEBSITE);

            return new POI(poiId, type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude)));

        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            return null;
        }
    }


    private static LinkedList<POI> getProgramOfGroup(String groupName) {

        JSONArray poiList;
        LinkedList<POI> listOfPOI = new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM + groupName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList = jsonObj.getJSONArray(Constants.PROGRAM);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);
                    String dateSTR = (String) jsonObject.get(Constants.DATE);
                    String tempsSTR = (String) jsonObject.get(Constants.TEMPS);
                    String poiId = (String) jsonObject.get(Constants.POI_ID);
                    POI poi = getPOIById(Integer.parseInt(poiId));
                    poi.setDateOfVisite(Date.dateFromLittleString(dateSTR));
                    poi.setTempsOfVisite(Temps.tempsFromLittleString(tempsSTR));
                    listOfPOI.add(poi);
                }
                return listOfPOI;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    public static LinkedList<POI> getProgramOfGroupByTask(String groupName) {
        TaskGetProgramOfGroup task = new TaskGetProgramOfGroup(groupName);
        try {
            return (LinkedList<POI>) task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TaskGetProgramOfGroup extends AsyncTask {

        String groupName;


        public TaskGetProgramOfGroup(String groupName) {
            this.groupName = groupName;
        }

        @Override
        protected LinkedList<POI> doInBackground(Object[] params) {
            return getProgramOfGroup(groupName);
        }

    }


    private static void initDayGroupProgram(Date date, String groupName) {
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.DELETE_PROGRAM + groupName + "/" + date.toString() + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
    }

    public static void initDayGroupProgramByTask(Date date, String groupName) {
        TaskInitDayGroupProgram task = new TaskInitDayGroupProgram(date, groupName);
        task.execute();
    }

    private static class TaskInitDayGroupProgram extends AsyncTask {

        String groupName;
        Date date;

        public TaskInitDayGroupProgram(Date date, String groupName) {
            this.groupName = groupName;
            this.date = date;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            initDayGroupProgram(date, groupName);
            return null;
        }
    }


    private static void saveDayGroupProgram(Date date, String groupName, LinkedList<POI> dayProgram) {
        for (int i = 0; i < dayProgram.size(); i++) {
            GlobalMethodes.getFromUrl(AllUrls.ADD_LIGNE_PROGRAM + dayProgram.get(i).getPoiId() + "/" + groupName + "/" + date.toString() + "/" + dayProgram.get(i).getTempsOfVisite().toString() + ":00" + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
        }
    }

    public static void saveDayGroupProgramByTask(Date date, String groupName, LinkedList<POI> dayProgram) {
        TasksaveDayGroupProgram task = new TasksaveDayGroupProgram(date, groupName, dayProgram);
        task.execute();
    }

    private static class TasksaveDayGroupProgram extends AsyncTask {

        Date date;
        String groupName;
        LinkedList<POI> dayProgram;

        public TasksaveDayGroupProgram(Date date, String groupName, LinkedList<POI> dayProgram) {
            this.date = date;
            this.groupName = groupName;
            this.dayProgram = dayProgram;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            saveDayGroupProgram(date, groupName, dayProgram);
            return null;
        }
    }


    public static LinkedList<POI> sortPOIByTime(LinkedList<POI> poiList) {
        if (poiList == null) return null;
        LinkedList<POI> sortedPOI = new LinkedList<POI>();
        int earLiestPOI = 0;
        while (poiList.size() > 0) {
            earLiestPOI = 0;
            for (int i = 0; i < poiList.size(); i++) {
                if (poiList.get(earLiestPOI).getTempsOfVisite().compareTo(poiList.get(i).getTempsOfVisite()) > 0)
                    earLiestPOI = i;
            }
            sortedPOI.add(poiList.get(earLiestPOI));
            poiList.remove(earLiestPOI);
        }
        return sortedPOI;
    }

    public static LinkedList<POI> getSortedPOIOfDay(LinkedList<POI> poiList, Date date) {
        LinkedList<POI> poisOfDay = new LinkedList<POI>();
        for (int i = 0; i < poiList.size(); i++) {
            if (date.equals(poiList.get(i).getDateOfVisite())) {
                poisOfDay.add(poiList.get(i));
            }
        }
        return sortPOIByTime(poisOfDay);
    }

    public static void addPoiToSortedList(LinkedList<POI> sortedPOIS, POI poi) {
        int i = 0;
        for (i = 0; i < sortedPOIS.size(); i++) {
            if (poi.getTempsOfVisite().compareTo(sortedPOIS.get(i).getTempsOfVisite()) < 0) break;
        }
        sortedPOIS.add(i, poi);
    }
}
