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
import smartcity.begrouped.utils.MessageUser;


public class JoinGroupFragment extends Fragment {

    private Button cancel;
    private Button join;
    private EditText groupName;
    private ProgressDialog progressDialog;

    public JoinGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join_group, container, false);

        join = (Button) rootView.findViewById(R.id.buttonJoin);
        cancel = (Button) rootView.findViewById(R.id.buttonCancel);
        groupName = (EditText) rootView.findViewById(R.id.editTextGroupName);

        join.setOnClickListener(new View.OnClickListener() {

         @Override
        public void onClick(View view) {
             getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

             if (groupName.getText().toString().isEmpty()) {
                 Toast.makeText(getActivity(), MessageUser.get("1209"), Toast.LENGTH_SHORT).show();
             }
             else {
                 showProgress();
                 GroupManager.callTaskJoinGroup(groupName.getText().toString(), JoinGroupFragment.this);
             }
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

    public void joinGroup(String message){

        Toast.makeText(getActivity(), MessageUser.get(message),Toast.LENGTH_LONG).show();
        hideProgress();
        Intent intent = new Intent(getActivity(), MainActivity.class);
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