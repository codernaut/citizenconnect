package org.cfp.citizenconnect.Notification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.cfp.citizenconnect.Model.Notifications;

import java.util.ArrayList;
import java.util.List;

public class FullScreenPagerAdapter extends FragmentStatePagerAdapter {

    List<Notifications> notificationsModel = new ArrayList<>();

    public FullScreenPagerAdapter(FragmentManager fm, List<Notifications> notificationsModel) {
        super(fm);
        this.notificationsModel = notificationsModel;
    }

    @Override
    public Fragment getItem(int position) {
        return FullScreenFragment.newInstance(notificationsModel.get(position).getFilePath(), notificationsModel.get(position).getDescription());
    }

    @Override
    public int getCount() {
        return notificationsModel.size();
    }
}