package org.cfp.citizenconnect;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.cfp.citizenconnect.Adapters.MyPagerAdapter;
import org.cfp.citizenconnect.Interfaces.ScrollStatus;
import org.cfp.citizenconnect.Interfaces.Search;
import org.cfp.citizenconnect.Model.MessageEvent;
import org.cfp.citizenconnect.Model.NotificationUpdate;
import org.cfp.citizenconnect.databinding.ActivityMainBinding;
import org.greenrobot.eventbus.EventBus;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.CALL_PERMISSION_REQUEST;
import static org.cfp.citizenconnect.Constants.ICT_NOTIFICATION_ID;


public class MainActivity extends AppCompatActivity implements ScrollStatus {


    public Search mSearch;
    ProgressDialog progress;
    ActivityMainBinding binding;
    boolean clearNotificationCount;
    String phoneNo;
    ConstraintLayout mLayout;
    AHBottomNavigation bottomNavigation;
    MyPagerAdapter mPageAdapter;
    ViewPager mViewPager;
    int currentItem;
    MenuItem menuItem;
    NotificationUpdate notificationUpdate;
    private BroadcastReceiver mNotificationReceiver;
    SearchView searchView;
    MenuItem searchMenu;

    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.in_progress_msg));
        progress.setCancelable(false);
        //progress.show();
        notificationUpdate = NotificationUpdate.getInstance(realm);
        mLayout = findViewById(R.id.mainLayout);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.viewpager);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_item1, R.drawable.ic_notifications_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_item2, R.drawable.ic_documents_filled, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_item3, R.drawable.ic_storage_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.bottom_item4, R.drawable.ic_feedback_white_24dp, R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        if (getIntent().getExtras() != null) {
            clearNotificationCount = getIntent().getExtras().getBoolean("clearNotificationCount", false);
            if (clearNotificationCount) {
                realm.executeTransaction(realm -> {
                    notificationUpdate.setLastStateRead(true);
                    notificationUpdate.setNewNotification(0);
                });

                changeNotificationStatus("", ContextCompat.getColor(MainActivity.this, R.color.red));
            }
        } else {
            if (notificationUpdate.getNewNotification() != 0) {
                changeNotificationStatus(notificationUpdate.getNewNotification() + "", ContextCompat.getColor(MainActivity.this, R.color.red));
            }
        }
        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        bottomNavigation.setAccentColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(MainActivity.this, R.color.lightGreen));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
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
                String count;
                count = notificationUpdate.getNewNotification() == 0 ? "" : notificationUpdate.getNewNotification() + "";
                if (position == 0) {
                    changeNotificationStatus(count, ContextCompat.getColor(MainActivity.this, R.color.red));
                    searchMenu.setVisible(true);

                } else {
                    changeNotificationStatus(count, ContextCompat.getColor(MainActivity.this, R.color.lightGreen));
                    searchMenu.setVisible(false);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (menuItem != null) {
                    menuItem.collapseActionView();
                }

            }
        });
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            mViewPager.setCurrentItem(position);
            return true;
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

                    changeNotificationStatus(notificationUpdate.getNewNotification() + "", ContextCompat.getColor(MainActivity.this, R.color.red));
                }
            }
        };
        this.registerReceiver(mNotificationReceiver, intentFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        searchMenu = menu.findItem(R.id.search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            menuItem.collapseActionView();

            return true;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearch.OnSearchNotification(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mSearch.OnSearchNotification(query);
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItem = item;
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
        progress.dismiss();
        this.unregisterReceiver(this.mNotificationReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
            case REQUEST_PERMISSION_GET_ACCOUNTS:{
                if(grantResults.length>0){
                    EventBus.getDefault().post(new MessageEvent("Permission Granted",true));
                }
                else {
                    EventBus.getDefault().post(new MessageEvent("Permission Denied",false));
                }
            }
            break;
            default:
                break;
        }
    }


    private void changeNotificationStatus(String count, @ColorInt int color) {
        bottomNavigation.setNotificationBackgroundColor(color);
        bottomNavigation.setNotification(count, 0);
    }

    @Override
    public void OnScrollStatusChanged(boolean status) {
        if (status) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.
                            NOTIFICATION_SERVICE);
            notificationManager.cancel(ICT_NOTIFICATION_ID);
            changeNotificationStatus("", ContextCompat.getColor(MainActivity.this, R.color.red));
            realm.executeTransaction(realm -> {
                notificationUpdate.setLastStateRead(true);
                notificationUpdate.setNewNotification(0);
            });
        }
    }
}
