package smartcity.begrouped.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.utils.MyApplication;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,LocationListener {

    private static String TAG = MainActivity.class.getSimpleName();



    final static private long ONE_MINUTE = 60000;
    final static private long FIVE_MINUTES = ONE_MINUTE * 5;
    final static private long FIVE_SECONDS = 5000;
    PendingIntent pi;
    AlarmManager am;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    private String provider;
    // la variable i est juste pour le test
    int i=0;


    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // create me statically for test
            //UserManager.createMeForTest();

            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(this);

            // display the first navigation drawer view on app launch
            displayView(0);


            // Get the location manager
            if (MyApplication.locationManager != null)
                MyApplication.locationManager.removeUpdates(MyApplication.locationListener);
            MyApplication.locationListener = this;
            MyApplication.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = MyApplication.locationManager.getBestProvider(criteria, false);
            Location location = MyApplication.locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                Toast.makeText(this, "no location possible ",
                        Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {

        }

        setUpRequestNearestPoint();
    }

    private void setUpRequestNearestPoint() {
        pi = PendingIntent.getBroadcast(this, 0, new Intent(
                "com.authorwjf.nearestpoint"), 0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIVE_SECONDS , pi);
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

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new JoinGroupFragment();
                title = getString(R.string.title_join_group);
                break;
            case 2:
                fragment = new CreateGroupFragment();
                title = getString(R.string.title_create_group);
                break;
            case 3:
                fragment = new ManageGroupFragment();
                title = getString(R.string.title_manage_group);
                break;
            case 4:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                Toast.makeText(getApplicationContext(), "About BeGrouped !", Toast.LENGTH_LONG).show();
                break;
            case 5:
                fragment = new HelpFragment();
                title = getString(R.string.title_help);
                Toast.makeText(getApplicationContext(), "Need some help?", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }




    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.locationManager.removeUpdates(MyApplication.locationListener);
        MyApplication.locationManager.requestLocationUpdates(provider, 20000, 1, this);
        MyApplication.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0,
                this);


    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Toast.makeText(this, "MAJ " + i+ "lat: "+lat+ " long: "+lng,
                Toast.LENGTH_SHORT).show();
        i++;
        UserManager.updateMyLocationToServer(new LatLng(lat,lng));
        if (MyApplication.myPosition==null){
            MyApplication.myPosition=new smartcity.begrouped.model.Location(lat,lng);
        }
        else {
            MyApplication.myPosition.setLatitude(lat);
            MyApplication.myPosition.setLongitude(lng);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
