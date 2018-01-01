package org.cfp.citizenconnect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.actions.NoteIntents;

import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.cfp.citizenconnect.MyUtils.getBitmapUri;

/**
 * Created by shahzaibshahid on 24/12/2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> implements NotificationLayoutAdapter.OnItemInteractionListener {

    List<String> notificationDate;
    List<Notifications> NotificationList;
    Context mContext;
    HashMap<String, List<Notifications>> map;


    public NotificationListAdapter(List<String> notificationDate, List<Notifications> NotificationList, Context mContext) {
        this.notificationDate = notificationDate;
        this.NotificationList = NotificationList;
        this.mContext = mContext;
        this.map = createHashMap(NotificationList, notificationDate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);

        return new NotificationListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationLayoutAdapter notificationAdapter;
        notificationAdapter = new NotificationLayoutAdapter(mContext, filterNotification(this.NotificationList, notificationDate.get(position)), this);
        LinearLayoutManager notificationLayout = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.notificationLayout.setLayoutManager(notificationLayout);
        holder.notificationLayout.setAdapter(notificationAdapter);
        holder.notificationLayout.setOnScrollChangeListener(
                (view, i, i1, i2, i3) -> {
                   List<Notifications> V = map.get(notificationDate.get(position));
                   holder.description.setText(V.get(holder.notificationLayout.getCurrentPosition()).getDescription());
                    return;
                });

    }

    @Override
    public int getItemCount() {
        return notificationDate.size();
    }

    @Override
    public void ShareImageClickListener(int position, Drawable Image) {
        try {
            Uri bmpUri = getBitmapUri(Uri.parse(NotificationList.get(position).getFilePath()), mContext);
            if (bmpUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Notifications> filterNotification(List<Notifications> notificationList, String currentDate) {
        List<Notifications> filteredNotificationList = new ArrayList<>();
        for (Notifications notification : notificationList) {
            if (notification.getDate().equals(currentDate)) {
                filteredNotificationList.add(notification);
            }
        }
        return filteredNotificationList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager notificationLayout;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            notificationLayout = itemView.findViewById(R.id.snapViewer);

        }
    }

    public static HashMap<String, List<Notifications>> createHashMap(List<Notifications> fileModel, List<String> notificationDates) {
        HashMap<String, List<Notifications>> map = new HashMap<>();
        Collections.reverse(notificationDates);

        for (String K : notificationDates) {
            List<Notifications> V_list = new ArrayList<>();
            for (Notifications V : fileModel) {
                if (V.getDate().equals(K)) {
                    V_list.add(V);
                }
            }
            map.put(K,V_list);
        }
        return map;
    }
}
