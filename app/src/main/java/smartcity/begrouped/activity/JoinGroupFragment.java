package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.GlobalMethodes;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class JoinGroupFragment extends Fragment implements AsyncResponse {

    private Button cancel;
    private Button join;
    private EditText groupNameEditText;

    private String groupname;

    private String action="";
    private TextView textViewJoinGroup;


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

        textViewJoinGroup = (TextView) rootView.findViewById(R.id.textViewJoinGroup);
        textViewJoinGroup.setText(Html.fromHtml("<font color=#c62828>Join</font> <font color=#000000>Group</font>"));

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
        if (isNumeric(output.charAt(0))) {
            Toast.makeText(getActivity(), MessageUser.get(output),Toast.LENGTH_SHORT).show();
        }
        else {
            Group group=GroupManager.parseGroup(output);
            String supervisorname=group.getSupervisor().getUsername();
            Log.v("aimen",supervisorname);

            GlobalMethodes.sendNotification("New Invitation",MyApplication.myIdentity.getUsername() +" want to join "+group.getName(),supervisorname);


        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        action="";
    }
}