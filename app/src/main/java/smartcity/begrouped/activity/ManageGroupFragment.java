package smartcity.begrouped.activity;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MyApplication;


public class ManageGroupFragment extends Fragment {

    private ListView maListViewPerso;
    private ProgressDialog progressDialog;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_group, container, false);
        //Get the listview
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);

        listItem = MyApplication.listItem;

        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                /// récupérer les infos sur le groupe
                Log.v("groupname",map.get(TAG_GROUP_NAME));
                Group group=new Group(null,null,null,map.get(TAG_GROUP_NAME),null);
                group= GroupManager.callTaskGetGroupInformation(group);
                Object object= UserManager.getUserFromUserName(group.getSupervisor().getUsername());
                if ( object instanceof User) {
                    User user=(User) object;
                    group.getSupervisor().setFirstname(user.getFirstname());
                    group.getSupervisor().setLastname(user.getLastname());
                    group.getSupervisor().setPhoneNumber(user.getPhoneNumber());
                }

                Log.v("super",group.getSupervisor().toString());
                Group group1=GroupManager.getGroupMembersFromName(group.getName());
                group.setMembers(group1.getMembers());

                MyApplication.currentGroup=group;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                GroupHomeFragment fragment = new GroupHomeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

            }
        });


        maListViewPerso.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);

                if ( MyApplication.myIdentity.getUsername().equals(map.get(TAG_SUPERVISEUR))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete " + map.get(TAG_GROUP_NAME) + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    boolean result = GroupManager.callTaskDeleteGroup(map.get(TAG_GROUP_NAME));
                                    if (result) {
                                        Toast.makeText(getActivity(), "Group deleted with success", Toast.LENGTH_LONG).show();
                                        getActivity().recreate();
                                    } else
                                        Toast.makeText(getActivity(), "There is a problem with deleting this group", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if ( ! MyApplication.myIdentity.getUsername().equals(map.get(TAG_SUPERVISEUR))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to leave " + map.get(TAG_GROUP_NAME) + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    boolean result = GroupManager.callTaskLeaveGroup(map.get(TAG_GROUP_NAME));
                                    if (result) {
                                        Toast.makeText(getActivity(), "Group left successfully", Toast.LENGTH_LONG).show();
                                        getActivity().recreate();
                                    } else
                                        Toast.makeText(getActivity(), "There is a problem with leaving this group", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
            }
        });






        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_GROUP_NAME,TAG_REGION,TAG_SUPERVISEUR}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
        return rootView;

    }
}