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
}
