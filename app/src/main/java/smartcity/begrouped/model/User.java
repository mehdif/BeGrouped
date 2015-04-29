package smartcity.begrouped.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by a on 27/04/2015.
 */
public class User {

   private String firstname;
   private String lastname;
   private String username;
   private String password;
   private Location localisation;
   private Marker marker=null;
    private String phoneNumber;

    public User(String firstname, String lastname, String username, String password, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.phoneNumber =phoneNumber;
    }

    public void setLocalisation(Location localisation){
        this.localisation=localisation;
    }
    public Location getLocalisation(){
        return localisation;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
