package smartcity.begrouped.model;

/**
 * Created by Anes on 30/04/2015.
 */
public class Date {
    private int yy,mm,jj;

    public Date(int jj, int mm, int yy) {
        this.jj = jj;
        this.mm = mm;
        this.yy = yy;
    }

    public int getYy() {
        return yy;
    }

    public int getMm() {
        return mm;
    }

    public int getJj() {
        return jj;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public void setJj(int jj) {
        this.jj = jj;
    }

    public static Date dateFromString(String dateSTR) {
        String day = dateSTR.substring(0, dateSTR.indexOf("-"));
        String sansDay = dateSTR.substring(dateSTR.indexOf("-") + 1, dateSTR.length());
        String mounth = sansDay.substring(0, sansDay.indexOf("-"));
        String year = sansDay.substring(sansDay.indexOf("-") + 1, sansDay.indexOf(" "));
        return new Date(Integer.parseInt(day),Integer.parseInt(mounth),Integer.parseInt(year));
    }
    public static Date dateFromLittleString(String dateSTR) {
        String day = dateSTR.substring(0, dateSTR.indexOf("-"));
        String sansDay = dateSTR.substring(dateSTR.indexOf("-") + 1, dateSTR.length());
        String mounth = sansDay.substring(0, sansDay.indexOf("-"));
        String year = sansDay.substring(sansDay.indexOf("-") + 1, sansDay.length());
        return new Date(Integer.parseInt(day),Integer.parseInt(mounth),Integer.parseInt(year));
    }
    public String afficher(){
        return jj+"-"+mm+"-"+yy;
    }

    @Override
    public String toString() {
        return yy+"-"+mm+"-"+jj;
    }

    @Override
    public boolean equals(Object o) {
        Date other=(Date )o;
        return ((jj==other.getJj()) && (mm==other.getMm()) && (yy==other.getYy()));
    }
}
