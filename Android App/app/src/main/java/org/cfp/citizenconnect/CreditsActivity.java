package org.cfp.citizenconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {
    Button asim, hamid,kiran,yusra,wahib,ayesha1,ayesha2,gulzaib,talha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits_activity);
        getSupportActionBar().setTitle("Credits");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        asim = findViewById(R.id.asim);
        hamid = findViewById(R.id.hamid);
        kiran = findViewById(R.id.kiran);
        yusra = findViewById(R.id.yusra);
        wahib = findViewById(R.id.Wahib);
        ayesha1 = findViewById(R.id.ayesha1);
        ayesha2 = findViewById(R.id.ayesha2);
        gulzaib = findViewById(R.id.gulzaib);
        talha = findViewById(R.id.talha);

        asim.setText("\u2022 Asim Ghaffar");
        hamid.setText("\u2022 Hamid");
        kiran.setText("\u2022 Kiran Majeed");
        yusra.setText("\u2022 Yusra");
        wahib.setText("\u2022 Wahib");
        ayesha1.setText("\u2022 Ayesha Mehmood");
        ayesha2.setText("\u2022 Ayesha Akhtar");
        gulzaib.setText("\u2022 Gul Zaib");
        talha.setText("\u2022 Talha Khan");

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
    public void asimProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.asim)));
        startActivity(browserIntent);
    }
    public void hamidProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.Hamid)));
        startActivity(browserIntent);
    }
    public void kiranProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.kiran)));
        startActivity(browserIntent);
    }
    public void yusraProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yusra)));
        startActivity(browserIntent);
    }
    public void wahibProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.wahib)));
        startActivity(browserIntent);
    }
    public void ayeshaMProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ayeshaMehmood)));
        startActivity(browserIntent);
    }
    public void ayeshaAProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ayeshaAkhtar)));
        startActivity(browserIntent);
    }
    public void gulzaibProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gulZaib)));
        startActivity(browserIntent);
    }
    public void talhaProfile(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.talhaKhan)));
        startActivity(browserIntent);
    }
}

