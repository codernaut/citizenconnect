package org.cfp.citizenconnect.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.cfp.citizenconnect.Data.FragmentDataSet;
import org.cfp.citizenconnect.Feedback.FragmentFeedback;
import org.cfp.citizenconnect.Notification.FragmentNotification;

/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class MyPagerAdapter extends  FragmentStateAdapter {
    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentNotification.newInstance();
            case 1:
                return FragmentDataSet.newInstance();
            case 2:
                return FragmentFeedback.newInstance();
            default:
                return null;
        }
    }
}
