package smartcity.begrouped.model;

/**
 * Created by Anes on 30/04/2015.
 */
public class Temps {
    private int hh,mm;

    public Temps(int hh, int mm) {
        this.hh = hh;
        this.mm = mm;
    }

    public int getHh() {
        return hh;
    }

    public void setHh(int hh) {
        this.hh = hh;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getMm() {
        return mm;
    }

    public static Temps tempsFromString(String dateSTR) {
        String hh = dateSTR.substring(dateSTR.indexOf(" ")+1, dateSTR.indexOf(":"));
        // minute
        String sansHour = dateSTR.substring(dateSTR.indexOf(":") + 1, dateSTR.length());
        String min= sansHour.substring(0, sansHour.indexOf(":"));

        return new Temps(Integer.parseInt(hh),Integer.parseInt(min));
    }
    public static Temps tempsFromLittleString(String dateSTR) {
        String hh = dateSTR.substring(0, dateSTR.indexOf(":"));
        // minute
        String sansHour = dateSTR.substring(dateSTR.indexOf(":") + 1, dateSTR.length());
        String min= sansHour.substring(0, sansHour.indexOf(":"));

        return new Temps(Integer.parseInt(hh),Integer.parseInt(min));
    }
    public String afficher(){
        return hh+":"+mm;
    }
}
