package smartcity.begrouped.activity;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class ManageGroupFragment extends Fragment {

    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private static final String TAG_GROUP_NAME = "Tassarkolat";
    private static final String TAG_REGION = "Lyon";
    private static final String TAG_SUPERVISEUR = "Hassan";

    public ManageGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_group, container, false);
        //Get the listview
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);
        //Array of data to fill in the list
        listItem = new ArrayList<HashMap<String, String>>();
        LinkedList<Group> mygroups=GroupManager.getGroups();
        for(int i=0;i<mygroups.size();i++) {
            Group group=mygroups.pop();
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
            map.put(TAG_GROUP_NAME,group.getName());
            map.put(TAG_REGION,group.getLocationName());
            map.put(TAG_SUPERVISEUR,group.getSupervisor().getUsername());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            listItem.add(map);
        }
        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);

                /// récupérer les infos sur le groupe

                MyApplication.currentGroup= GroupManager.getGroupMembersFromName("algertour");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GroupHomeFragment fragment = new GroupHomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

            }
        });

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_GROUP_NAME,TAG_REGION,TAG_SUPERVISEUR}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
        return rootView;

    }
}