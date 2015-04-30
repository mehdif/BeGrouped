package smartcity.begrouped.model;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by a on 27/04/2015.
 */
public class Group {
    private User supervisor;
    private LinkedList<User> members;
    private LinkedList<POI> lieuxAVisiter;
    private String name;
    private String locationName;
    private String expirationDate;

    public Group(User supervisor, LinkedList<User> members, LinkedList<POI> lieuxAVisiter, String name ,String locationName) {
        this.supervisor = supervisor;
        this.members = members;
        this.lieuxAVisiter = lieuxAVisiter;
        this.locationName = locationName;
        this.name = name;
    }
    public Group(User supervisor,String name ,String locationName) {
        this.supervisor = supervisor;
        this.locationName = locationName;
        this.name = name;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public LinkedList<User> getMembers() {
        return members;
    }

    public LinkedList<POI> getLieuxAVisiter() {
        return lieuxAVisiter;
    }

    public String getName() {
        return name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName){
        this.locationName = locationName;
    }

    public void setSupervisor(User supervisor){
        this.supervisor = supervisor;
    }

    public void setExpirationDate(String expirationDate){
        this.expirationDate = expirationDate;
    }
}
