package org.cfp.citizenconnect;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import org.cfp.citizenconnect.Model.DataSet;
import org.cfp.citizenconnect.Model.User;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmResults;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.SCOPES;
import static org.cfp.citizenconnect.Model.DataSet.getDataSet;
import static org.cfp.citizenconnect.MyUtils.isDeviceOnline;

/**
 * Created by shahzaibshahid on 26/01/2018.
 */

public class SplashScreen extends Activity implements EasyPermissions.PermissionCallbacks {
    ProgressBar progressBar;
    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 2;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 3;
    static final int REQUEST_AUTHORIZATION = 4;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    GoogleAccountCredential mCredential;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        progressBar = findViewById(R.id.progressBar);
        user = User.getUserInstance(realm);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                new AlertDialog.Builder(SplashScreen.this).
                        setMessage("This app needs to access your Google account (via Contacts)")
                        .setCancelable(false).setPositiveButton("OKAY", (dialogInterface, i) -> finish()).show();
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (user.getEmail() != null) {

                mCredential.setSelectedAccountName(user.getEmail());
                getResultsFromApi();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    REQUEST_PERMISSION_GET_ACCOUNTS);

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
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        user.setEmail(accountName);
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    public void getResultsFromApi() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                showGooglePlayServicesAvailabilityErrorDialog(status);
            }
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline(SplashScreen.this)) {
            Toast.makeText(SplashScreen.this, "No Internet Available", Toast.LENGTH_LONG).show();
        } else {
            if (isObjectExist()) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            } else {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                new DownloadFilesTask(SplashScreen.this).execute();
            }
            // new SendEmail(mCredential).execute();
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
