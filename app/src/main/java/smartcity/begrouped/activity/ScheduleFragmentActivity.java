package smartcity.begrouped.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import smartcity.begrouped.R;
import smartcity.begrouped.adapter.MembersAdapter;
import smartcity.begrouped.adapter.ScheduleAdapter;

public class ScheduleFragmentActivity extends Fragment {
    ScheduleAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    //private FragmentActivity myContext;
    //private FragmentActivity myContext;

    public ScheduleFragmentActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myContext=new FragmentActivity();
        mAdapter = new ScheduleAdapter(getActivity().getSupportFragmentManager());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mPager = (ViewPager)rootView.findViewById(R.id.pager1);
        mPager.setAdapter(mAdapter);

        mIndicator = (TitlePageIndicator)rootView.findViewById(R.id.indicator1);
        mIndicator.setViewPager(mPager);

        return rootView;

    }



}