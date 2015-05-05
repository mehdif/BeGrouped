package smartcity.begrouped.model;

/**
 * Created by Anes on 30/04/2015.
 */
public class Temps implements  Comparable{
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
        String chaine="";
        if (hh>9) chaine=chaine+hh+":";
        else chaine=chaine+"0"+hh+":";
        if (mm>9) chaine=chaine+mm;
        else chaine=chaine+"0"+mm;
        return chaine;
    }

    @Override
    public int compareTo(Object another) {
        Temps otherTime=(Temps)another;
        if (hh<otherTime.getHh()) return -1;
        if (hh>otherTime.getHh()) return 1;
        if (mm<otherTime.getMm()) return -1;
        if (mm>otherTime.getMm()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return hh+":"+mm;
    }
}
