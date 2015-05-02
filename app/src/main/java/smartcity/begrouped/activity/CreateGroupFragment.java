package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.model.Group;


public class CreateGroupFragment extends Fragment {

    private Button create;
    private Button cancel;
    private EditText groupName;
    private EditText regionName;
    private ProgressDialog progressDialog;


    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupName=(EditText) rootView.findViewById(R.id.editTextGroupName);
        regionName=(EditText) rootView.findViewById(R.id.editTextRegionName);

        create = (Button) rootView.findViewById(R.id.buttonAddGroup);
        cancel = (Button) rootView.findViewById(R.id.buttonCancel);

        create.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            showProgress();
            GroupManager.callTaskCreateNewGroup(groupName.getText().toString(), regionName.getText().toString(), CreateGroupFragment.this);

            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            });


        return rootView;
    }

    public void createGroup(Group group){

        if(groupName.getText().toString().matches("")){
            Toast.makeText(getActivity().getBaseContext(), "Please enter a group name !", Toast.LENGTH_SHORT).show();
        }
        else if(regionName.getText().toString().matches("")){
            Toast.makeText(getActivity().getBaseContext(), "Please enter a region name !", Toast.LENGTH_SHORT).show();
        }
        else {
            if(group == null){
                Toast.makeText(getActivity().getBaseContext(), "This group already exists !", Toast.LENGTH_SHORT).show();
            }
        }

        hideProgress();
        Intent intent = new Intent(getActivity(), MainActivity.class);//HAVE TO REDIRECT TO ManageGroupFragment
        startActivity(intent);
    }

    public void showProgress(){
        progressDialog = ProgressDialog.show(this.getActivity(), null,
                "Loading...", true);
    }

    public void hideProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}