package smartcity.begrouped.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import smartcity.begrouped.R;
import smartcity.begrouped.adapter.MembersAdapter;

public class MembersFragmentActivity extends Fragment {
    MembersAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    View rootView;
    //private FragmentActivity myContext;

    public MembersFragmentActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), " Salut !", Toast.LENGTH_LONG).show();
        mAdapter = new MembersAdapter(getFragmentManager());
        View rootView = inflater.inflate(R.layout.fragment_members, container, false);
        mPager = (ViewPager)rootView.findViewById(R.id.pager1);
        mPager.setAdapter(mAdapter);
        mIndicator = (TitlePageIndicator)rootView.findViewById(R.id.indicator1);
        mIndicator.setViewPager(mPager);

        return rootView;

    }

}