package smartcity.begrouped.activity;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
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
import smartcity.begrouped.utils.Constants;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class ManageGroupFragment extends Fragment implements AsyncResponse {

    private Fragment context;
    private ListView maListViewPerso;
    private ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private static final String TAG_GROUP_NAME = "Tassarkolat";
    private static final String TAG_REGION = "Lyon";
    private static final String TAG_SUPERVISEUR = "Hassan";

    String action = "";

    public ManageGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        context = this;
        View rootView = inflater.inflate(R.layout.fragment_manage_group, container, false);
        //Get the listview
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);

        listItem = MyApplication.listItem;

        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                action = "groupInformation";
                HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                try {
                    String encodedName = URLEncoder.encode(map.get(TAG_GROUP_NAME), "utf-8").replace("+", "%20");
                    Downloader downloader = new Downloader(getActivity(), ManageGroupFragment.this);
                    downloader.execute(AllUrls.GET_GROUP_ALL_INFORMATIONS + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getActivity(), MessageUser.get("0000"), Toast.LENGTH_SHORT).show();
                }

            }
        });


        maListViewPerso.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);

                if (MyApplication.myIdentity.getUsername().equals(map.get(TAG_SUPERVISEUR))) {
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
                } else if (!MyApplication.myIdentity.getUsername().equals(map.get(TAG_SUPERVISEUR))) {
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
                new String[]{"img", TAG_GROUP_NAME, TAG_REGION, TAG_SUPERVISEUR}, new int[]{R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
        return rootView;

    }

    @Override
    public void executeAfterDownload(String output) {
        Log.v("adnane",output);
        if (action.equals("groupInformation")) {
            if (isNumeric(output.charAt(0))) {
                Toast.makeText(getActivity(), MessageUser.get(output), Toast.LENGTH_SHORT).show();
            } else {
                Group group=GroupManager.parseGroupAllInfo(output);
                Intent i = new Intent(getActivity().getApplicationContext(), GroupHomeFragment.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Home");
            }
        }
        action = "";
    }
}