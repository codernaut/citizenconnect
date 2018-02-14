package org.cfp.citizenconnect.Services;

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
import org.cfp.citizenconnect.Model.Services;
import org.cfp.citizenconnect.PdfViewerActivity;
import org.cfp.citizenconnect.R;

import java.util.ArrayList;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.DATA_TYPE;
import static org.cfp.citizenconnect.Constants.FILE_URL;
import static org.cfp.citizenconnect.Constants.SERVICES_REFFERENCE;
import static org.cfp.citizenconnect.Model.Layout.getLayout;
import static org.cfp.citizenconnect.Model.Services.getServices;
import static org.cfp.citizenconnect.Model.Services.isObjectExist;

/**
 * Created by shahzaibshahid on 22/01/2018.
 */

public class FragmentServices extends Fragment implements GridViewAdapter.OnItemClickListener {

    GridView mGridView;
    GridViewAdapter gridViewAdapter;
    ArrayList<Layout> mList;
    ProgressDialog progressDialog;

    public static FragmentServices newInstance() {
        FragmentServices fragmentServices = new FragmentServices();
        return fragmentServices;
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
        getLayout("SERVICES",database.getReference(SERVICES_REFFERENCE), response -> {
            gridViewAdapter = new GridViewAdapter(getActivity(), response, this);
            mGridView.setAdapter(gridViewAdapter);
            progressDialog.dismiss();
        }, error -> {
            Toast.makeText(FragmentServices.this.getActivity(), "Failed to connect", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
        return rootView;
    }

    @Override
    public void viewDataList(String type) {
        progressDialog.show();
        Services servicesModel = isObjectExist(type, realm);
        if (servicesModel != null) {
            progressDialog.dismiss();
            Intent i = new Intent(getActivity(), PdfViewerActivity.class);
            i.putExtra(DATA_TYPE, type);
            i.putExtra(FILE_URL, servicesModel.getFileUrl());
            startActivity(i);
        } else {
            getServices(response -> {
                if (response) {
                    progressDialog.dismiss();
                    Intent i = new Intent(getActivity(), PdfViewerActivity.class);
                    i.putExtra(DATA_TYPE, type);
                    i.putExtra(FILE_URL, isObjectExist(type, realm).getFileUrl());
                    startActivity(i);
                }
            }, error -> {
                progressDialog.dismiss();
            });
        }
    }
}
