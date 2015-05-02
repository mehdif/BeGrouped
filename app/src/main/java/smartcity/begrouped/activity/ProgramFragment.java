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
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class ProgramFragment extends Fragment {

    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
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
        /*
        //Get the listview
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);
        //Array of data to fill in the list
        listItem = new ArrayList<HashMap<String, String>>();
        LinkedList<Group> mygroups= GroupManager.getGroups();
        for(int i=0;i<mygroups.size();i++) {
            Group group= mygroups.get(i);
            Log.v("group", group.toString());
            map = new HashMap<String, String>();
            // recuperer les données du superviseur
            String supervisorname=group.getSupervisor().getUsername();
            Object object= UserManager.getUserFromUserName(supervisorname);
            if ( object instanceof User) {
                User user=(User) object;
                group.getSupervisor().setFirstname(user.getFirstname());
                group.getSupervisor().setLastname(user.getLastname());
                group.getSupervisor().setPhoneNumber(user.getPhoneNumber());
            }
            map.put(TAG_NAME,group.getName());
            map.put(TAG_TYPE,group.getLocationName());
            map.put(TAG_TEMPS,group.getSupervisor().getUsername());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            listItem.add(map);
        }
        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                /// récupérer les infos sur le groupe
                MyApplication.currentGroup= GroupManager.getGroupMembersFromName(map.get(TAG_NAME));

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GroupHomeFragment fragment = new GroupHomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

            }
        });

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_NAME,TAG_TYPE,TAG_TEMPS}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
    */
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
