package smartcity.begrouped.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import smartcity.begrouped.R;
import smartcity.begrouped.adapter.MembersAdapter;

public class ScheduleFragmentActivity extends Fragment {
    MembersAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    //private FragmentActivity myContext;

    public ScheduleFragmentActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MembersAdapter(getActivity().getSupportFragmentManager());

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