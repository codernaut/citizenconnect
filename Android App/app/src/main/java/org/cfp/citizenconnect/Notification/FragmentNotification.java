package org.cfp.citizenconnect.Notification;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.cfp.citizenconnect.Adapters.NotificationLayoutAdapter;
import org.cfp.citizenconnect.Interfaces.ScrollStatus;
import org.cfp.citizenconnect.Interfaces.Search;
import org.cfp.citizenconnect.MainActivity;
import org.cfp.citizenconnect.Model.NotificationUpdate;
import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;
import org.cfp.citizenconnect.databinding.NotificationFragmentBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Case;
import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplication.FilesRef;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Model.Notifications.fetchFirebaseNotifications;
import static org.cfp.citizenconnect.MyUtils.getBitmapUri;

/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentNotification extends Fragment implements NotificationLayoutAdapter.OnItemInteractionListener, Search {
    NotificationFragmentBinding binding;
    List<Notifications> notificationsModel = new ArrayList<>();
    NotificationLayoutAdapter notificationListAdapter;
    ProgressDialog progressDialog;
    ScrollStatus mScrollStatus;
    NotificationUpdate notificationUpdate;
    private BroadcastReceiver mNotificationReceiver;


    public static FragmentNotification newInstance() {
        FragmentNotification fragmentNotification = new FragmentNotification();
        return fragmentNotification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.notification_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.in_progress_msg));
        progressDialog.show();
        notificationUpdate = NotificationUpdate.getInstance(realm);
        mScrollStatus = (MainActivity) getActivity();
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                loadFromFirebase();
                if (notificationListAdapter != null && notificationsModel != null) {
                    notificationsModel.clear();
                    notificationListAdapter.notifyDataSetChanged();
                }
            }, 500);

        });
        if (notificationUpdate.getNewNotification() > 0 || !notificationUpdate.isLastStateRead()) {
            loadFromFirebase();
        } else {
            loadFromRealm();
        }

        binding.notificationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mScrollStatus.OnScrollStatusChanged(true);
                }
            }
        });

        FilesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                loadFromFirebase();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        View view = binding.getRoot();
        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mNotificationReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean update = intent.getBooleanExtra("newUpdate", false);
                if (update) {
                    loadFromFirebase();
                }
            }
        };
        getActivity().registerReceiver(mNotificationReceiver, intentFilter);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mNotificationReceiver);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).mSearch = this;
    }

    private void loadFromRealm() {
        notificationsModel.clear();
        RealmResults<Notifications> realmResults = realm.where(Notifications.class).findAll();
        if (realmResults.size() != 0) {
            for (Notifications _Notifications : realmResults) {
                notificationsModel.add(_Notifications);
            }
            Collections.reverse(notificationsModel);
            updateRecyclerView();

        } else {
            loadFromFirebase();
        }
    }

    private void loadFromFirebase() {
        notificationsModel.clear();
        fetchFirebaseNotifications(FilesRef, (List<Notifications> response) -> {
            notificationsModel = response;
            Collections.reverse(notificationsModel);
            updateRecyclerView();
        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG);
            progressDialog.dismiss();
        });
    }

    @Override
    public void ShareImageClickListener(int position, Drawable image) {
        try {
            if (notificationsModel.get(position).getFilePath() != null &&!TextUtils.isEmpty(notificationsModel.get(position).getFilePath())) {

                Uri bmpUri = getBitmapUri(Uri.parse(notificationsModel.get(position).getFilePath()), getActivity());
                if (bmpUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.playStoreUrl));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                }
            } else {
                Toast.makeText(getActivity(), "Failed to Share. Please try again", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void FullSizeImageClickListener(int position, String imagePath, String description) {
        /*Intent i = new Intent(getActivity(), FullImageActivity.class);
        i.putExtra(FILE_URL, imagePath);
        i.putExtra(DESCRIPTION, description);
        startActivity(i);*/

        FullNewsViewFragment fullNewsViewFragment = new FullNewsViewFragment();
        fullNewsViewFragment.setNotifications(notificationsModel);
        fullNewsViewFragment.setPosition(position);
        fullNewsViewFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
        fullNewsViewFragment.show(getFragmentManager(), "FullScreenNews");



    }

    @Override
    public void OnSearchNotification(String query) {
        notificationsModel.clear();
        RealmResults<Notifications> realmResults = realm.where(Notifications.class).contains("description", query, Case.INSENSITIVE).findAll();
        for (Notifications _Notifications : realmResults) {
            notificationsModel.add(_Notifications);
        }
        Collections.reverse(notificationsModel);
        LinearLayoutManager notificationList = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        notificationListAdapter = new NotificationLayoutAdapter(getActivity(), notificationsModel, this);
        binding.notificationList.setLayoutManager(notificationList);
        binding.notificationList.setAdapter(notificationListAdapter);
    }

    public void updateRecyclerView() {
        binding.swipeRefreshLayout.setRefreshing(false);
        LinearLayoutManager notificationList = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        notificationListAdapter = new NotificationLayoutAdapter(getActivity(), notificationsModel, FragmentNotification.this);
        binding.notificationList.setLayoutManager(notificationList);
        binding.notificationList.setAdapter(notificationListAdapter);
        progressDialog.dismiss();
    }
}
