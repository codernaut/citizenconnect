package org.cfp.citizenconnect.Feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.cfp.citizenconnect.Model.MessageEvent;
import org.cfp.citizenconnect.Model.User;
import org.cfp.citizenconnect.PhoneVerificationActivity;
import org.cfp.citizenconnect.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.cfp.citizenconnect.CitizenConnectApplication.mRequestQueue;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;


/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentFeedback extends Fragment {
    static final int REQUEST_PHONE_VERIFICATION = 626;
    public static String BUNDLE_PHONE_VERIFY = "phoneNumber";
    User user;
    Button send;
    EditText fullName, contactNumber, Message;
    TextView countryCode;
    Spinner feedBackType;

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
        countryCode = rootView.findViewById(R.id.countryCode);
        Message = rootView.findViewById(R.id.messageET);
        feedBackType = rootView.findViewById(R.id.subject);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && charSequence.toString().startsWith(" ")) {
                    fullName.getText().clear();
                    Snackbar.make(getActivity().findViewById(R.id.mainLayout), "Name can't start with space!", Snackbar.LENGTH_SHORT).show();
                } else if (charSequence.toString().contains("  ")) {
                    fullName.setText(charSequence.toString().replaceAll("  ", " "));
                }
                fullName.setSelection(fullName.getText().length());
//                fullName.setText(charSequence.toString().replaceAll("0",""));
//                Snackbar.make(getActivity().findViewById(R.id.mainLayout),"Name contains alphabets only!",Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && charSequence.toString().startsWith("0")) {
                    contactNumber.getText().clear();
                    Snackbar.make(getActivity().findViewById(R.id.mainLayout), "Number cannot start with 0!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (charSequence.toString().length() > 1 && charSequence.toString().startsWith("0")) {
                        if (charSequence.charAt(0) == '0') {
                            Snackbar.make(getActivity().findViewById(R.id.mainLayout), "Number cannot start with 0!", Snackbar.LENGTH_SHORT).show();
                            contactNumber.setText(charSequence.toString().substring(1));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send.setOnClickListener(view -> {
            if (fieldVerifications()) {
                phoneVerification();
            } else {
                Toast.makeText(getActivity(), "Please Enter all details", Toast.LENGTH_LONG).show();
            }

        });

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
        Toast.makeText(getActivity(), "Sending", Toast.LENGTH_LONG).show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.sendEmailURL), response -> {
            try {
                Log.i("response123 = ", response);
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                Toast.makeText(getActivity(), "Sent!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            clearToDefaults();
        },
                error -> {
                    error.printStackTrace();
                    clearToDefaults();
                }

      /*  StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.sendFeedbackURL),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        Toast.makeText(getActivity(), "Sent!", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()*/

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", fullName.getText().toString());
                params.put("contactNo", countryCode.getText().toString() + contactNumber.getText().toString());
                params.put("subject", feedBackType.getSelectedItem().toString());
                params.put("message", Message.getText().toString());
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private void clearToDefaults() {
        fullName.getText().clear();
        contactNumber.getText().clear();
        Message.getText().clear();
        feedBackType.setSelection(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHONE_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                sendMessage();
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
        verifyContact.putExtra(BUNDLE_PHONE_VERIFY, countryCode.getText().toString() + contactNumber.getText().toString());
        startActivityForResult(verifyContact, REQUEST_PHONE_VERIFICATION);
    }
}
