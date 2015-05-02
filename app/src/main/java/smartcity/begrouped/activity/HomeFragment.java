package smartcity.begrouped.activity;


import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MyApplication;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button join;
    private Button create;
    private Button manage;

    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private static final String TAG_GROUP_NAME = "Tassarkolat";
    private static final String TAG_REGION = "Lyon";
    private static final String TAG_SUPERVISEUR = "Hassan";
    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        join = (Button) rootView.findViewById(R.id.join);
        create = (Button) rootView.findViewById(R.id.nouveau);
        manage = (Button) rootView.findViewById(R.id.manage);

        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Join Group");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                JoinGroupFragment fragment = new JoinGroupFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

            }
        });

        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Create Group");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreateGroupFragment fragment = new CreateGroupFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();

                //fragmentTransaction.addToBackStack(null);

            }
        });

        manage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Manage Group");

                showProgress();

                GroupManager.getGroups(HomeFragment.this);
            }
        });
        return rootView;
    }

    public void getGroup(LinkedList<Group> mygroups){

        //Array of data to fill in the list
        listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<mygroups.size();i++) {
            Group group= mygroups.get(i);
            Log.v("group", group.toString());
            map = new HashMap<String, String>();


            // recuperer les données du superviseur
            //String supervisorname=group.getSupervisor().getUsername();

/*            Object object= UserManager.getUserFromUserName(supervisorname);
            if ( object instanceof User) {
                User user=(User) object;
                group.getSupervisor().setFirstname(user.getFirstname());
                group.getSupervisor().setLastname(user.getLastname());
                group.getSupervisor().setPhoneNumber(user.getPhoneNumber());
            }*/
            map.put(TAG_GROUP_NAME,group.getName());
            map.put(TAG_REGION,group.getLocationName());
            map.put(TAG_SUPERVISEUR,group.getSupervisor().getUsername());
            map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
            listItem.add(map);
        }

        MyApplication.listItem = listItem;

        hideProgress();

        //Calling the next fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ManageGroupFragment fragment = new ManageGroupFragment();
        fragmentTransaction.replace(R.id.container_body, fragment,"tag");
        fragmentTransaction.commit();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void showProgress(){
        progressDialog = ProgressDialog.show(getActivity(), null,
                "Loading...", true);
    }

    public void hideProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {


    }
}
