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
import java.util.Locale;

import smartcity.begrouped.R;
import smartcity.begrouped.utils.MyApplication;


public class GroupHomeFragment extends Fragment {

    private ImageButton map;
    private ImageButton members;
    private ImageButton chat;
    private ImageButton schedule;
    private ProgressDialog progressDialog;

    public GroupHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_home, container, false);

        map = (ImageButton) rootView.findViewById(R.id.imageButton2);
        members = (ImageButton) rootView.findViewById(R.id.imageButtonMembers);
        chat = (ImageButton) rootView.findViewById(R.id.imageButtonChat);
        schedule = (ImageButton) rootView.findViewById(R.id.imageButtonSched);

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //map.setImageDrawable(getResources().getDrawable(R.drawable.map_red));
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                       // map.setImageDrawable(getResources().getDrawable(R.drawable.map));
                        // RELEASED
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });

        members.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.v("supervisor:",MyApplication.currentGroup.getName());


                   if ( MyApplication.currentGroup.getSupervisor().getUsername().equals(MyApplication.myIdentity.getUsername()))
                    {
                        Log.v("TAG","I'm the supervisor of current group");
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MembersFragmentActivity fragment = new MembersFragmentActivity();
                        fragmentTransaction.replace(R.id.container_body, fragment, "tag");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                else
                    {
                        Log.v("TAG","I'm not the supervisor of current group");
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MembersOnGroupFragment fragment = new MembersOnGroupFragment();
                        fragmentTransaction.replace(R.id.container_body, fragment, "tag");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

            }
        });
/*
        members.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        members.setImageDrawable(getResources().getDrawable(R.drawable.group_red));

                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        members.setImageDrawable(getResources().getDrawable(R.drawable.group));
                        // RELEASED
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });
*/
        chat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);

            }
        });
 /*       chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //chat.setImageDrawable(getResources().getDrawable(R.drawable.chat_red));
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        //chat.setImageDrawable(getResources().getDrawable(R.drawable.chat));
                        // RELEASED
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });
*/
        schedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
  /*      schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //schedule.setImageDrawable(getResources().getDrawable(R.drawable.agenda_red));
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        //schedule.setImageDrawable(getResources().getDrawable(R.drawable.agenda));
                        // RELEASED
                        return true; // if you want to handle the touch event
                }

                return false;
            }
        });
*/

        return rootView;
    }


    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(getActivity());
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

                        Toast.makeText(getActivity(), dateViewFormatter.format(choosen.getTime()),Toast.LENGTH_LONG).show();
                        MyApplication.dateOfCurrentProgram=new smartcity.begrouped.model.Date(choosen.get(Calendar.DAY_OF_MONTH),choosen.get(Calendar.MONTH)+1,choosen.get(Calendar.YEAR));
                        dialog.dismiss();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ScheduleFragmentActivity fragment = new ScheduleFragmentActivity();
                        fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
}
