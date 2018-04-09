package org.cfp.citizenconnect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static org.cfp.citizenconnect.CitizenConnectApplication.mAuth;
import static org.cfp.citizenconnect.Feedback.FragmentFeedback.BUNDLE_PHONE_VERIFY;

/**
 * Created by shahzaibshahid on 28/01/2018.
 */

public class PhoneVerificationActivity extends AppCompatActivity {
    String mVerificationId;
    Button verify;
    EditText code;
    String phoneNumber;
    ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification_activity);
        verify = findViewById(R.id.submitCode);
        code = findViewById(R.id.code);
        progressDialog = new ProgressDialog(PhoneVerificationActivity.this);
        progressDialog.setTitle(getString(R.string.in_progress_msg));
        progressDialog.setMessage("Verifying...");
        getSupportActionBar().setTitle("Phone Verification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Phone Verification failed" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                progressDialog.dismiss();
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        verify.setOnClickListener(view -> {
            if (verify.getText().equals("Verify")) {
                progressDialog.show();
                if (!code.getText().toString().equals("")) {
                    verifyPhoneNumberWithCode(mVerificationId, code.getText().toString());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PhoneVerificationActivity.this, "Please Enter valid Code", Toast.LENGTH_LONG).show();
                }
            } else {
                progressDialog.show();
                resendVerificationCode(phoneNumber, mResendToken);
                verify.setText("Verify");
            }


        });
        if (getIntent().getExtras() != null) {
            phoneNumber = getIntent().getStringExtra(BUNDLE_PHONE_VERIFY);
            startPhoneNumberVerification(phoneNumber);
        } else {
            Toast.makeText(PhoneVerificationActivity.this, "Invalid Phone No", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            code.setText("Resend");
                            Toast.makeText(getApplicationContext(), "The verification code entered was invalid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                PhoneVerificationActivity.this,
                mCallbacks,
                token);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }
}
