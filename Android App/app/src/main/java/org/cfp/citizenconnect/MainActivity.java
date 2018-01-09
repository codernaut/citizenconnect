package org.cfp.citizenconnect;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import org.cfp.citizenconnect.Adapters.NotificationLayoutAdapter;
import org.cfp.citizenconnect.Model.NotificationUpdate;
import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplciation.FilesRef;
import static org.cfp.citizenconnect.CitizenConnectApplciation.realm;
import static org.cfp.citizenconnect.MyUtils.getBitmapUri;

public class MainActivity extends AppCompatActivity implements NotificationLayoutAdapter.OnItemInteractionListener {
    public static final int CALL_PERMISSION_REQUEST = 1;

    List<Notifications> notificationsModel = new ArrayList<>();
    NotificationLayoutAdapter notificationListAdapter;
    NotificationUpdate notificationUpdate;
    ProgressDialog progress;
    ActivityMainBinding binding;
    boolean clearNotificationCount;
    MenuItem menuItem;
    View actionView;
    String phoneNo;
    TextView countNotification;
    private BroadcastReceiver mNotificationReceiver;
    ConstraintLayout mLayout;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            clearNotificationCount = getIntent().getExtras().getBoolean("clearNotificationCount", false);
        }

        progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.show();
        notificationUpdate = NotificationUpdate.getInstance(realm);
        loadFromRealm();
        mLayout = findViewById(R.id.mainLayout);

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
                    updateNotificationCount();
                }
            }
        };
        this.registerReceiver(mNotificationReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        menuItem = menu.findItem(R.id.newNotification);
        actionView = MenuItemCompat.getActionView(menuItem);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        countNotification = actionView.findViewById(R.id.notificationCount);
        updateNotificationCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.newNotification:
                loadFromFirebase();
                clearNotificationCount = true;
                updateNotificationCount();
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
                progress.show();
                return true;
            case R.id.aboutUS:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            case R.id.police:
                phoneNo = getResources().getString(R.string.police);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);
                }
                return true;
            case R.id.ambulance:
                phoneNo = getResources().getString(R.string.ambulance);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);
                }
                return true;
            case R.id.FireBrigade:
                phoneNo = getResources().getString(R.string.fireBrigade);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        this.unregisterReceiver(this.mNotificationReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void loadFromFirebase() {


        FilesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationsModel.clear();
                realm.executeTransaction(realm -> realm.where(Notifications.class).findAll().deleteAllFromRealm());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Notifications notifications = data.getValue(Notifications.class);
                    Notifications.setNotifications(notifications, realm);
                    notificationsModel.add(notifications);
                }
                Collections.reverse(notificationsModel);
                LinearLayoutManager notificationList = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                notificationListAdapter = new NotificationLayoutAdapter(MainActivity.this, notificationsModel, MainActivity.this);
                binding.notificationList.destroyDrawingCache();
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

    private void loadFromRealm() {
        notificationsModel.clear();
        RealmResults<Notifications> realmResults = realm.where(Notifications.class).findAll();
        if (realmResults.size() != 0) {
            for (Notifications _Notifications : realmResults) {
                notificationsModel.add(_Notifications);
            }
            Collections.reverse(notificationsModel);
            LinearLayoutManager notificationList = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            notificationListAdapter = new NotificationLayoutAdapter(MainActivity.this, notificationsModel, MainActivity.this);
            binding.notificationList.setLayoutManager(notificationList);
            binding.notificationList.setAdapter(notificationListAdapter);
            progress.dismiss();
        } else {
            loadFromFirebase();
        }

    }

    private void updateNotificationCount() {
        if (clearNotificationCount) {
            realm.executeTransaction(realm -> notificationUpdate.setNewNotiifcation(0));
            countNotification.setText(notificationUpdate.getNewNotiifcation() + "");
            countNotification.setVisibility(View.GONE);
            clearNotificationCount = false;
            loadFromFirebase();

        } else {
            if (notificationUpdate.getNewNotiifcation() == 0) {
                countNotification.setVisibility(View.GONE);
            } else {
                countNotification.setVisibility(View.VISIBLE);
                countNotification.setText(notificationUpdate.getNewNotiifcation() + "");
            }
        }
    }

    @Override
    public void ShareImageClickListener(int position, Drawable Image) {
        try {
            Uri bmpUri = getBitmapUri(Uri.parse(notificationsModel.get(position).getFilePath()), MainActivity.this);
            if (bmpUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                getApplicationContext().startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void FullSizeImageClickListener(String imagePath) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View customView = inflater.inflate(R.layout.full_image_size_popup, null);
        customView.setLayoutParams(params);
         new BlurPopupWindow.Builder(MainActivity.this)
                .setContentView(customView)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build()
                .show();

        SimpleDraweeView imageHolder = customView.findViewById(R.id.imageHolder);
        imageHolder.setImageURI(Uri.parse(imagePath));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION_REQUEST: {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);
                }
            }
            break;

            default:
                break;
        }
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
