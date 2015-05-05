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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class MembersWaitingFragment extends Fragment implements AsyncResponse {
    ListView membersWaitingView;
    ArrayList<HashMap<String, String>> listItem;//array of items
    public static SimpleAdapter mSchedule=null;

    String username = "";
    String action = "";
    HashMap<String, String> actualMap;
    LinkedList<User> membersWaiting = new LinkedList<>();


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

        listItem = new ArrayList<>();

        action = "members";
        launch();
        membersWaitingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                actualMap = (HashMap<String, String>) membersWaitingView.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to accept " + actualMap.get("username") + " ?")
                        .setCancelable(false)
                        .setNegativeButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                action = "acceptMember";
                                username = actualMap.get("username");
                                launch();
                            }
                        })
                        .setNeutralButton("Reject", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        action = "deleteMember";
                                        username = actualMap.get("username");
                                        launch();
                                    }
                                }
                        )
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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

    public void launch() {
        try {
            String encodedName = URLEncoder.encode(MyApplication.currentGroup.getName(), "utf-8").replace("+", "%20");
            Downloader downloader = new Downloader(getActivity(), MembersWaitingFragment.this);
            switch (action) {
                case "members":
                    downloader.execute(AllUrls.GET_PENDING_DEMANDS + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                    break;
                case "acceptMember":
                    downloader.execute(AllUrls.ACCEPT_MEMBER_TO_GROUP + encodedName + "/" + username + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                    break;
                case "deleteMember":
                    downloader.execute(AllUrls.EXPULSER_GROUP_SUPERVISOR + encodedName + "/" + username + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                    break;
                default:
                    break;
            }

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getActivity(), MessageUser.get("0000"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void executeAfterDownload(String output) {

        Log.v("hatem", output);
        if (isNumeric(output.charAt(0))) {
            Toast.makeText(getActivity(), MessageUser.get(output), Toast.LENGTH_SHORT).show();
        } else {
            listItem = new ArrayList<>();
            LinkedList<User> ancientMembersWaiting = membersWaiting;
            membersWaiting = GroupManager.parsePendingDemands(output);
            listItem = new ArrayList<>();

            for (int i = 0; i < membersWaiting.size(); i++) {
                HashMap map = new HashMap<String, String>();
                // map.put("img", String.valueOf(R.drawable.user));//Ici l icone qui va s'afficher
                map.put("username", membersWaiting.get(i).getUsername());
                map.put("telephone", membersWaiting.get(i).getPhoneNumber());
                map.put("img", String.valueOf(R.drawable.user));
                map.put("flname", membersWaiting.get(i).getLastname() + " " + membersWaiting.get(i).getFirstname());//Ici l icone qui va s'afficher
                listItem.add(map);
            }
            mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                    new String[]{"img", "username", "telephone", "flname"}, new int[]{R.id.img, R.id.titre, R.id.description, R.id.superviseur});


            membersWaitingView.setAdapter(mSchedule);
            mSchedule.notifyDataSetChanged();


            if (action.equals("acceptMember")) {
                User user = GroupManager.getUserByUsernameFromListAndRemove(actualMap.get("username"),ancientMembersWaiting);
                MyApplication.currentGroup.getMembers().add(user);
                HashMap map2 = new HashMap<String, String>();
                map2.put("username", actualMap.get("username"));
                map2.put("telephone", actualMap.get("telephone"));
                map2.put("img", String.valueOf(R.drawable.user));//Ici l icone qui va s'afficher
                map2.put("flname", actualMap.get("flname"));//Ici l icone qui va s'afficher
                MembersOnGroupFragment.listItem.add(map2);
                MembersOnGroupFragment.mSchedule.notifyDataSetChanged();
            }
        }
        action = "";
    }
}
