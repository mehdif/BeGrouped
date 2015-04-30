package smartcity.begrouped.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import smartcity.begrouped.R;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button join;
    private Button create;
    private Button manage;

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
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ManageGroupFragment fragment = new ManageGroupFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.commit();
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

    @Override
    public void onClick(View v) {


    }
}
