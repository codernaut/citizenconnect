package org.cfp.citizenconnect.Feedback;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import org.cfp.citizenconnect.Model.MessageEvent;
import org.cfp.citizenconnect.Model.User;
import org.cfp.citizenconnect.PhoneVerificationActivity;
import org.cfp.citizenconnect.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.cfp.citizenconnect.CitizenConnectApplication.mRequestQueue;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.SCOPES;


/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentFeedback extends Fragment {
    static GoogleAccountCredential mCredential;
    User user;
    Button send;
    EditText fullName, contactNumber, Message;
    Spinner feedBackType;
    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 4;
    static final int REQUEST_PHONE_VERIFICATION = 626;
    public static String BUNDLE_PHONE_VERIFY = "phoneNumber";
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 3;

    public static FragmentFeedback newInstance() {
        FragmentFeedback fragmentFeedback = new FragmentFeedback();
        return fragmentFeedback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_fragment, container, false);
        user = User.getUserInstance(realm);
        send = rootView.findViewById(R.id.sendMessage);
        fullName = rootView.findViewById(R.id.fullNameET);
        contactNumber = rootView.findViewById(R.id.ContactNumberET);
        Message = rootView.findViewById(R.id.messageET);
        feedBackType = rootView.findViewById(R.id.subject);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
        send.setOnClickListener(view -> {
            if (fieldVerifications()) {
                phoneVerification();
            } else {
                Toast.makeText(getActivity(), "Please Enter all details", Toast.LENGTH_LONG).show();
            }

        });
        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mCredential.setSelectedAccountName(user.getEmail());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.status) {

        } else {

        }
    }

    public void sendMessage() {
        Toast.makeText(getActivity(),"Sending",Toast.LENGTH_LONG).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.sendEmailURL),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        Toast.makeText(getActivity(),"Sent!",Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", fullName.getText().toString());
                params.put("contactNo", contactNumber.getText().toString());
                params.put("feedbackType", feedBackType.getSelectedItem().toString());
                params.put("message", Message.getText().toString());
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHONE_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                sendMessage();
            }
        }
        if (requestCode == REQUEST_ACCOUNT_PICKER) {
            if (resultCode == RESULT_OK && data != null &&
                    data.getExtras() != null) {
                String accountName =
                        data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    user.setEmail(accountName);
                    mCredential.setSelectedAccountName(accountName);
                    phoneVerification();
                } else {
                    Toast.makeText(getActivity(), "No Account details provided", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), "Failed to send your message Feedback", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean fieldVerifications() {
        if (fullName.getText().toString().equals("") ||
                contactNumber.getText().toString().equals("") || Message.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void phoneVerification() {
        Intent verifyContact = new Intent(getActivity(), PhoneVerificationActivity.class);
        verifyContact.putExtra(BUNDLE_PHONE_VERIFY, contactNumber.getText().toString());
        startActivityForResult(verifyContact, REQUEST_PHONE_VERIFICATION);
    }
}
