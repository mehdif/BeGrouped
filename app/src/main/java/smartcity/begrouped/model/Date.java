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
}
