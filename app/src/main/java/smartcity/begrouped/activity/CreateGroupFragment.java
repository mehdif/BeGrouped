package smartcity.begrouped.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import smartcity.begrouped.R;


public class CreateGroupFragment extends Fragment {

    private Button create;
    private Button cancel;

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

        create = (Button) rootView.findViewById(R.id.buttonAddGroup);
        cancel = (Button) rootView.findViewById(R.id.buttonCancel);

        create.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View view) {
        Intent intent = new Intent(getActivity(), MainActivity.class);//HAVE TO REDIRECT TO ManageGroupFragment
        startActivity(intent);
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
}