package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.cfp.citizenconnect.Adapters.SnapsNotificationAdapter;
import org.cfp.citizenconnect.Model.Files;

import java.util.ArrayList;
import java.util.List;

import static org.cfp.citizenconnect.CitizenConnectApplciation.FilesRef;

public class MainActivity extends AppCompatActivity {

    List <Files> filesModel = new ArrayList<>();
    RecyclerView snapViwer;
    SnapsNotificationAdapter notificationAdapter;
    ProgressDialog progress ;

    private BroadcastReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.show();
        snapViwer = (RecyclerView)findViewById(R.id.snapViewer);
        loadFromFirebase();
    }

    @Override
    public  void onResume() {

        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mNotificationReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean update = intent.getBooleanExtra("newUpdate",false);
                if(update){
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

    private  void loadFromFirebase(){
        filesModel.clear();
        FilesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Files files = data.getValue(Files.class);
                    filesModel.add(files);
                }
                notificationAdapter = new SnapsNotificationAdapter(MainActivity.this,filesModel);
                LinearLayoutManager eventProgramLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                snapViwer.setLayoutManager(eventProgramLayoutManager);
                snapViwer.addItemDecoration(new DividerItemDecoration(snapViwer.getContext(),
                        eventProgramLayoutManager.getOrientation()));
                snapViwer.setAdapter(notificationAdapter);
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Error", "Failed to read value.", error.toException());
                progress.dismiss();
            }
        });
    }
}
