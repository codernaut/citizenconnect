package org.cfp.citizenconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.cfp.citizenconnect.Model.DataSet;

import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Model.DataSet.getDataSet;

/**
 * Created by shahzaibshahid on 26/01/2018.
 */

public class SplashScreen extends Activity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        progressBar = findViewById(R.id.progressBar);
        if (isObjectExist()) {
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        } else {
            new DownloadFilesTask(progressBar, SplashScreen.this).execute();
        }
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
        ProgressBar progress;
        Context context;

        public DownloadFilesTask(ProgressBar progress, Context context) {
            this.progress = progress;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            progress.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            getDataSet(response -> {
                progress.setVisibility(ProgressBar.GONE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();

            }, error -> {
                progress.setVisibility(ProgressBar.GONE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }
}
