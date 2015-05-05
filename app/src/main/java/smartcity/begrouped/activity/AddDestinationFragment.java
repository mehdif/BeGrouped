package smartcity.begrouped.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Date;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.model.Temps;
import smartcity.begrouped.utils.MyApplication;


public class AddDestinationFragment extends Fragment {

    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private SimpleAdapter mSchedule;
    private EditText search_query;
    private static final String TAG_NAME = "First day";
    private static final String TAG_TYPE = "hang out";
    private static final String TAG_TEMPS = "HH::MM";
    private LinkedList<POI> listPOI;
    public static EditText hhEdit;
    public static EditText mmEdit;
    public static int positionClicked;

    public AddDestinationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_destination, container, false);
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);
        listItem = new ArrayList<HashMap<String, String>>();
        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                afficherDialogRDV(position);

            }
        });
        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_NAME,TAG_TYPE,TAG_TEMPS}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
        search_query = (EditText) rootView.findViewById(R.id.search_input);

        search_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(getActivity(), "Looking for : "+search_query.getText(), Toast.LENGTH_LONG).show();

                    // Anes : Call your search function here : performSearch();
                    listPOI= POIManager.searchPOIByNameByTask(search_query.getText().toString());
                    listItem.clear();
                    if (listPOI==null) listPOI=new LinkedList<POI>();
                    for(int i=0;i<listPOI.size();i++) {
                        POI poi= listPOI.get(i);
                        Log.v("group", poi.toString());
                        map = new HashMap<String, String>();
                        // recuperer les données du superviseur
                        map.put(TAG_NAME,poi.getName());
                        map.put(TAG_TYPE,poi.getType().replace("_"," "));
                        map.put(TAG_TEMPS,"");
                        //map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
                        map.put("img", String.valueOf(R.drawable.monument_black));
                        listItem.add(map);
                    }
                    mSchedule.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void afficherDialogRDV(int position){
        positionClicked=position;
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View alertDialogView = factory.inflate(R.layout.alertdialog_poi, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(alertDialogView);
        adb.setTitle("Destination Detail");
        adb.setIcon(android.R.drawable.ic_dialog_info);


        TextView name = (TextView)alertDialogView.findViewById(R.id.name);
        TextView type = (TextView)alertDialogView.findViewById(R.id.type);
        TextView detailType = (TextView)alertDialogView.findViewById(R.id.typedetail);
        TextView address = (TextView)alertDialogView.findViewById(R.id.address);
        TextView phone = (TextView)alertDialogView.findViewById(R.id.phone);

        name.setText(listPOI.get(position).getName());
        type .setText(listPOI.get(position).getType().replace("_"," "));
        detailType .setText(listPOI.get(position).getTypeDetail());
        address .setText(listPOI.get(position).getAddres());
        phone .setText(listPOI.get(position).getPhone());




        adb.setNegativeButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showTimePicker();

            }
        });
        adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();

    }
    public static void updateListViewOfDay() {
        ProgramFragment.listItem1.clear();
        HashMap<String, String> map;
        for(int i=0;i<MyApplication.listOfCurrentPOIS.size();i++) {
            POI poi= MyApplication.listOfCurrentPOIS.get(i);
            Log.v("group", poi.toString());
            map = new HashMap<String, String>();
            // recuperer les données du superviseur
            map.put(TAG_NAME,poi.getName());
            map.put(TAG_TYPE,poi.getType().replace("_"," "));
            map.put(TAG_TEMPS,poi.getTempsOfVisite().afficher());
            map.put("img", String.valueOf(R.drawable.monument_black));//Ici l icone qui va s'afficher
            ProgramFragment.listItem1.add(map);
        }
        ProgramFragment.mSchedule1.notifyDataSetChanged();

    }

    private void showTimePicker() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(getActivity());
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
                        //dialog.dismiss();
                        POI poi=listPOI.get(positionClicked);
                        poi.setTempsOfVisite(temps);
                        POIManager.addPoiToSortedList(MyApplication.listOfCurrentPOIS,poi);
                        updateListViewOfDay();

                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        // Finish

        dialog.show();


    }

}


