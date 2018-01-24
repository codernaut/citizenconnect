package org.cfp.citizenconnect;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;


import com.joanzapata.pdfview.PDFView;

import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Constants.URL;

/**
 * Created by shahzaibshahid on 24/01/2018.
 */

public class PdfViewerActivity extends AppCompatActivity {
    WebView mWebView;
    String url;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(DATA_TYPE);
            url = bundle.getString(URL);
            getSupportActionBar().setTitle(type);
            mWebView=findViewById(R.id.pdfview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
        } else {
            Toast.makeText(PdfViewerActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
