package smartcity.begrouped.controllers;

import com.google.android.gms.maps.model.Marker;

import java.util.LinkedList;

import smartcity.begrouped.model.POI;

/**
 * Created by a on 27/04/2015.
 */
public class POIManager {

    public static LinkedList<POI> createPoiForTest() {
        LinkedList<POI> lesPOI = new LinkedList<POI>();
        for (int i = 0; i < 5; i++) {
            POI poi = new POI("type : " + i, "type detail : " + i,
                    "name " + i, "address " + i, "phone " + i);
            lesPOI.add(poi);
        }
        return lesPOI;
    }
}
