package smartcity.begrouped.model;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;


/**
 * Created by Anes on 29/04/2015.
 */
public class Appointment {
    private Marker aptMarker;
    private Date date;
    private Temps temps;
    private Location location;

    public Appointment(Marker aptMarker, int hh,int min, int jj, int mm,int yy) {
        this.aptMarker = aptMarker;
        temps=new Temps(hh,min);
        date=new Date(jj,mm,yy);
        location=new Location(aptMarker.getPosition().latitude,aptMarker.getPosition().longitude);


    }
    public Appointment(){

    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    private void dateCreation(String time, String laDate) {
        try {
            String hh = time.substring(0, time.indexOf(":"));
            String min = time.substring(time.indexOf(":") + 1, time.length());
            String mounth = laDate.substring(0, time.indexOf("/"));
            String sansMounth = laDate.substring(time.indexOf("/") + 1, laDate.length());
            String day = sansMounth.substring(0, time.indexOf("/"));

            String year = sansMounth.substring(time.indexOf("/") + 1, sansMounth.length());
            date = new Date(Integer.parseInt(day), Integer.parseInt(mounth), Integer.parseInt(year));
            temps = new Temps(Integer.parseInt(hh), Integer.parseInt(min));
        } catch (Exception e) {
            Log.e("date conversion erreur", "cannot convert date");
            //return new Date(2015,1,1,10,00);
        }


    }

    public void setAptMarker(Marker aptMarker) {
        this.aptMarker = aptMarker;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTemps(Temps temps) {
        this.temps = temps;
    }

    public Marker getAptMarker() {
        return aptMarker;
    }

    public Date getDate() {
        return date;
    }

    public Temps getTemps() {
        return temps;
    }

    public String getDateSousForme(){
        return date.getYy()+"-"+date.getMm()+"-"+date.getJj();
    }
}
