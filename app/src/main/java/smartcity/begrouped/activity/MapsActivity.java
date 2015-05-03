package smartcity.begrouped.activity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.util.Log;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.MarkerManager;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.drawpathclasses.GMapV2Direction;
import smartcity.begrouped.drawpathclasses.GetDirectionsAsyncTask;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Date;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.utils.MyApplication;

public class MapsActivity extends ActionBarActivity implements FragmentDrawerMap.FragmentDrawerListener,GoogleMap.OnMarkerClickListener {

    //Add Actionbar + menu : Hassan
    private Toolbar mToolbar;
    private FragmentDrawerMap drawerFragment;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private TextView latituteField;
    private TextView longitudeField;
    private Marker myPosition=null,beginPath=null,endPath=null;
    public static Marker aptMarker=null;
    public static boolean aptPathShown=false;
    public static boolean programPathShown=false;
    public static boolean programShown=false;
    private LatLng latLngForApt=null;
    public static MarkerManager markerManager;
    public static Polyline pathToApt=null;
    public static LinkedList<Polyline> programPath=null;

    final static private long ONE_MINUTE = 60000;
    final static private long TWENTY_SECONDS = 20000;
    final static private long FIVE_SECONDS = 5000;
    final static private long TWO_SECONDS = 2000;
    private boolean pathToAptEnCreation=false;

    PendingIntent pi;
    AlarmManager am;


    private boolean creatingApt=false;
    private boolean drawingPath1=false;
    private boolean drawingPath2=false;
    public static boolean aptEnCreation=false;

    public MapsActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MyApplication.requestingMemberPositions=true;
        initializeMarkers();
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


/*    private void createNotification(View view) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MapsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        // Actions are just fake
        if(MyApplication.myPosition != null) {

            LinkedList<POI> listPOI = POIManager.getNearestPoiByTask(MyApplication.myPosition.getLatitude(), MyApplication.myPosition.getLongitude());

            if(listPOI != null){
                Notification noti = new Notification.Builder(this)
                        .setContentTitle("You are near " + listPOI.get(0).getName())
                        .setContentText(listPOI.get(0).getType()).setSmallIcon(R.drawable.monument)
                        .setContentIntent(pIntent).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);
            }

        }
    }*/

    private void setupUpdatePositionTask() {
        markerManager=new MarkerManager(mMap, MyApplication.currentGroup);
        pi = PendingIntent.getBroadcast(this, 0, new Intent(
                "com.authorwjf.MajPositions"), 0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWENTY_SECONDS , pi);

    }
    private void initializeMarkers(){
        if (MyApplication.currentGroup!=null) {
            for (int i = 0; i < MyApplication.currentGroup.getMembers().size();i++){
                MyApplication.currentGroup.getMembers().get(i).setMarker(null);
            }
        }

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

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.requestingMemberPositions=false;
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

        if (pathToAptEnCreation) {
            if (pathToApt!=null) pathToApt.remove();
            pathToApt=newPolyline;
            pathToAptEnCreation=false;
        }
        else {
            programPath.add(newPolyline);
        }



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



    public void findDirectionBetweenMeAndApt(){
        if (MyApplication.myIdentity!=null){
            if (MyApplication.myIdentity.getLocalisation()!=null){
                findDirections(MyApplication.myIdentity.getLocalisation().getLatitude(),
                        MyApplication.myIdentity.getLocalisation().getLongitude(),
                        aptMarker.getPosition().latitude,aptMarker.getPosition().longitude, GMapV2Direction.MODE_DRIVING);
            }
            else {
                Toast.makeText(getApplicationContext(), "Your position is not known yet", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void findDirectionsForDayProgram(){
        programPath=new LinkedList<>();
        for (int i=0;i<MyApplication.listOfCurrentPOIS.size()-1;i++){
            try {
                findDirections(MyApplication.listOfCurrentPOIS.get(i).getLocation().getLatitude(), MyApplication.listOfCurrentPOIS.get(i).getLocation().getLongitude(), MyApplication.listOfCurrentPOIS.get(i + 1).getLocation().getLatitude(), MyApplication.listOfCurrentPOIS.get(i + 1).getLocation().getLongitude(), GMapV2Direction.MODE_DRIVING);
            }
            catch (NullPointerException e){
                Log.i("calcul program path", e.getMessage());
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
        adb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                MapsActivity.aptEnCreation=true;
                EditText hh = (EditText)alertDialogView.findViewById(R.id.hhRDV);
                EditText min = (EditText)alertDialogView.findViewById(R.id.minRDV);
                EditText jj = (EditText)alertDialogView.findViewById(R.id.jjRDV);
                EditText mm = (EditText)alertDialogView.findViewById(R.id.mmRDV);
                EditText yy = (EditText)alertDialogView.findViewById(R.id.aaRDV);
                if (aptMarker!=null) aptMarker.remove();
                aptMarker = mMap.addMarker(new MarkerOptions().position(latLngForApt)
                        .title("Appointment").snippet(jj.getText().toString()+"-"+mm.getText().toString()+"-"+yy.getText().toString()+" at "+hh.getText().toString()+":"+min.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                Appointment appoint=new Appointment(aptMarker,hh.getText().toString(),min.getText().toString(),jj.getText().toString(),mm.getText().toString(),yy.getText().toString());
                MyApplication.currentGroup.setAppointment(appoint);
                UserManager.sendAptToServer(appoint);

            } });
        adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on quittera l'application

            } });
        adb.show();

    }
    public void afficherDialogChoixDate(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialog_date, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Choose the program date");
        adb.setIcon(android.R.drawable.ic_dialog_info);
        adb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                EditText jj = (EditText)alertDialogView.findViewById(R.id.jjRDV);
                EditText mm = (EditText)alertDialogView.findViewById(R.id.mmRDV);
                EditText yy = (EditText)alertDialogView.findViewById(R.id.aaRDV);
                Date date=new Date(Integer.parseInt(jj.getText().toString()),Integer.parseInt(mm.getText().toString()),Integer.parseInt(yy.getText().toString()));
                MyApplication.listOfCurrentPOIS=POIManager.sortPOIByTime(POIManager.getDayProgramOfGroupByTask(date,MyApplication.currentGroup.getName()));
                if (programShown){
                    hideProgram();
                    programShown=false;
                }


                if (MyApplication.listOfCurrentPOIS!=null){
                    programShown=true;
                    for (int i=0;i<MyApplication.listOfCurrentPOIS.size();i++){
                        Marker marker= mMap.addMarker(new MarkerOptions().position(new LatLng(MyApplication.listOfCurrentPOIS.get(i).getLocation().getLatitude(), MyApplication.listOfCurrentPOIS.get(i).getLocation().getLongitude()))
                                .title(MyApplication.listOfCurrentPOIS.get(i).getName()).snippet(MyApplication.listOfCurrentPOIS.get(i).getTempsOfVisite().afficher()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        MyApplication.listOfCurrentPOIS.get(i).setMarker(marker);
                    }
                }
            } });
        adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on quittera l'application

            } });
        adb.show();

    }

    public void hideProgram(){
        if (MyApplication.listOfCurrentPOIS!=null){
            for (int i=0;i<MyApplication.listOfCurrentPOIS.size();i++){
                if (MyApplication.listOfCurrentPOIS.get(i).getMarker()!=null) {
                    MyApplication.listOfCurrentPOIS.get(i).getMarker().remove();
                    MyApplication.listOfCurrentPOIS.get(i).setMarker(null);
                }
            }
            if (programPath!=null){
                for (int i=0;i<programPath.size();i++){
                    programPath.get(i).remove();
                }
            }

        }
    }




    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {

        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case 1:
                //fragment = new AboutFragment();
                title = getString(R.string.title_add_appointment);
                getSupportActionBar().setTitle(title);
                if (aptMarker==null) {
                    createAppointment();
                    Toast.makeText(getApplicationContext(), "Choose a location !", Toast.LENGTH_LONG).show();
                }
                else {
                    aptEnCreation=true;
                    new AlertDialog.Builder(this)
                            .setTitle("Delete Appointment")
                            .setMessage("Are you sure you want to delete the appointment?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    aptMarker.remove();
                                    aptMarker=null;
                                    MyApplication.currentGroup.setAppointment(null);
                                    UserManager.sendRemoveApt(MyApplication.currentGroup.getName());
                                    if (pathToApt!=null){
                                        pathToApt.remove();
                                        pathToApt=null;
                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    aptEnCreation=false;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            case 2:
                //fragment = new HelpFragment();
                title = getString(R.string.title_path_to_appointment);
                getSupportActionBar().setTitle(title);
                if (aptMarker==null)
                Toast.makeText(getApplicationContext(), "There is no appointment",Toast.LENGTH_LONG).show();
                else {
                    if (pathToApt==null) {
                        pathToAptEnCreation = true;
                        findDirectionBetweenMeAndApt();
                    }
                    else {
                        pathToApt.remove();
                        pathToApt=null;
                    }


                }





                break;
            case 3:
                title = getString(R.string.title_find_program_itinerary);
                getSupportActionBar().setTitle(title);
                Toast.makeText(getApplicationContext(), "Find Program itinirerary !",Toast.LENGTH_LONG).show();
                if (programShown){
                    if (programPath!=null){
                        for (int j=0;j<programPath.size();j++){
                            programPath.get(j).remove();
                        }
                        programPath=null;
                    }
                    else{
                        // tracer l'itineraire

                        findDirectionsForDayProgram();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "there is no loaded program",Toast.LENGTH_LONG).show();
                }


                break;
            case 4:
                title = getString(R.string.title_show_program);
                getSupportActionBar().setTitle(title);
                Toast.makeText(getApplicationContext(), "Show Program !",Toast.LENGTH_LONG).show();
                afficherDialogChoixDate();
                break;
            case 5:
                title = getString(R.string.title_hide_program);
                getSupportActionBar().setTitle(title);

                if (programShown) {
                    hideProgram();
                    programShown=false;
                }
                else{
                    Toast.makeText(getApplicationContext(), "There is no shown program",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }


}
