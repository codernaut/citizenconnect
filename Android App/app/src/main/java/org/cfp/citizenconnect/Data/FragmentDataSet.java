package org.cfp.citizenconnect.Data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import org.cfp.citizenconnect.Adapters.GridViewAdapter;
import org.cfp.citizenconnect.Model.Layout;
import org.cfp.citizenconnect.R;

import java.util.ArrayList;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.Constants.DATASET_REFFERENCE;
import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Model.Layout.getLayout;

/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentDataSet extends Fragment implements GridViewAdapter.OnItemClickListener {

    GridView mGridView;
    GridViewAdapter gridViewAdapter;
    ArrayList<Layout> mList;
    ProgressDialog progressDialog;

    public static FragmentDataSet newInstance() {
        FragmentDataSet fragmentDataSet = new FragmentDataSet();
        return fragmentDataSet;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.in_progress_msg));
        progressDialog.show();
        progressDialog.setCancelable(false);
        mGridView = rootView.findViewById(R.id.dataSet_GridView);
        mList = new ArrayList<>();
        getLayout("DATASET", database.getReference(DATASET_REFFERENCE), response -> {
            gridViewAdapter = new GridViewAdapter(getActivity(), response, this);
            mGridView.setAdapter(gridViewAdapter);
            progressDialog.dismiss();
        }, error -> {
            Toast.makeText(FragmentDataSet.this.getActivity(), "Failed to connect", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
        return rootView;
    }

    @Override
    public void viewDataList(String type) {
        Intent i = new Intent(getActivity(), DataSetListActivity.class);
        i.putExtra(DATA_TYPE, type);
        startActivity(i);
    }
}
