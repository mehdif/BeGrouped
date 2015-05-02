package smartcity.begrouped.model;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by a on 27/04/2015.
 */
public class POI {
    private Marker marker;
    private String type;
    private String typeDetail;
    private String name;
    private String addres;
    private String phone;
    private Date dateOfVisite;
    private Temps tempsOfVisite;
    private String website;
    private String email;
    private Location location;


    public POI(String type, String typeDetail, String name, String addres, String phone, String email, String website, Location location) {
        this.type = type;
        this.typeDetail = typeDetail;
        this.name = name;
        this.addres = addres;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public Location getLocation() {
        return location;
    }

    public Date getDateOfVisite() {
        return dateOfVisite;
    }

    public Temps getTempsOfVisite() {
        return tempsOfVisite;
    }

    public void setDateOfVisite(Date dateOfVisite) {
        this.dateOfVisite = dateOfVisite;
    }

    public void setTempsOfVisite(Temps tempsOfVisite) {
        this.tempsOfVisite = tempsOfVisite;
    }

    public POI(){}

    public Marker getMarker() {
        return marker;
    }

    public String getType() {
        return type;
    }

    public String getTypeDetail() {
        return typeDetail;
    }

    public String getName() {
        return name;
    }

    public String getAddres() {
        return addres;
    }

    public String getPhone() {
        return phone;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
