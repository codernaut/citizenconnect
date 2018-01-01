package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.cfp.citizenconnect.Adapters.NotificationListAdapter;
import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.cfp.citizenconnect.CitizenConnectApplciation.FilesRef;

public class MainActivity extends AppCompatActivity {

    List<Notifications> notificationsModel = new ArrayList<>();
    NotificationListAdapter notificationListAdapter;
    ProgressDialog progress;
    ActivityMainBinding binding;


    private BroadcastReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.show();
        loadFromFirebase();
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
                    progress.show();

                }
            }
        };
        this.registerReceiver(mNotificationReceiver, intentFilter);
    }

    @Override
    protected void onPause() {

        super.onPause();

        this.unregisterReceiver(this.mNotificationReceiver);
    }

    private void loadFromFirebase() {
        notificationsModel.clear();
        FilesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Notifications notifications = data.getValue(Notifications.class);
                    notificationsModel.add(notifications);
                }
                Collections.reverse(notificationsModel);
                LinearLayoutManager notificationList = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                notificationListAdapter = new NotificationListAdapter(extractDate(notificationsModel), notificationsModel, MainActivity.this);
                binding.notificationList.setLayoutManager(notificationList);
                binding.notificationList.setAdapter(notificationListAdapter);
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Error", "Failed to read value.", error.toException());
                progress.dismiss();
            }
        });
    }


    private List<String> extractDate(List<Notifications> fileModel) {
        List<String> notificationDates = new ArrayList<>();

        for (Notifications notifications : fileModel) {
            if (!notificationDates.contains(notifications.getDate())) {
                notificationDates.add(notifications.getDate());
            }
        }
        Collections.reverse(notificationDates);
        return notificationDates;
    }
}
