package org.cfp.citizenconnect.Data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.cfp.citizenconnect.Adapters.DataSetAdapter;
import org.cfp.citizenconnect.LocationUpdates;
import org.cfp.citizenconnect.Model.DataSet;
import org.cfp.citizenconnect.Model.MyItem;
import org.cfp.citizenconnect.PermissionsRequest;
import org.cfp.citizenconnect.Popups.DataSetComplainDialog;
import org.cfp.citizenconnect.R;

import java.util.List;

import io.realm.Case;
import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Model.DataSet.fetchFromRealm;
import static org.cfp.citizenconnect.MyUtils.canGetLocation;
import static org.cfp.citizenconnect.MyUtils.isDeviceOnline;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

public class DataSetListActivity extends AppCompatActivity implements DataSetAdapter.OnComplainClickListener, OnMapReadyCallback, LocationUpdates.OnReceiveLocation {


    public static Context CONTEXT_DATA;
    DataSetAdapter dataSetAdapter;
    RecyclerView recyclerView;
    String type;
    List<DataSet> list;
    ProgressDialog progressDialog;
    MenuItem menuItem;
    LinearLayoutManager dataListLayout;
    DividerItemDecoration dividerItemDecoration;
    MapView mapView;
    FloatingActionButton fab;
    MenuItem mapViewMenu;
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataset_list_activity);
        recyclerView = findViewById(R.id.dataList_RV);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setVisibility(View.GONE);
        fab = findViewById(R.id.fAb);
        fab.setVisibility(View.GONE);
        CONTEXT_DATA = DataSetListActivity.this;
        progressDialog = new ProgressDialog(DataSetListActivity.this);
        progressDialog.setMessage(getString(R.string.in_progress_msg));
        progressDialog.show();
        dataListLayout = new LinearLayoutManager(DataSetListActivity.this, LinearLayoutManager.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                dataListLayout.getOrientation());
        recyclerView.setLayoutManager(dataListLayout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(DATA_TYPE);
            getSupportActionBar().setTitle(type);
            list = fetchFromRealm(type);
            if (list.size() == 0) {
                Toast.makeText(DataSetListActivity.this, "No data found", Toast.LENGTH_LONG).show();
                finish();
            } else {
                updateAdapter();
                progressDialog.dismiss();
            }

        } else {
            Toast.makeText(DataSetListActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        menuItem = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.mapView:
                switchToMapView();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateAdapter() {
        dataSetAdapter = new DataSetAdapter(DataSetListActivity.this, list, this);
        recyclerView.setAdapter(dataSetAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        mapViewMenu = menu.findItem(R.id.mapView);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
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
                updateQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                updateQuery(query);
                return true;
            }

        });
        return true;
    }

    private void updateQuery(String query) {
        list.clear();
        RealmResults<DataSet> dataSets = realm.where(DataSet.class).equalTo("dataSetType",
                type).contains("name", query, Case.INSENSITIVE).findAll();
        list.addAll(dataSets);
        updateAdapter();
        if (mapView.getVisibility() == View.VISIBLE) {
            mMap.clear();
            mClusterManager.clearItems();
            if (query.length() == 0) {
                plotMarkers(false);
            } else {
                plotMarkers(true);
            }
        }

    }

    @Override
    public void setOnComplainInteraction(String dataSetName, String dataSetAddress) {
        Bundle bundle = new Bundle();
        bundle.putString("dataSetName", dataSetName);
        bundle.putString("dataSetAddress", dataSetAddress);
        bundle.putString("type", type);

        DataSetComplainDialog dataSetComplainDialog = new DataSetComplainDialog();
        dataSetComplainDialog.setArguments(bundle);
        dataSetComplainDialog.show(getSupportFragmentManager(), "complain");
    }

    private void switchToMapView() {
        fab.setVisibility(View.VISIBLE);
        mapViewMenu.setVisible(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DataSetListActivity.this, PermissionsRequest.LOCATION_PERMISSIONS, PermissionsRequest.LOCATION_REQUEST_CODE);
            } else {
                getLocationUpdates();
            }
        } else {
            getLocationUpdates();
        }
    }
    public   void switchToListView(View view){
        mapView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
        mapViewMenu.setVisible(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsRequest.LOCATION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    boolean showRationale = shouldShowRequestPermissionRationale( permissions[0] );
                    if (!showRationale) {
                        new AlertDialog.Builder(this).setTitle("Allow Location Access")
                                .setMessage("You must allow app to access location to use map View")
                                .setPositiveButton("OKAY", (dialogInterface, i) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:" + getPackageName()));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }).show();
                        return;
                    }

                    ActivityCompat.requestPermissions(DataSetListActivity.this, PermissionsRequest.LOCATION_PERMISSIONS, PermissionsRequest.LOCATION_REQUEST_CODE);

                    return;
                }
                mMap.setMyLocationEnabled(true);
                getLocationUpdates();

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void plotMarkers(Boolean zoomToNewLocation) {
        for (DataSet dataSet : list) {
            MyItem location = new MyItem(dataSet.getLatitude(), dataSet.getLongitude(), dataSet.getName(), dataSet.getAddress());
            mClusterManager.addItem(location);
        }

        if (zoomToNewLocation) {
            if (!list.isEmpty()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()), 18.0f));
            }
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15.0f));
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationUpdates() {
        if (isDeviceOnline(this)) {
            if (canGetLocation(this)) {
                mapView.setVisibility(View.VISIBLE);
                mMap.clear();
                recyclerView.setVisibility(View.GONE);
                mMap.setMyLocationEnabled(true);
                startActivity(new Intent(DataSetListActivity.this, LocationUpdates.class));

            } else {
                Toast.makeText(this, "Enable Location Setting", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void LocationReceived(Location location) {
        mCurrentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

        //set Up cluster
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        plotMarkers(false);
    }
}
