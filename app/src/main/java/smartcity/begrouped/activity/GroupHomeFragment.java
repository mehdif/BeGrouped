package smartcity.begrouped.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.utils.MyApplication;


public class GroupHomeFragment extends ActionBarActivity implements FragmentDrawerGroup.FragmentDrawerListener {

    private ImageButton map;
    private ImageButton members;
    private ImageButton chat;
    private ImageButton schedule;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    private FragmentDrawerGroup drawerFragment;
    private TextView textViewMembers;
    private TextView textViewChat;
    private TextView textViewSchedule;
    private TextView textViewMap;

    public GroupHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_group_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawerGroup) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_home_group);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_home_group, (DrawerLayout) findViewById(R.id.drawer_layout_group), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setTitle("Home");



        map = (ImageButton) findViewById(R.id.imageButton2);
        members = (ImageButton) findViewById(R.id.imageButtonMembers);
        chat = (ImageButton) findViewById(R.id.imageButtonChat);
        schedule = (ImageButton) findViewById(R.id.imageButtonSched);
        textViewChat = (TextView) findViewById(R.id.textViewChat);
        textViewMap = (TextView) findViewById(R.id.textViewMap);
        textViewMembers = (TextView) findViewById(R.id.textViewMembers);
        textViewSchedule = (TextView) findViewById(R.id.textViewSchedule);


        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        textViewMap.setTextColor(getResources().getColor(R.color.colorPrimary));
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:

                        textViewMap.setTextColor(getResources().getColor(R.color.black));

                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        getSupportActionBar().setTitle("Map");
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });


        members.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        textViewMembers.setTextColor(getResources().getColor(R.color.colorPrimary));


                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:

                        textViewMembers.setTextColor(getResources().getColor(R.color.black));

                        Log.v("supervisor:",MyApplication.currentGroup.getName());


                        if ( MyApplication.currentGroup.getSupervisor().getUsername().equals(MyApplication.myIdentity.getUsername()))
                        {
                            Intent i = new Intent(getApplicationContext(), MembersFragmentActivity.class);
                            startActivity(i);
                            getSupportActionBar().setTitle("Members");

                        }
                        else
                        {
                            Log.v("TAG","I'm not the supervisor of current group");
                            Intent i = new Intent(getApplicationContext(), MmeberOnlyActivity.class);
                            startActivity(i);
                            getSupportActionBar().setTitle("Members");
                        }

                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });


        chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textViewChat.setTextColor(getResources().getColor(R.color.colorPrimary));

                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:

                        textViewChat.setTextColor(getResources().getColor(R.color.black));

                        Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                        startActivity(intent);
                        getSupportActionBar().setTitle("Chat");
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });


        schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        textViewSchedule.setTextColor(getResources().getColor(R.color.colorPrimary));
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:

                        textViewSchedule.setTextColor(getResources().getColor(R.color.black));
                        // RELEASED
                        if (MyApplication.myIdentity.getUsername().equals(MyApplication.currentGroup.getSupervisor().getUsername())) {
                            showDatePicker();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "You're not the supervisor of this group",Toast.LENGTH_LONG).show();
                        }

                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });

    }



    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
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
                new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.FRANCE);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd.MM.yyyy", Locale.FRANCE);
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


                        MyApplication.dateOfCurrentProgram=new smartcity.begrouped.model.Date(choosen.get(Calendar.DAY_OF_MONTH),choosen.get(Calendar.MONTH)+1,choosen.get(Calendar.YEAR));
                        dialog.dismiss();

                        POIManager.getDayProgramOfGroupByTask(MyApplication.dateOfCurrentProgram, MyApplication.currentGroup.getName(), GroupHomeFragment.this);

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

    public void loadSchedule(LinkedList<POI> listPOI) {
        MyApplication.listOfCurrentPOIS=POIManager.sortPOIByTime(listPOI);
        Intent i = new Intent(getApplicationContext(), ScheduleFragmentActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        getSupportActionBar().setTitle("Schedule");
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Intent i = null;
        String title = "Home";
        switch (position) {
            case 0:
                title = "Home";
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 1:
                title = "Current Group";
                i = new Intent(getApplicationContext(), GroupHomeFragment.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);

                break;
            case 2:
                title = "Map";
                i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 3:
                title = "Members";
                i = new Intent(getApplicationContext(), MembersFragmentActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 4:
                title = "Chat";
                i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);

                break;
            case 5:
                title = "Schedule";
                i = new Intent(getApplicationContext(), ScheduleFragmentActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            default:
                break;
        }
    }


}
