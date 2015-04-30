package smartcity.begrouped.activity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.MarkerManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.drawpathclasses.GMapV2Direction;
import smartcity.begrouped.drawpathclasses.GetDirectionsAsyncTask;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.utils.MyApplication;

public class MapsActivity extends ActionBarActivity implements FragmentDrawerMap.FragmentDrawerListener,GoogleMap.OnMarkerClickListener {

    //Add Actionbar + menu : Hassan
    private Toolbar mToolbar;
    private FragmentDrawerMap drawerFragment;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private TextView latituteField;
    private TextView longitudeField;
    private Marker myPosition=null, aptMarker=null,beginPath=null,endPath=null;
    private LatLng latLngForApt=null;
    public static MarkerManager markerManager;

    final static private long ONE_MINUTE = 60000;
    final static private long TWENTY_SECONDS = 20000;
    final static private long FIVE_SECONDS = 5000;
    final static private long TWO_SECONDS = 2000;

    PendingIntent pi;
    AlarmManager am;


    private boolean creatingApt=false;
    private boolean drawingPath1=false;
    private boolean drawingPath2=false;

    public MapsActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawerMap)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_map);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_map, (DrawerLayout) findViewById(R.id.drawer_layout_map), mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
        //displayView(0);


        setUpMapIfNeeded();
        setupUpdatePositionTask();
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        //majPosition(46,5);



    }


    private void setupUpdatePositionTask() {
        markerManager=new MarkerManager(mMap, MyApplication.currentGroup);
        pi = PendingIntent.getBroadcast(this, 0, new Intent(
                "com.authorwjf.MajPositions"), 0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIVE_SECONDS , pi);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
               // setUpMap();
                mMap.setOnMarkerClickListener(this);
                mMap.setOnMapClickListener(new MyMapClickListener());
            }
        }
    }

    private void majPosition(double lat, double lng) {
       if (myPosition==null){
           myPosition = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                   .title("myPosition").snippet("gawri"));
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
          // findDirections(lat,lng,lat+1,lng+2,GMapV2Direction.MODE_DRIVING);
       }

        else{
           myPosition.setPosition(new LatLng(lat,lng));

       }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(myPosition))
        {
            //handle click here
            Toast.makeText(this, "hiho " ,
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }



    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }
    public void handleGetDirectionsResult(ArrayList directionPoints)
    {
        Polyline newPolyline;
        GoogleMap mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);
        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add((LatLng) directionPoints.get(i));
        }
        newPolyline = mMap.addPolyline(rectLine);
    }

    public void createAppointment(){
        creatingApt=true;
        drawingPath1=false;
        drawingPath2=false;
    }
    public void drawPath(View v){
        creatingApt=false;
        drawingPath1=true;
        drawingPath2=false;
    }

    class MyMapClickListener implements GoogleMap.OnMapClickListener{

        @Override
        public void onMapClick(LatLng latLng) {
            if (creatingApt){
                latLngForApt=latLng;
                afficherDialogRDV();
                creatingApt=false;
            }
            else if (drawingPath1){
                beginPath=  mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Begin"));
                drawingPath1=false;
                drawingPath2=true;

            }
            else if (drawingPath2){
                endPath=  mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("End"));
                drawingPath1=false;
                drawingPath2=false;
                findDirections(beginPath.getPosition().latitude,beginPath.getPosition().longitude,endPath.getPosition().latitude,endPath.getPosition().longitude, GMapV2Direction.MODE_DRIVING);
            }
        }
    }



 public void findDirectionBetweenMeAndApt(Marker markerApt){
        if (MyApplication.myIdentity!=null){
            if (MyApplication.myIdentity.getLocalisation()!=null){
                findDirections(MyApplication.myIdentity.getLocalisation().getLatitude(),
                        MyApplication.myIdentity.getLocalisation().getLongitude(),
                        markerApt.getPosition().latitude,markerApt.getPosition().longitude, GMapV2Direction.MODE_DRIVING);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //if(id == R.id.action_search){
        //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
        //return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void afficherDialogRDV(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogperso, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Add an appointment");
        adb.setIcon(android.R.drawable.ic_dialog_info);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                EditText heure = (EditText)alertDialogView.findViewById(R.id.heureRDV);
                EditText date = (EditText)alertDialogView.findViewById(R.id.dateRDV);
                if (aptMarker!=null) aptMarker.remove();
                aptMarker = mMap.addMarker(new MarkerOptions().position(latLngForApt)
                        .title("Appointment").snippet(date.getText().toString()+" at "+heure.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                UserManager.sendAptToServer(new Appointment(aptMarker,heure.getText().toString(), date.getText().toString()));
            } });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on quittera l'application

            } });
        adb.show();

    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        //Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                //fragment = new HomeFragment();
                title = getString(R.string.title_home_map);
                getSupportActionBar().setTitle(title);
                break;
            case 1:
                //fragment = new AboutFragment();
                title = getString(R.string.title_add_appointment);
                getSupportActionBar().setTitle(title);
                createAppointment();
                Toast.makeText(getApplicationContext(), "Choose a location !",Toast.LENGTH_LONG).show();
                break;
            case 2:
                //fragment = new HelpFragment();
                title = getString(R.string.title_path_to_appointment);
                getSupportActionBar().setTitle(title);
                Toast.makeText(getApplicationContext(), "Path to Approintment",Toast.LENGTH_LONG).show();
                break;
            case 3:
                title = getString(R.string.title_find_program_itinerary);
                getSupportActionBar().setTitle(title);
                Toast.makeText(getApplicationContext(), "Find Program itinirerary !",Toast.LENGTH_LONG).show();
            default:
                break;
        }

        //if (fragment != null) {
          //  FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.container_body, fragment);
            //fragmentTransaction.commit();

        //}
    }


}
