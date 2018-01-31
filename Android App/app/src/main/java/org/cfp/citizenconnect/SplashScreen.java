package org.cfp.citizenconnect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.cfp.citizenconnect.Model.DataSet;
import org.cfp.citizenconnect.Model.User;

import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Model.DataSet.getDataSet;
import static org.cfp.citizenconnect.MyUtils.isDeviceOnline;
import static org.cfp.citizenconnect.MyUtils.mSnakbar;

/**
 * Created by shahzaibshahid on 26/01/2018.
 */

public class SplashScreen extends Activity {
    ProgressBar progressBar;

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 2;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 3;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        progressBar = findViewById(R.id.progressBar);
        user = User.getUserInstance(realm);
        getResultsFromApi();
    }

    public boolean isObjectExist() {
        RealmResults<DataSet> sets = realm.where(DataSet.class).findAll();
        if (sets.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private class DownloadFilesTask extends AsyncTask<Void, Integer, Void> {

        Context context;

        public DownloadFilesTask(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            mSnakbar("Please wait...", null, 5000, 1, findViewById(R.id.coordinator), null);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            getDataSet(response -> {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();

            }, error -> {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    //Todo: google service must be required
                } else {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GET_ACCOUNTS) {
            getResultsFromApi();
        }
    }

    public void getResultsFromApi() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                showGooglePlayServicesAvailabilityErrorDialog(status);
            }
        } else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                    REQUEST_PERMISSION_GET_ACCOUNTS);
        } else if (!isDeviceOnline(SplashScreen.this)) {
            mSnakbar("No Internet Available", null, 5000, 1, findViewById(R.id.coordinator), null);
            progressBar.setVisibility(View.GONE);
        } else {
            if (isObjectExist()) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            } else {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                new DownloadFilesTask(SplashScreen.this).execute();
            }
        }
    }


    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                SplashScreen.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

}
