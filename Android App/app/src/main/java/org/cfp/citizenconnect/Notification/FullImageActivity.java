package org.cfp.citizenconnect.Notification;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.cfp.citizenconnect.R;
import static org.cfp.citizenconnect.MyUtils.frescoImageRequest;

/**
 * Created by shahzaibshahid on 09/02/2018.
 */

public class FullImageActivity extends Activity {
    String filePath;

    com.github.chrisbanes.photoview.PhotoView ImageHolder;
    ImageButton backBtn;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        ImageHolder = findViewById(R.id.imageHolder);
        backBtn = findViewById(R.id.backButton);
        description = findViewById(R.id.description);

        ImageHolder.setOnClickListener(view -> {
            if (backBtn.getVisibility() == View.VISIBLE && description.getVisibility() == View.VISIBLE) {
                backBtn.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            } else {
                backBtn.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
            }
        });
        backBtn.setOnClickListener(view -> finish());
        if (getIntent().getExtras() != null) {
            filePath = getIntent().getExtras().getString(getString(R.string.FILE_URL));
            description.setText(getIntent().getExtras().getString(getString(R.string.DESCRIPTION)));
            frescoImageRequest(filePath, this, response -> {
                Picasso.with(this).load(filePath).into(ImageHolder);


            }, error -> Toast.makeText(FullImageActivity.this, "Failed to load Image", Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(FullImageActivity.this, "Failed to load Image", Toast.LENGTH_LONG).show();
        }
    }
}
