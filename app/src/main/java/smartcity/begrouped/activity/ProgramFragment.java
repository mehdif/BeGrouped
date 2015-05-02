package smartcity.begrouped.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.POI;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class ProgramFragment extends Fragment {

    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private SimpleAdapter mSchedule;
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

        //Get the listview
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);
        //Array of data to fill in the list
        listItem = new ArrayList<HashMap<String, String>>();
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
            listItem.add(map);
        }
        MyApplication.listOfCurrentPOIS=myPOIs;
        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

              // MyApplication.currentGroup= GroupManager.getGroupMembersFromName(map.get(TAG_NAME));



            }
        });

        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_NAME,TAG_TYPE,TAG_TEMPS}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);

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
}
