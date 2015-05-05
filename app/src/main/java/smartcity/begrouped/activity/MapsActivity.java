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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
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
import smartcity.begrouped.model.Temps;
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
    private Marker nearestPOIMareker=null;

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
                SystemClock.elapsedRealtime() + TWO_SECONDS, pi);

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
                if (MyApplication.myPosition!=null){

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MyApplication.myPosition.getLatitude(),MyApplication.myPosition.getLongitude()), 8));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

                }
                afficherNearestPOI();


            }
        }
    }

    private void afficherNearestPOI() {
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String type=intent.getStringExtra("type");
        double latitude=intent.getDoubleExtra("latitude",-1);
        double longitude=intent.getDoubleExtra("longitude",-1);
        if ((latitude!=-1) && (longitude!=-1)&&(name!=null) && (type!=null)){
            nearestPOIMareker=  mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))
                    .title(name).snippet(type));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(myPosition))
        {
            //handle click here

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

    @Override
    protected void onResume() {
        super.onResume();
        pi = PendingIntent.getBroadcast(this, 0, new Intent(
                "com.authorwjf.MajPositions"), 0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWO_SECONDS, pi);
        MyApplication.requestingMemberPositions=true;
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
                showDatePicker();

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



        return super.onOptionsItemSelected(item);
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
                if (MyApplication.myIdentity.getUsername().equals(MyApplication.currentGroup.getSupervisor().getUsername())) {
                    title = getString(R.string.title_add_appointment);
                    //getSupportActionBar().setTitle(title);
                    if (aptMarker == null) {
                        createAppointment();
                        Toast.makeText(getApplicationContext(), "Choose a location !", Toast.LENGTH_LONG).show();
                    } else {
                        aptEnCreation = true;
                        new AlertDialog.Builder(this)
                                .setTitle("Delete Appointment")
                                .setMessage("Are you sure you want to delete the appointment?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        aptMarker.remove();
                                        aptMarker = null;
                                        MyApplication.currentGroup.setAppointment(null);
                                        UserManager.sendRemoveApt(MyApplication.currentGroup.getName(),MapsActivity.this);
                                        if (pathToApt != null) {
                                            pathToApt.remove();
                                            pathToApt = null;
                                        }

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        aptEnCreation = false;
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "You're not the supervisor of this group",Toast.LENGTH_LONG).show();
                }

                break;
            case 2:
                //fragment = new HelpFragment();
                title = getString(R.string.title_path_to_appointment);
                //getSupportActionBar().setTitle(title);
                creatingApt=false;
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
                //getSupportActionBar().setTitle(title);
                //Toast.makeText(getApplicationContext(), "Find Program itinirerary !",Toast.LENGTH_LONG).show();
                creatingApt=false;
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
                //getSupportActionBar().setTitle(title);
               // Toast.makeText(getApplicationContext(), "Show Program !",Toast.LENGTH_LONG).show();
                //afficherDialogChoixDate();
                creatingApt=false;
                showDatePicker();
                break;
            case 5:
                title = getString(R.string.title_hide_program);
                //getSupportActionBar().setTitle(title);
                creatingApt=false;

                if (programShown) {
                    hideProgram();
                    programShown=false;
                }
                else{
                    Toast.makeText(getApplicationContext(), "There is no shown program",Toast.LENGTH_LONG).show();
                }
                break;
            case 6:
                Intent ii = new Intent(getApplicationContext(), GroupHomeFragment.class);
                startActivity(ii);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            default:
                break;
        }

    }




    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = this.getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.date_picker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.ENGLISH);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("12.12.2010"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            // Date choosenDateFromUI = formatter.parse(datePickerShowDialogButton.getText().toString());

            //choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons
        dialogBuilder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        creatingApt=false;
                    }
                }
        );
        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );

                        //dialog.dismiss();
                        Date date=new Date(choosen.get(Calendar.DAY_OF_MONTH),choosen.get(Calendar.MONTH)+1,choosen.get(Calendar.YEAR));
                        if (!creatingApt) {
                            POIManager.getDayProgramOfGroupByTask(date, MyApplication.currentGroup.getName(),MapsActivity.this);

                        }else {
                            showTimePicker(date);
                        }
                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText( dateViewFormatter.format(choosenDate.getTime()));
                        dateTextView.setTextColor(Color.parseColor("#ff0000"));
                        ((Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setEnabled(true);
                    }
                }
        );
        // Finish
        dialog.show();
    }

    private void showTimePicker(final Date date) {
        LayoutInflater inflater = this.getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.timepicker, null);
        dialogBuilder.setView(customView);

        final TimePicker timePicker =
                (TimePicker) customView.findViewById(R.id.dialog_timepicker);
        final TextView timeTextView =
                (TextView) customView.findViewById(R.id.dialog_timeview);
        // View settings
        dialogBuilder.setTitle("Choose a time");

        // Buttons
        dialogBuilder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                1,1,2015,
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute()
                        );
                        Temps temps=new Temps(choosen.get(Calendar.HOUR_OF_DAY),choosen.get(Calendar.MINUTE));
                        MapsActivity.aptEnCreation=true;
                        if (aptMarker!=null) aptMarker.remove();
                        aptMarker = mMap.addMarker(new MarkerOptions().position(latLngForApt)
                                .title("Appointment").snippet(date.afficher()+" at "+temps.afficher()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        Appointment appoint=new Appointment(aptMarker,temps.getHh(),temps.getMm(),date.getJj(),date.getMm(),date.getYy());
                        MyApplication.currentGroup.setAppointment(appoint);
                        UserManager.sendAptToServer(appoint,MapsActivity.this);
                        //dialog.dismiss();

                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        // Finish

        dialog.show();
        creatingApt=false;

    }
    public void afficherLeProgramme(LinkedList<POI> listPOI){
        listPOI=POIManager.sortPOIByTime(listPOI);
        if (programShown) {
            hideProgram();
            programShown = false;
        }
        MyApplication.listOfCurrentPOIS = listPOI;

        if (MyApplication.listOfCurrentPOIS != null) {
            programShown = true;
            for (int i = 0; i < MyApplication.listOfCurrentPOIS.size(); i++) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(MyApplication.listOfCurrentPOIS.get(i).getLocation().getLatitude(), MyApplication.listOfCurrentPOIS.get(i).getLocation().getLongitude()))
                        .title(MyApplication.listOfCurrentPOIS.get(i).getName()).snippet(MyApplication.listOfCurrentPOIS.get(i).getTempsOfVisite().afficher()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                MyApplication.listOfCurrentPOIS.get(i).setMarker(marker);
            }
        }
    }
}
