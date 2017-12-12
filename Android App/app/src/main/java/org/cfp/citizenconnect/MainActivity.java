package org.cfp.citizenconnect;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    com.facebook.drawee.view.SimpleDraweeView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (com.facebook.drawee.view.SimpleDraweeView)findViewById(R.id.imageHolder);
        SharedPreferences myPref = getSharedPreferences("MyPrefference", MODE_PRIVATE);
        image.setImageURI(Uri.parse(myPref.getString("imageLink","")));
    }
}
