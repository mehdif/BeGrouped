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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
