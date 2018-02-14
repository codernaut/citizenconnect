package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.net.URL;

import needle.Needle;

import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Constants.FILE_URL;


/**
 * Created by shahzaibshahid on 24/01/2018.
 */

public class PdfViewerActivity extends AppCompatActivity {

    String url;
    String type;
    ProgressDialog progressDialog;
    MenuItem menuItem;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer_layout);
        pdfView = findViewById(R.id.pdfView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.in_progress_msg));
        progressDialog.show();
        progressDialog.setCancelable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(DATA_TYPE);
            url = bundle.getString(FILE_URL);
            getSupportActionBar().setTitle(type);

            Needle.onBackgroundThread().execute(() -> {

                try {
                    pdfView.fromStream(new URL(url).openStream())
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            .enableAntialiasing(true)
                            .spacing(0)
                            .load();
                    progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } else {
            Toast.makeText(PdfViewerActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        menuItem = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
