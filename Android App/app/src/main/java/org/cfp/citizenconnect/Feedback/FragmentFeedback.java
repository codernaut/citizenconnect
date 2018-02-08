package org.cfp.citizenconnect.Feedback;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;

import org.cfp.citizenconnect.Interfaces.Permissions;
import org.cfp.citizenconnect.Model.MessageEvent;
import org.cfp.citizenconnect.Model.User;
import org.cfp.citizenconnect.PhoneVerificationActivity;
import org.cfp.citizenconnect.R;
import org.cfp.citizenconnect.SendEmail;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;

import needle.Needle;

import static android.app.Activity.RESULT_OK;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.SCOPES;


/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentFeedback extends Fragment{
    static GoogleAccountCredential mCredential;


    User user;
    Button send;
    EditText fullName, contactNumber, Message;
    Spinner spinner;
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
        spinner = rootView.findViewById(R.id.subject);

        send.setOnClickListener(view -> {
            if (fieldVerifications()) {
                chooseAccount();
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
        if(event.status){
            chooseAccount();
        }
        else {

        }
    }

    public void sendMessage() {
        Needle.onBackgroundThread().execute(() -> {
            Looper.prepare();
            SendEmail sendEmail = new SendEmail(mCredential, "shahzaib.shahid414@gmail.com", spinner.getSelectedItem().toString(), Message.getText().toString());
            try {
                sendEmail.getDataFromApi();

            } catch (IOException e) {
                if (e instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) e).getIntent(),
                            REQUEST_AUTHORIZATION);
                }
                Toast.makeText(getActivity(), "Failed, Please try again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
        Toast.makeText(getActivity(), "Sending", Toast.LENGTH_LONG).show();
    }

    private void chooseAccount() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            user = User.getUserInstance(realm);
            if (user.getEmail() != null) {
                mCredential.setSelectedAccountName(user.getEmail());
                phoneVerification();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                    REQUEST_PERMISSION_GET_ACCOUNTS);
        }
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
            }
            else {
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
