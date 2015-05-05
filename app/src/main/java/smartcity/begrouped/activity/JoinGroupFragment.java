package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;


public class JoinGroupFragment extends Fragment implements AsyncResponse {

    private Button cancel;
    private Button join;
    private EditText groupNameEditText;

    private String groupname;

    private String action="";


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
        groupNameEditText = (EditText) rootView.findViewById(R.id.editTextGroupName);

        join.setOnClickListener(new View.OnClickListener() {

         @Override
        public void onClick(View view) {
             getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
             groupname=groupNameEditText.getText().toString();

             if (groupname.isEmpty()) {
                 Toast.makeText(getActivity(), MessageUser.get("1209"), Toast.LENGTH_SHORT).show();
             }
             else {
                 action="join";
                 try {
                     String encodedName = URLEncoder.encode(groupname, "utf-8").replace("+", "%20");
                     Downloader downloader = new Downloader(getActivity(), JoinGroupFragment.this);
                     downloader.execute(AllUrls.ASK_JOIN_GROUP + encodedName + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                 } catch (UnsupportedEncodingException e) {
                     Toast.makeText(getActivity(), MessageUser.get("0000"),Toast.LENGTH_SHORT).show();
                 }
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

    @Override
    public void executeAfterDownload(String output) {
        Toast.makeText(getActivity(), MessageUser.get(output),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        action="";
    }
}