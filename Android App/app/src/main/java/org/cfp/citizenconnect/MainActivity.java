package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.cfp.citizenconnect.Adapters.SnapsNotificationAdapter;
import org.cfp.citizenconnect.Model.Files;
import org.cfp.citizenconnect.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.cfp.citizenconnect.CitizenConnectApplciation.FilesRef;
import static org.cfp.citizenconnect.MyUtils.getBitmapUri;

public class MainActivity extends AppCompatActivity implements SnapsNotificationAdapter.OnItemInteractionListener {

    List <Files> filesModel = new ArrayList<>();
    SnapsNotificationAdapter notificationAdapter;
    ProgressDialog progress ;
    ActivityMainBinding binding;
    private BroadcastReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.show();
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
                notificationAdapter = new SnapsNotificationAdapter(MainActivity.this,filesModel,MainActivity.this);
                LinearLayoutManager eventProgramLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                binding.snapViewer.setLayoutManager(eventProgramLayoutManager);
                binding.snapViewer.addItemDecoration(new DividerItemDecoration(binding.snapViewer.getContext(),
                        eventProgramLayoutManager.getOrientation()));
                binding.snapViewer.setAdapter(notificationAdapter);
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Error", "Failed to read value.", error.toException());
                progress.dismiss();
            }
        });
    }

    @Override
    public void ShareImageClickListener(int position, Drawable Image) {
        try {
            Uri bmpUri = getBitmapUri(Uri.parse(filesModel.get(position).getUrl()),MainActivity.this);
            if (bmpUri!=null){
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
