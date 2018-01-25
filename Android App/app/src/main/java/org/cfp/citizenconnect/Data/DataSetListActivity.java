package org.cfp.citizenconnect.Data;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.cfp.citizenconnect.Adapters.DataSetAdapter;
import org.cfp.citizenconnect.Model.DataSet;
import org.cfp.citizenconnect.R;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.RealmResults;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Model.DataSet.getDataSet;
import static org.cfp.citizenconnect.Model.DataSet.isObjectExist;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

public class DataSetListActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DataSetAdapter dataSetAdapter;
    RecyclerView recyclerView;
    String type;
    ArrayList<DataSet> list;
    MenuItem menuItem;
    LinearLayoutManager dataListLayout;
    DividerItemDecoration dividerItemDecoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataset_list_activity);
        recyclerView = findViewById(R.id.dataList_RV);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        dataListLayout = new LinearLayoutManager(DataSetListActivity.this, LinearLayoutManager.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                dataListLayout.getOrientation());
        recyclerView.setLayoutManager(dataListLayout);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(DATA_TYPE);
            getSupportActionBar().setTitle(type);
            list = isObjectExist(type);
            if (list.size() == 0) {

                getDataSet(type, response -> {
                    list = response;
                    updateAdapter();
                    progressDialog.dismiss();
                }, error -> {
                    progressDialog.dismiss();
                    Toast.makeText(DataSetListActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
                    finish();
                });
            } else {
                updateAdapter();
                progressDialog.dismiss();
            }

        } else {
            Toast.makeText(DataSetListActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

    public void updateAdapter() {
        dataSetAdapter = new DataSetAdapter(DataSetListActivity.this, list);
        recyclerView.setAdapter(dataSetAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            menuItem.collapseActionView();

            return true;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                updateQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                updateQuery(query);
                return true;
            }

        });
        return true;
    }

    private void updateQuery(String query) {
        list.clear();
        RealmResults<DataSet> dataSets = realm.where(DataSet.class).equalTo("dataSetType", type).contains("Name", query, Case.INSENSITIVE).findAll();
        for (DataSet dataSet : dataSets) {
            list.add(dataSet);
        }
        updateAdapter();
    }
}
