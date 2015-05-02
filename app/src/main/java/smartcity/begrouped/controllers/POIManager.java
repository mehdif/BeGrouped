package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

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




    private static LinkedList<POI> searchPOIByName(String searchName){
        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.SEARCH_POI_BY_NAME+searchName+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", " " +jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.POINTS);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);

                    String type= (String) jsonObject.get(Constants.TYPE);
                    String typeDetail= (String) jsonObject.get(Constants.TYPE_DETAIL);
                    String name= (String) jsonObject.get(Constants.NAME);
                    String latitude= (String) jsonObject.get(Constants.LATITUDE);
                    String longitude= (String) jsonObject.get(Constants.LONGITUDE);

                    String address= (String) jsonObject.get(Constants.ADDRESS);
                    String phone= (String) jsonObject.get(Constants.PHONE);
                    String email= (String) jsonObject.get(Constants.EMAIL);
                    String website= (String) jsonObject.get(Constants.WEBSITE);
                    String poiId= (String) jsonObject.get(Constants.ID);
                    listOfPOI.add(new POI(Integer.parseInt(poiId),type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude))));
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

    public static LinkedList<POI> searchPOIByNameByTask(String searchName){
        TaskSearchPOIByName task=new TaskSearchPOIByName(searchName);
        try {
                return (LinkedList<POI>) task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static class TaskSearchPOIByName extends AsyncTask {

        String searchName;


        public TaskSearchPOIByName(String searchName){
            this.searchName= searchName;
        }
        @Override
        protected LinkedList<POI> doInBackground(Object[] params)
        {
            return searchPOIByName(searchName);
        }

    }


    private static LinkedList<POI> getNearestPoint(double alatitude,double alongitude){
        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        Log.v("aymen", AllUrls.GET_NEAREST_POI+alatitude+"/"+alongitude+"/" + "ba_belfodil"+"/"+"4a7d1ed414474e4033ac29ccb8653d9b");
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_NEAREST_POI+alatitude+"/"+alongitude+"/" + "ba_belfodil"+"/"+"4a7d1ed414474e4033ac29ccb8653d9b");
        Log.v("aymen", " " +jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.POINTS);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);

                    String type= (String) jsonObject.get(Constants.TYPE);
                    String typeDetail= (String) jsonObject.get(Constants.TYPE_DETAIL);
                    String name= (String) jsonObject.get(Constants.NAME);
                    String latitude= (String) jsonObject.get(Constants.LATITUDE);
                    String longitude= (String) jsonObject.get(Constants.LONGITUDE);

                    String address= (String) jsonObject.get(Constants.ADDRESS);
                    String phone= (String) jsonObject.get(Constants.PHONE);
                    String email= (String) jsonObject.get(Constants.EMAIL);
                    String website= (String) jsonObject.get(Constants.WEBSITE);
                    String poiId= (String) jsonObject.get(Constants.ID);
                    listOfPOI.add(new POI(Integer.parseInt(poiId),type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude))));
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

    public static LinkedList<POI> getNearestPoiByTask(double latitude,double longitude){
        TaskGetNearestPoi task=new TaskGetNearestPoi(latitude,longitude);
        try {
            return (LinkedList<POI>) task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static class TaskGetNearestPoi extends AsyncTask {

        double latitude;
        double longitude;

        public TaskGetNearestPoi(double latitude,double longitude)
        {
            this.latitude= latitude;
            this.longitude= longitude;
        }


        @Override
        protected LinkedList<POI> doInBackground(Object[] params)
        {
            return getNearestPoint(latitude,longitude);
        }

    }











    public static LinkedList<POI> getDayProgramOfGroup(Date date,String groupName){

        JSONArray poiList;
        Log.i("entree2","entree2");
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM+groupName+"/"+date.toString()+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.PROGRAM);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);
                    String dateSTR=(String) jsonObject.get(Constants.DATE);
                    String tempsSTR=(String) jsonObject.get(Constants.TEMPS);
                    String poiId= (String) jsonObject.get(Constants.POI_ID);
                    POI poi=getPOIById(Integer.parseInt(poiId));
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
    public static LinkedList<POI> getDayProgramOfGroupByTask(Date date,String groupName){
        TaskGetDayProgramOfGroup  task=new TaskGetDayProgramOfGroup(date,groupName);
        try {
            return (LinkedList<POI>) task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  static class TaskGetDayProgramOfGroup extends AsyncTask {

        String groupName;
        Date date;

        public TaskGetDayProgramOfGroup (Date date,String groupName){
            this.groupName= groupName;
            this.date=date;
        }
        @Override
        protected LinkedList<POI> doInBackground(Object[] params)
        {
           return getDayProgramOfGroup(date,groupName);
        }

    }

    private static POI getPOIById(int poiId){
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_POI_BY_ID + poiId + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());


        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonFileUrl);
            String type= (String) jsonObject.get(Constants.TYPE);
            String typeDetail= (String) jsonObject.get(Constants.TYPE_DETAIL);
            String name= (String) jsonObject.get(Constants.NAME);
            String latitude= (String) jsonObject.get(Constants.LATITUDE);
            String longitude= (String) jsonObject.get(Constants.LONGITUDE);
            String address= (String) jsonObject.get(Constants.ADDRESS);
            String phone= (String) jsonObject.get(Constants.PHONE);
            String email= (String) jsonObject.get(Constants.EMAIL);
            String website= (String) jsonObject.get(Constants.WEBSITE);

            return  new POI(poiId,type,typeDetail,name,address,phone,email,website,new Location(Double.parseDouble(latitude),Double.parseDouble(longitude)));

        }
        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return null;
        }
    }







    private static LinkedList<POI> getProgramOfGroup(String groupName){

        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM+groupName+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", " " + jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.PROGRAM);

                // looping through All members
                for (int i = 0; i < poiList.length(); i++) {
                    JSONObject jsonObject = poiList.getJSONObject(i);
                    String dateSTR=(String) jsonObject.get(Constants.DATE);
                    String tempsSTR=(String) jsonObject.get(Constants.TEMPS);
                    String poiId= (String) jsonObject.get(Constants.POI_ID);
                    POI poi=getPOIById(Integer.parseInt(poiId));
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
    public static LinkedList<POI> getProgramOfGroupByTask(String groupName){
        TaskGetProgramOfGroup   task=new TaskGetProgramOfGroup (groupName);
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


        public TaskGetProgramOfGroup  (String groupName){
            this.groupName= groupName;
        }
        @Override
        protected LinkedList<POI> doInBackground(Object[] params)
        {
            return getProgramOfGroup(groupName);
        }

    }















    private static void initDayGroupProgram(Date date,String groupName){
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.DELETE_PROGRAM+groupName+"/"+date.toString()+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
    }

    public static void initDayGroupProgramByTask(Date date,String groupName){
        TaskInitDayGroupProgram   task=new TaskInitDayGroupProgram (date,groupName);
        task.execute();
    }
    private static class TaskInitDayGroupProgram extends AsyncTask {

        String groupName;
        Date date;

        public TaskInitDayGroupProgram  (Date date,String groupName){
            this.groupName= groupName;
            this.date=date;
        }
        @Override
        protected Object doInBackground(Object[] params)
        {
            initDayGroupProgram(date,groupName);
            return null;
        }
    }








    private static void saveDayGroupProgram(Date date, String groupName, LinkedList<POI> dayProgram){
        for (int i=0;i<dayProgram.size();i++){
            GlobalMethodes.getFromUrl(AllUrls.ADD_LIGNE_PROGRAM+dayProgram.get(i).getPoiId()+"/"+groupName+"/"+date.toString()+"/"+dayProgram.get(i).getTempsOfVisite().toString()+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        }
    }
    public static void saveDayGroupProgramByTask(Date date, String groupName, LinkedList<POI> dayProgram){
    TasksaveDayGroupProgram   task=new TasksaveDayGroupProgram(date, groupName,dayProgram);
        task.execute();
    }
    private static class TasksaveDayGroupProgram extends AsyncTask {

        Date date;
        String groupName;
        LinkedList<POI> dayProgram;

        public TasksaveDayGroupProgram  (Date date, String groupName, LinkedList<POI> dayProgram){
            this.date=date;
            this.groupName=groupName;
            this.dayProgram=dayProgram;
        }
        @Override
        protected Object doInBackground(Object[] params)
        {
            saveDayGroupProgram(date,groupName,dayProgram);
            return null;
        }
    }













    public static LinkedList<POI> sortPOIByTime(LinkedList<POI> poiList){
        LinkedList<POI> sortedPOI=new LinkedList<POI>();
        int earLiestPOI=0;
        while (poiList.size()>0){
            earLiestPOI=0;
            for (int i=0;i<poiList.size();i++){
                if (poiList.get(earLiestPOI).getTempsOfVisite().compareTo(poiList.get(i).getTempsOfVisite())>0)
                    earLiestPOI=i;
            }
            sortedPOI.add(poiList.get(earLiestPOI));
            poiList.remove(earLiestPOI);
        }
        return sortedPOI;
    }
    public static LinkedList<POI> getSortedPOIOfDay(LinkedList<POI> poiList, Date date){
        LinkedList<POI> poisOfDay=new LinkedList<POI>();
        for (int i=0;i<poiList.size();i++){
            if (date.equals(poiList.get(i).getDateOfVisite())){
                poisOfDay.add(poiList.get(i));
            }
        }
        return sortPOIByTime(poisOfDay);
    }
    public static void addPoiToSortedList(LinkedList<POI> sortedPOIS, POI poi){
        int i=0;
        for (i=0;i<sortedPOIS.size();i++){
            if (poi.getTempsOfVisite().compareTo(sortedPOIS.get(i).getTempsOfVisite())<0) break;
        }
        sortedPOIS.add(i,poi);
    }
}
