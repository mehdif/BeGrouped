package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


/**
 * Created by Anes on 28/04/2015.
 */
public class MarkerManager {
    private GoogleMap mMap;
    private Group group;
    private boolean firstTime=true;
    public MarkerManager(GoogleMap mMap, Group group) {
        this.mMap = mMap;
        this.group = group;
    }
    public void updateLocations(){
        GroupManager.callTaskUpdateGroupMemberLocations(group);
      //  requestForApt();
    }



    public void updateMarkerPositions(){
        try {
            for (int i = 0; i < group.getMembers().size(); i++) {
                User user = group.getMembers().get(i);
                if (user.getMarker() != null)
                    user.getMarker().setPosition(new LatLng(user.getLocalisation().getLatitude(), user.getLocalisation().getLongitude()));
                else
                    user.setMarker(mMap.addMarker(new MarkerOptions().position(new LatLng(user.getLocalisation().getLatitude(), user.getLocalisation().getLongitude()))
                            .title(user.getFirstname() + " " + user.getLastname()).snippet("member")));

                if (user.getUsername().equals(MyApplication.myIdentity.getUsername())) {
                    user.getMarker().setSnippet("Me");
                    user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    MyApplication.myIdentity.setLocalisation(new Location(user.getMarker().getPosition().latitude, user.getMarker().getPosition().longitude));
                    if (firstTime)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user.getMarker().getPosition(), 12));
                    firstTime = false;
                }
            /*
            if (user.getUsername().equals(group.getSupervisor().getUsername())){
                user.getMarker().setSnippet("Supervisor");
                user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }*/
            }
        } catch(Exception e){
            Log.e("exception", e.getMessage());
        }

    }




    public class AsTaskUpdatePositions extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            GroupManager.updateGroupUserLocationsForTest(group);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateMarkerPositions();
        }
    }


/*
    private void requestForApt() {
        new AsTaskRequestForApt().execute();
    }

    public Appointment getAptOfMyGroup() {
        return aptOfMyGroup;
    }
    public class AsTaskRequestForApt extends AsyncTask<String, Void, String> {

        Appointment appoint;
        @Override
        protected String doInBackground(String... params) {
            appoint=getAptOfMyGroup();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (appoint!=null){

            }
        }
    }*/
}
