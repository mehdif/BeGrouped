package smartcity.begrouped.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Appointment;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class ProgramFragment extends Fragment {

    private ListView maListViewPerso1;
    public static ArrayList<HashMap<String, String>> listItem1;//array of items
    HashMap<String, String> map;//single item data
    public static SimpleAdapter mSchedule1;
    private static final String TAG_NAME = "First day";
    private static final String TAG_TYPE = "hang out";
    private static final String TAG_TEMPS = "HH::MM";

    public ProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);

        Button save=(Button)rootView.findViewById(R.id.buttonSave);
        Button cancel=(Button)rootView.findViewById(R.id.buttonCancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POIManager.initDayGroupProgramByTask(MyApplication.dateOfCurrentProgram,MyApplication.currentGroup.getName());
                POIManager.saveDayGroupProgramByTask(MyApplication.dateOfCurrentProgram,MyApplication.currentGroup.getName(),MyApplication.listOfCurrentPOIS);
                /*
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }*/

                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GroupHomeFragment fragment = new GroupHomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GroupHomeFragment fragment = new GroupHomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();
            }
        });


        //Get the listview
        maListViewPerso1 = (ListView) rootView.findViewById(R.id.listView1);
        //Array of data to fill in the list
        listItem1 = new ArrayList<HashMap<String, String>>();
        //LinkedList<Group> mygroups= GroupManager.getGroups();
        LinkedList<POI> myPOIs= POIManager.getDayProgramOfGroupByTask(MyApplication.dateOfCurrentProgram,MyApplication.currentGroup.getName());
        if (myPOIs==null) myPOIs=new LinkedList<>();
        for(int i=0;i<myPOIs.size();i++) {
            POI poi= myPOIs.get(i);
            Log.v("group", poi.toString());
            map = new HashMap<String, String>();
            // recuperer les données du superviseur
            map.put(TAG_NAME,poi.getName());
            map.put(TAG_TYPE,poi.getType());
            map.put(TAG_TEMPS,poi.getTempsOfVisite().afficher());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            listItem1.add(map);
        }
        MyApplication.listOfCurrentPOIS=myPOIs;
        maListViewPerso1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                afficherDialogRDV(position);

            }
        });

        mSchedule1 = new SimpleAdapter(getActivity(), listItem1, R.layout.affichageitem,
                new String[] {"img", TAG_NAME,TAG_TYPE,TAG_TEMPS}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso1.setAdapter(mSchedule1);

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
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View alertDialogView = factory.inflate(R.layout.alertdialog_programpoi, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(alertDialogView);
        adb.setTitle("Destination Detail");
        adb.setIcon(android.R.drawable.ic_dialog_info);


        TextView name = (TextView)alertDialogView.findViewById(R.id.name);
        TextView type = (TextView)alertDialogView.findViewById(R.id.type);
        TextView detailType = (TextView)alertDialogView.findViewById(R.id.typedetail);
        TextView address = (TextView)alertDialogView.findViewById(R.id.address);
        TextView phone = (TextView)alertDialogView.findViewById(R.id.phone);
        TextView temps = (TextView)alertDialogView.findViewById(R.id.timeVisit);
        name.setText(MyApplication.listOfCurrentPOIS.get(position).getName());
        type .setText(MyApplication.listOfCurrentPOIS.get(position).getType());
        detailType .setText(MyApplication.listOfCurrentPOIS.get(position).getTypeDetail());
        address .setText(MyApplication.listOfCurrentPOIS.get(position).getAddres());
        phone .setText(MyApplication.listOfCurrentPOIS.get(position).getPhone());
        temps .setText(MyApplication.listOfCurrentPOIS.get(position).getTempsOfVisite().afficher());



        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        adb.show();

    }




}
