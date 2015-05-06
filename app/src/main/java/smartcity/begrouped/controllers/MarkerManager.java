package smartcity.begrouped.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import smartcity.begrouped.activity.MapsActivity;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Date;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.Location;
import smartcity.begrouped.model.Temps;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.getFromUrl;


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
    public void updateLocations(ReceiverUpdatePositions rec){
        GroupManager.callTaskUpdateGroupMemberLocations(group,rec);
        if (!MapsActivity.aptEnCreation) requestForApt();
    }



    public void updateMarkerPositions(){
        try {
            for (int i = 0; i < group.getMembers().size(); i++) {

                User user = group.getMembers().get(i);
                if (user.getLocalisation() != null) {
                    if (user.getMarker() != null)
                        user.getMarker().setPosition(new LatLng(user.getLocalisation().getLatitude(), user.getLocalisation().getLongitude()));
                    else
                        user.setMarker(mMap.addMarker(new MarkerOptions().position(new LatLng(user.getLocalisation().getLatitude(), user.getLocalisation().getLongitude()))
                                .title(user.getFirstname() + " " + user.getLastname()).snippet("member")));

                    if (user.getUsername().equals(MyApplication.myIdentity.getUsername())) {
                        user.getMarker().setSnippet("Me");
                        user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        MyApplication.myIdentity.setLocalisation(new Location(user.getMarker().getPosition().latitude, user.getMarker().getPosition().longitude));
                        MyApplication.myIdentity.setMarker(user.getMarker());
                        if (firstTime)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user.getMarker().getPosition(), 8));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                        firstTime = false;
                    }
            /*
            if (user.getUsername().equals(group.getSupervisor().getUsername())){
                user.getMarker().setSnippet("Supervisor");
                user.getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }*/
                }
            }
        } catch(Exception e){
            Log.e("exception", e.getMessage());
        }

    }







    private void requestForApt() {
        new AsTaskRequestForApt().execute();
    }

    public Appointment getAptOfMyGroup() {
        // on recupere a partir de la bdd avec getURL
        ///return aptOfMyGroup;
        String jsonReponse=getFromUrl(AllUrls.GET_RDV+"/"+MyApplication.currentGroup.getName()+"/"+ MyApplication.myIdentity.getUsername()+"/"+MyApplication.myIdentity.getPassword());
        Log.v("Json group info : ", jsonReponse);

        //Json file parser
        try {

            JSONObject jsonObject = new JSONObject(jsonReponse);

            String latitude = (String) jsonObject.get(Constants.LATITUDE);
            String longitude= (String) jsonObject.get(Constants.LONGITUDE);
            String dateSTR = (String) jsonObject.get(Constants.DATE);
            Appointment appoint=new Appointment();
            Location location=new Location(Double.parseDouble(latitude),Double.parseDouble(longitude));
            Date date= Date.dateFromString(dateSTR);
            Temps temps=Temps.tempsFromString(dateSTR);
            appoint.setLocation(location);
            appoint.setDate(date);
            appoint.setTemps(temps);




            return appoint;

        }
        catch(Exception e){
            Log.e("Error : ", e.getMessage());
            return null;
        }

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
            if ((appoint!=null) && (!MapsActivity.aptEnCreation)){
                // supprimer le chemin si l'appointment n'est pas l'ancien
                if (MyApplication.currentGroup.getAppointment()!=null){
                    if ((!appoint.getLocation().equals(MyApplication.currentGroup.getAppointment().getLocation()))){
                        if (MapsActivity.pathToApt!=null) {
                            MapsActivity.pathToApt.remove();
                            MapsActivity.pathToApt=null;
                        }
                    }
                }
                MyApplication.currentGroup.setAppointment(appoint);
                // afficher le RDV sur la map
                if (MapsActivity.aptMarker!=null) MapsActivity.aptMarker.remove();
                MapsActivity.aptMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(appoint.getLocation().getLatitude(),appoint.getLocation().getLongitude()))
                        .title("Appointment").snippet(appoint.getDate().afficher()+" at "+appoint.getTemps().afficher()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                appoint.setAptMarker(MapsActivity.aptMarker);
            }
        }
    }
}
