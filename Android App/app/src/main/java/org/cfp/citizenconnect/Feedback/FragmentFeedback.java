package org.cfp.citizenconnect.Feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import org.cfp.citizenconnect.Model.User;
import org.cfp.citizenconnect.PhoneVerificationActivity;
import org.cfp.citizenconnect.R;
import org.cfp.citizenconnect.SendEmail;

import java.io.IOException;
import java.util.Arrays;

import needle.Needle;

import static android.app.Activity.RESULT_OK;
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
    Spinner spinner;
    static final int REQUEST_AUTHORIZATION = 4;
    static final int REQUEST_PHONE_VERIFICATION = 626;
    public static String BUNDLE_PHONE_VERIFY = "phoneNumber";
    Context mContext;
    static ProgressDialog progressBar;

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
        mContext = getActivity();
        send.setOnClickListener(view -> {
            if (feildVerifications()) {
                Intent verifyContact = new Intent(getActivity(), PhoneVerificationActivity.class);
                verifyContact.putExtra(BUNDLE_PHONE_VERIFY, contactNumber.getText().toString());
                startActivityForResult(verifyContact, REQUEST_PHONE_VERIFICATION);
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
        Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHONE_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                sendMessage();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUTHORIZATION:
                sendMessage();
                break;
        }
    }

    private boolean feildVerifications() {
        if (fullName.getText().toString().equals("") ||
                contactNumber.getText().toString().equals("") || Message.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

}
