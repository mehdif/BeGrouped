package smartcity.begrouped.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class MembersOnGroupFragment extends Fragment {

    ListView membersView;
    public static ArrayList<HashMap<String, String>> listItem;//array of items
    public static SimpleAdapter mSchedule;

    public MembersOnGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_members_on_group, container, false);
        Log.v("oncreateview","oncreateview");

        membersView = (ListView) rootView.findViewById(R.id.listView1);
        listItem = new ArrayList<HashMap<String, String>>();
        String groupname=MyApplication.currentGroup.getName();
        Group group=GroupManager.getGroupMembersFromName(groupname);
        final LinkedList<User> members=group.getMembers();
        Log.v("ongroup",String.valueOf(members.size()));
        for(int i=0; i<members.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",members.get(i).getUsername());
            map.put("telephone",members.get(i).getPhoneNumber());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            map.put("flname",members.get(i).getLastname()+" "+ members.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }

        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
        new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
        membersView.setAdapter(mSchedule);



        membersView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final HashMap<String, String> map = (HashMap<String, String>) membersView.getItemAtPosition(position);

                if (MyApplication.myIdentity.getUsername().equals(MyApplication.currentGroup.getSupervisor().getUsername())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete " + map.get("username") + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    boolean result = GroupManager.callTaskDeleteMember(MyApplication.currentGroup.getName(), map.get("username"));
                                    if (result) {
                                        Toast.makeText(getActivity(), "Member deleted with success", Toast.LENGTH_LONG).show();
                                        reload();
                                       SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                                                new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
                                       membersView.setAdapter(mSchedule);


                                    } else
                                        Toast.makeText(getActivity(), "There is a problem with deleting this member", Toast.LENGTH_LONG).show();

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
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Toast.makeText(getActivity(), "OnAttach", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public   void reload()
    {
        listItem = new ArrayList<HashMap<String, String>>();
        String groupname=MyApplication.currentGroup.getName();
        Group group=GroupManager.getGroupMembersFromName(groupname);
        final LinkedList<User> members=group.getMembers();
        Log.v("ongroup",String.valueOf(members.size()));
        for(int i=0; i<members.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",members.get(i).getUsername());
            map.put("telephone",members.get(i).getPhoneNumber());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            map.put("flname",members.get(i).getLastname()+" "+ members.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }

    }
}
