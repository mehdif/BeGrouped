package smartcity.begrouped.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import smartcity.begrouped.R;


public class GroupHomeFragment extends Fragment {

    private ImageButton map;
    private ImageButton members;
    private ImageButton chat;
    private ImageButton schedule;

    public GroupHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_home, container, false);

        map = (ImageButton) rootView.findViewById(R.id.imageButton2);
        members = (ImageButton) rootView.findViewById(R.id.imageButtonMembers);
        chat = (ImageButton) rootView.findViewById(R.id.imageButtonChat);
        schedule = (ImageButton) rootView.findViewById(R.id.imageButtonSched);

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        members.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }




}
