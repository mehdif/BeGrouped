package smartcity.begrouped.controllers;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

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




    public static LinkedList<POI> searchPOIByName(String searchName){
        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.SEARCH_POI_BY_NAME+searchName+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", jsonFileUrl);

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
                    String poiId= (String) jsonObject.get(Constants.POI_ID);
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


    public static LinkedList<POI> getDayProgramOfGroup(Date date,String groupName){

        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM+groupName+"/"+date.toString()+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.PROGRAM);

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
                    String dateSTR=(String) jsonObject.get(Constants.DATE);
                    String tempsSTR=(String) jsonObject.get(Constants.TEMPS);
                    String poiId= (String) jsonObject.get(Constants.POI_ID);
                    POI poi=new POI(Integer.parseInt(poiId),type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude)));
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
    public static LinkedList<POI> getProgramOfGroup(String groupName){

        JSONArray poiList;
        LinkedList<POI> listOfPOI=new LinkedList<POI>();
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.GET_PROGRAM+groupName+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Jsonfile : ", jsonFileUrl);

        //Json file parser
        if (jsonFileUrl != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonFileUrl);

                // Getting JSON Array node
                poiList= jsonObj.getJSONArray(Constants.PROGRAM);

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
                    String dateSTR=(String) jsonObject.get(Constants.DATE);
                    String tempsSTR=(String) jsonObject.get(Constants.TEMPS);
                    String poiId= (String) jsonObject.get(Constants.POI_ID);
                    POI poi=new POI(Integer.parseInt(poiId),type, typeDetail, name, address, phone, email, website, new Location(Double.parseDouble(latitude), Double.parseDouble(longitude)));
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
    public static void initDayGroupProgram(Date date,String groupName){
        String jsonFileUrl = GlobalMethodes.getFromUrl(AllUrls.DELETE_PROGRAM+groupName+"/"+date.toString()+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
    }
    public static void saveDayGroupProgram(Date date, String groupName, LinkedList<POI> dayProgram){
        for (int i=0;i<dayProgram.size();i++){
            GlobalMethodes.getFromUrl(AllUrls.ADD_LIGNE_PROGRAM+groupName+"/" + MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        }
    }






}
