package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.model.Group;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class CreateGroupFragment extends Fragment implements AsyncResponse {

    private Button create;
    private Button cancel;
    private EditText groupName;
    private EditText regionName;
    private ProgressDialog progressDialog;
    private TextView textViewNewGroup;

    String action = "";


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

        groupName = (EditText) rootView.findViewById(R.id.editTextGroupName);
        regionName = (EditText) rootView.findViewById(R.id.editTextRegionName);

        create = (Button) rootView.findViewById(R.id.buttonAddGroup);
        cancel = (Button) rootView.findViewById(R.id.buttonCancel);

        textViewNewGroup = (TextView) rootView.findViewById(R.id.textViewNewGroup);
        textViewNewGroup.setText(Html.fromHtml("<font color=#c62828>New</font> <font color=#000000>Group</font>"));

        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (groupName.getText().toString().isEmpty() || regionName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), MessageUser.get("1210"), Toast.LENGTH_SHORT).show();
                } else {
                    action = "create";
                    //showProgress();
                    try {
                        String encodedName = URLEncoder.encode(groupName.getText().toString(), "utf-8").replace("+", "%20");
                        String encodedRegion = URLEncoder.encode(regionName.getText().toString(), "utf-8").replace("+", "%20");
                        Downloader downloader = new Downloader(getActivity(), CreateGroupFragment.this);
                        downloader.execute(AllUrls.CREATE_GROUP + encodedName + "/" + encodedRegion + "/" + MyApplication.myIdentity.getUsername() + "/" + MyApplication.myIdentity.getPassword());
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(getActivity(), MessageUser.get("0000"), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
            Toast.makeText(getActivity(), MessageUser.get(output), Toast.LENGTH_SHORT).show();
        } else {
            Group group = GroupManager.parseGroup(output);
            Toast.makeText(getActivity(), MessageUser.get("2202"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);//HAVE TO REDIRECT TO ManageGroupFragment
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        action = "";
    }
}