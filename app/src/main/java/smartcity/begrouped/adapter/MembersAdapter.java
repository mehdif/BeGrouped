package smartcity.begrouped.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import smartcity.begrouped.activity.*;
import com.viewpagerindicator.IconPagerAdapter;
import smartcity.begrouped.activity.MembersWaitingFragment;

public class MembersAdapter extends FragmentPagerAdapter implements IconPagerAdapter{
    protected static final String[] CONTENT = new String[] {
            "This", "Is"
    };

    private int mCount = CONTENT.length;

    public MembersAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getIconResId(int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MembersOnGroupFragment();
        switch(position){
            case 0:
                fragment = new MembersOnGroupFragment();
                break;
            case 1:

                fragment = new MembersWaitingFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0:
                title = "Members";
                break;
            case 1:
                title = "Waiting";
                break;
        }

        return title;
    }

    public void setCount(int count){
        if (count > 0 && count < 10){
            mCount = count;
            notifyDataSetChanged();
        }
    }


}
