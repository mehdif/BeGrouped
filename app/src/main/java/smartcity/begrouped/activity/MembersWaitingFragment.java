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


public class MembersWaitingFragment extends Fragment {
    ListView membersWaitingView;
    ArrayList<HashMap<String, String>> listItem;//array of items

    public MembersWaitingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("waiting", "waiting");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_members_waiting, container, false);
        membersWaitingView = (ListView) rootView.findViewById(R.id.listView1);
        listItem = new ArrayList<HashMap<String, String>>();
        LinkedList<User> membersWaiting= GroupManager.callTaskGetPendingDemands(MyApplication.currentGroup.getName());

        for(int i=0; i<membersWaiting.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",membersWaiting.get(i).getUsername());
            map.put("telephone",membersWaiting.get(i).getPhoneNumber());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            map.put("flname",membersWaiting.get(i).getLastname()+" "+ membersWaiting.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }

        membersWaitingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                final HashMap<String, String> map = (HashMap<String, String>) membersWaitingView.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to accept " + map.get("username") + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                boolean result = GroupManager.callTaskAcceptMember(MyApplication.currentGroup.getName(),map.get("username"));
                                if (result) {
                                    Toast.makeText(getActivity(), "Member added with success", Toast.LENGTH_LONG).show();
                                    reload();
                                    SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                                            new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
                                    membersWaitingView.setAdapter(mSchedule);
                                } else
                                    Toast.makeText(getActivity(), "There is a problem with adding this member", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
        membersWaitingView.setAdapter(mSchedule);
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
    public void reload()
    {

        listItem = new ArrayList<HashMap<String, String>>();
        LinkedList<User> membersWaiting= GroupManager.callTaskGetPendingDemands(MyApplication.currentGroup.getName());

        for(int i=0; i<membersWaiting.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",membersWaiting.get(i).getUsername());
            map.put("telephone",membersWaiting.get(i).getPhoneNumber());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            map.put("flname",membersWaiting.get(i).getLastname()+" "+ membersWaiting.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }

    }
}
