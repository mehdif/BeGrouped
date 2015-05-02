package smartcity.begrouped.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smartcity.begrouped.R;
import smartcity.begrouped.adapter.MembersAdapter;
import smartcity.begrouped.model.Group;

public class MembersFragmentActivity extends Fragment {
    MembersAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    //private FragmentActivity myContext;

    public MembersFragmentActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mycontext.getSupportFragmentManager()
        //getFragmentManager()
         mAdapter = new MembersAdapter(getActivity().getSupportFragmentManager());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_members, container, false);
        mPager = (ViewPager)rootView.findViewById(R.id.pager1);
        mPager.setAdapter(mAdapter);

        mIndicator = (TitlePageIndicator)rootView.findViewById(R.id.indicator1);
        mIndicator.setViewPager(mPager);

        return rootView;

    }

    //@Override
    //public void onAttach(Activity activity) {
    //    myContext=(FragmentActivity) activity;
    //    super.onAttach(activity);
    //}

}