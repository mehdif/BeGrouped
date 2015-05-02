package smartcity.begrouped.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
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
}
