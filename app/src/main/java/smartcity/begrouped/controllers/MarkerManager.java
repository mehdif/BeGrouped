package smartcity.begrouped.controllers;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.User;


/**
 * Created by Anes on 28/04/2015.
 */
public class MarkerManager {
    private GoogleMap mMap;
    private Group group;

    public MarkerManager(GoogleMap mMap, Group group) {
        this.mMap = mMap;
        this.group = group;
    }
    public void updateLocations(){
        new AsTaskUpdatePositions().execute("");
    }
    public void updateMarkerPositions(){
        for (int i=0;i<group.getMembers().size();i++){
            User user=group.getMembers().get(i);
            if (user.getMarker()!=null)
            user.getMarker().setPosition(new LatLng(user.getLocalisation().getLatitude(),user.getLocalisation().getLongitude()));
            else
                user.setMarker( mMap.addMarker(new MarkerOptions().position(new LatLng(user.getLocalisation().getLatitude(),user.getLocalisation().getLongitude()))
                        .title(user.getFirstname()+" "+user.getLastname()).snippet("member")));

            if (user.getUsername().equals(UserManager.myIdentity.getUsername())){
                user.getMarker().setSnippet("Me");
                user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                UserManager.myIdentity.setLocalisation(new Location(user.getMarker().getPosition().latitude,user.getMarker().getPosition().longitude));
            }

            if (user.getUsername().equals(group.getSupervisor().getUsername())){
                user.getMarker().setSnippet("Supervisor");
                user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
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
}
