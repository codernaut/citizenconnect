package org.cfp.citizenconnect.Popups;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.cfp.citizenconnect.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.cfp.citizenconnect.CitizenConnectApplication.mRequestQueue;

public class DataSetComplainDialog extends BaseFragment {
    EditText complainMsg;
    String name, address, type;
    ImageButton sendComplain;
    ProgressDialog progressDialog;

    public DataSetComplainDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        name = getArguments().getString("dataSetName");
        address = getArguments().getString("dataSetAddress");
        type = getArguments().getString("type");
        return inflater.inflate(R.layout.complain_dialog, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        complainMsg = view.findViewById(R.id.complainMsg);
        sendComplain = view.findViewById(R.id.sendBtn);
        sendComplain.setOnClickListener(view1 -> {
            sendComplain();
        });

    }

    public void sendComplain() {
        progressDialog.setMessage("Sending");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.sendComplainURL),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("succeess")) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Thank You", Toast.LENGTH_LONG).show();
                            dismissAllowingStateLoss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject", type);
                params.put("message", complainMsg.getText().toString());
                params.put("dataSetName", name);
                params.put("dataSetAddress", address);
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }
}
