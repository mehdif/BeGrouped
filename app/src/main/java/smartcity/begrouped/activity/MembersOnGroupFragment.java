package smartcity.begrouped.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.CallService;
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class MembersOnGroupFragment extends Fragment implements AsyncResponse {

    ListView membersView;
    public static ArrayList<HashMap<String, String>> listItem;//array of items
    public static SimpleAdapter mSchedule=null;
    private ProgressDialog progressDialog;

    String username;
    String action="";

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
        listItem = new ArrayList<>();

        //showProgress();

        action="members";
        launch();
        //GroupManager.getGroupMembersFromName(groupname, this, Constants.MEMBERS_ON_GROUP_ONCREATE);

        membersView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final HashMap<String, String> map =
                        (HashMap<String, String>) membersView.getItemAtPosition(position);

                if (MyApplication.myIdentity.getUsername().equals(MyApplication.currentGroup.getSupervisor().getUsername())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete " + map.get("username") + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    action = "deleteMember";
                                    username=map.get("username");
                                    launch();
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

        membersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HashMap<String, String> map =
                        (HashMap<String, String>) membersView.getItemAtPosition(position);
                if (!ParseUser.getCurrentUser().getUsername().equals(map.get("username"))) {
                                    Intent intent = new Intent(getActivity(), CallActivity.class);
                                    intent.putExtra("callerId", MyApplication.currentUserId);
                                    intent.putExtra("recipientName", map.get("username"));
                                    startActivity(intent);
                                }


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

    public void launch() {
        try {
            String encodedName = URLEncoder.encode(MyApplication.currentGroup.getName(), "utf-8").replace("+", "%20");
            Downloader downloader = new Downloader(getActivity(), MembersOnGroupFragment.this);
            switch (action) {
                case "members":
                    downloader.execute(AllUrls.GET_GROUP_MEMBERS + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
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

    public void getGroupMembersOnCreate(Group group){

        final LinkedList<User> members=group.getMembers();
        listItem=new ArrayList<>();
        Log.v("ongroup",String.valueOf(members.size()));
        for(int i=0; i<members.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",members.get(i).getUsername());
            map.put("telephone",members.get(i).getPhoneNumber());
            //map.put("img", String.valueOf(R.drawable.user));//Ici l icone qui va s'afficher
            map.put("img", String.valueOf(R.drawable.user));
            map.put("flname",members.get(i).getLastname()+" "+ members.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }
        //if (mSchedule==null){
        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                    new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
        membersView.setAdapter(mSchedule);
        //}

        mSchedule.notifyDataSetChanged();
        //hideProgress();
    }

    public void getGroupMembersReload(Group group){
        final LinkedList<User> members=group.getMembers();
        listItem=new ArrayList<>();
        Log.v("ongroup",String.valueOf(members.size()));
        for(int i=0; i<members.size();i++)
        {
            HashMap map=new HashMap<String,String>();
            map.put("username",members.get(i).getUsername());
            map.put("telephone",members.get(i).getPhoneNumber());
            map.put("img", String.valueOf(R.drawable.user));//Ici l icone qui va s'afficher
            map.put("flname",members.get(i).getLastname()+" "+ members.get(i).getFirstname() );//Ici l icone qui va s'afficher
            listItem.add(map);
        }


        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", "username","telephone","flname"}, new int[] {R.id.img, R.id.titre, R.id.description,R.id.superviseur});
        membersView.setAdapter(mSchedule);

        mSchedule.notifyDataSetChanged();
        //hideProgress();
    }

    @Override
    public void executeAfterDownload(String output) {
        Log.v("hatem2", output);
        if (isNumeric(output.charAt(0))) {
            Toast.makeText(getActivity(), MessageUser.get(output), Toast.LENGTH_SHORT).show();
        } else {
            LinkedList<User> members = GroupManager.parseGroupMembers(output);
            MyApplication.currentGroup.setMembers(members);
            listItem=new ArrayList<>();
            for(int i=0; i<members.size();i++)
            {
                HashMap map=new HashMap<String,String>();
                map.put("username",members.get(i).getUsername());
                map.put("telephone",members.get(i).getPhoneNumber());
                map.put("img", String.valueOf(R.drawable.user));//Ici l icone qui va s'afficher
                map.put("flname",members.get(i).getLastname()+" "+ members.get(i).getFirstname() );//Ici l icone qui va s'afficher
                listItem.add(map);
            }
            //if (mSchedule ==null) {
            mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                        new String[]{"img", "username", "telephone", "flname"}, new int[]{R.id.img, R.id.titre, R.id.description, R.id.superviseur});
            membersView.setAdapter(mSchedule);
            //}
            mSchedule.notifyDataSetChanged();
        }
        action = "";
    }
}
