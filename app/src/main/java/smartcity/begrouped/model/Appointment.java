package smartcity.begrouped.model;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * Created by Anes on 29/04/2015.
 */
public class Appointment {
    private Marker aptMarker;
    private Date date;

    public Appointment(Marker aptMarker, String time, String laDate) {
        this.aptMarker = aptMarker;
        date=dateCreation(time,laDate);

    }

    private Date dateCreation(String time, String laDate) {
        try {
            String hh = time.substring(0, time.indexOf(":"));
            String min = time.substring(time.indexOf(":") + 1, time.length());
            String mounth = laDate.substring(0, time.indexOf("/"));
            String sansMounth = laDate.substring(time.indexOf("/") + 1, laDate.length());
            String day = sansMounth.substring(0, time.indexOf("/"));

            String year = sansMounth.substring(time.indexOf("/") + 1, sansMounth.length());
            return new Date(Integer.parseInt(year), Integer.parseInt(mounth), Integer.parseInt(day), Integer.parseInt(hh), Integer.parseInt(min));
        }
        catch (Exception e){
            Log.e("date conversion erreur", "cannot convert date");
            return new Date(2015,1,1,10,00);
        }


    }

    public Marker getAptMarker() {
        return aptMarker;
    }

    public Date getDate() {
        return date;
    }

    public void setAptMarker(Marker aptMarker) {
        this.aptMarker = aptMarker;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
