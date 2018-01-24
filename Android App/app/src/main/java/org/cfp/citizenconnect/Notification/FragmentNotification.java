package org.cfp.citizenconnect.Notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.squareup.picasso.Picasso;

import org.cfp.citizenconnect.Adapters.NotificationLayoutAdapter;
import org.cfp.citizenconnect.Interfaces.ScrollStatus;
import org.cfp.citizenconnect.Interfaces.Search;
import org.cfp.citizenconnect.MainActivity;
import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;
import org.cfp.citizenconnect.databinding.NotificationFragmentBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Case;
import io.realm.RealmResults;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static org.cfp.citizenconnect.CitizenConnectApplication.FilesRef;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Model.Notifications.fetchFirebaseNotifications;
import static org.cfp.citizenconnect.MyUtils.frescoImageRequest;
import static org.cfp.citizenconnect.MyUtils.getBitmapUri;

/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentNotification extends Fragment implements NotificationLayoutAdapter.OnItemInteractionListener, Search {
    NotificationFragmentBinding binding;
    List<Notifications> notificationsModel = new ArrayList<>();
    NotificationLayoutAdapter notificationListAdapter;
    BlurPopupWindow.Builder mBuilder;
    ProgressDialog progressDialog;
    ScrollStatus mScrollStatus;

    public static FragmentNotification newInstance() {
        FragmentNotification fragmentNotification = new FragmentNotification();
        return fragmentNotification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.notification_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        mScrollStatus = (MainActivity)getActivity();
        loadFromRealm();
        binding.notificationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    mScrollStatus.OnScrollStatusChanged(true);
                }
            }
        });
        View view = binding.getRoot();
        return view;
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
            LinearLayoutManager notificationList = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            notificationListAdapter = new NotificationLayoutAdapter(getActivity(), notificationsModel, this);
            binding.notificationList.setLayoutManager(notificationList);
            binding.notificationList.setAdapter(notificationListAdapter);
            progressDialog.dismiss();

        } else {
            loadFromFirebase();
        }

    }

    private void loadFromFirebase() {
        notificationsModel.clear();
        fetchFirebaseNotifications(FilesRef, response -> {
            notificationsModel = response;
            Collections.reverse(notificationsModel);
            LinearLayoutManager notificationList = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            notificationListAdapter = new NotificationLayoutAdapter(getActivity(), notificationsModel, FragmentNotification.this);
            binding.notificationList.destroyDrawingCache();
            binding.notificationList.setLayoutManager(notificationList);
            binding.notificationList.setAdapter(notificationListAdapter);
            progressDialog.dismiss();
        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG);
            progressDialog.dismiss();
        });
    }

    @Override
    public void ShareImageClickListener(int position, Drawable image) {
        try {
            if (notificationsModel.get(position).getFilePath() != null) {
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
    public void FullSizeImageClickListener(String imagePath) {
        frescoImageRequest(imagePath, getActivity(), response -> {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View customView = inflater.inflate(R.layout.full_image_size_popup, null);
            customView.setLayoutParams(params);
            com.github.chrisbanes.photoview.PhotoView imageHolder = customView.findViewById(R.id.imageHolder);

            Picasso.with(getActivity()).load(imagePath).into(imageHolder);

            mBuilder = new BlurPopupWindow.Builder(getActivity());

            mBuilder.setContentView(customView)
                    .setGravity(Gravity.CENTER)
                    .setDismissOnClickBack(true)
                    .setDismissOnTouchBackground(false)
                    .setBlurRadius(10)
                    .setTintColor(0x30000000)
                    .build().show();
        }, error -> Toast.makeText(getActivity(), "Failed to load Image", Toast.LENGTH_LONG).show());
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
}
