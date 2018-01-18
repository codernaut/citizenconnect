package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.cfp.citizenconnect.Adapters.MyPagerAdapter;
import org.cfp.citizenconnect.Model.NotificationUpdate;
import org.cfp.citizenconnect.databinding.ActivityMainBinding;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.CALL_PERMISSION_REQUEST;


public class MainActivity extends AppCompatActivity {


    NotificationUpdate notificationUpdate;
    ProgressDialog progress;
    ActivityMainBinding binding;
    boolean clearNotificationCount;
    String phoneNo;
    ConstraintLayout mLayout;
    AHBottomNavigation bottomNavigation;
    private BroadcastReceiver mNotificationReceiver;
    MyPagerAdapter mPageAdapter;
    ViewPager mViewPager;
    int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            clearNotificationCount = getIntent().getExtras().getBoolean("clearNotificationCount", false);
        }

        progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        //progress.show();
        notificationUpdate = NotificationUpdate.getInstance(realm);
        mLayout = findViewById(R.id.mainLayout);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.viewpager);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_item1, R.drawable.ic_notifications_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_item2, R.drawable.ic_data_usage_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_item3, R.drawable.ic_feedback_white_24dp, R.color.colorPrimary);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        bottomNavigation.setAccentColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(MainActivity.this, R.color.lightGreen));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red));
        bottomNavigation.setNotification("3", 0);
        mPageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        currentItem = bottomNavigation.getCurrentItem();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
                if (position == 0) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                mViewPager.setCurrentItem(position);
                return true;
            }
        });
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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


    private void updateNotificationCount() {

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
}
