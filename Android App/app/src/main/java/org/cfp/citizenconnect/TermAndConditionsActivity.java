package org.cfp.citizenconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TermAndConditionsActivity extends AppCompatActivity {
    WebView link_to_websites, GoverningLaw,Changes,ContactUs;
    WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        getSupportActionBar().setTitle("Terms and Conditions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        link_to_websites = findViewById(R.id.link_to_other_websites);
        GoverningLaw = findViewById(R.id.governingLaw);
        Changes = findViewById(R.id.changes);
        ContactUs = findViewById(R.id.contactUs);

        String body = "<html><body style=\"text-align:justify;color:#9A9A9A\"> %s </body></Html>";
        link_to_websites.loadData(String.format(body,getString(R.string.links_to_other_websites)), "text/html", "utf-8");
        GoverningLaw.loadData(String.format(body,getString(R.string.governing_law)), "text/html", "utf-8");
        Changes.loadData(String.format(body,getString(R.string.changes)), "text/html", "utf-8");
        ContactUs.loadData(String.format(body,getString(R.string.contact_us)), "text/html", "utf-8");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
