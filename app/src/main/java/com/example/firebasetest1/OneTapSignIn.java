package com.example.firebasetest1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class OneTapSignIn extends AppCompatActivity {

    private static final String TAG = "One Tap Sign In";
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    TextView account_oneTap_tv;
    Button logOut_oneTap_btn;
    SignInButton oneTapSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_tap_sign_in);
        account_oneTap_tv = findViewById(R.id.tv_oneTap_account);
        logOut_oneTap_btn = findViewById(R.id.btn_oneTap_logOut);
        logOut_oneTap_btn.setEnabled(false);
        oneTapSignIn = findViewById(R.id.oneTap_signIn);



        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();



        ActivityResultLauncher<IntentSenderRequest> intentSenderActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            try {
                                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                                String idToken = credential.getGoogleIdToken();
                                String username = credential.getId();
                                String password = credential.getPassword();

                                logOut_oneTap_btn.setEnabled(true);
                                Log.d("laLala", "idToken: "+idToken +"\n" + "password: "+password + "\n");

                                if (idToken != null) {
                                    // Got an ID token from Google. Use it to authenticate
                                    // with your backend.
                                    account_oneTap_tv.setText(username);
                                    Log.d(TAG, "Got ID token.");
                                } else if (password != null) {
                                    // Got a saved username and password. Use them to authenticate
                                    // with your backend.
                                    Log.d(TAG, "Got password.");
                                }
                            } catch (ApiException e) {
                                switch (e.getStatusCode()) {
                                    case CommonStatusCodes.CANCELED:
                                        Log.d(TAG, "One-tap dialog was closed.");
                                        // Don't re-prompt the user.
                                        break;
                                    case CommonStatusCodes.NETWORK_ERROR:
                                        Log.d(TAG, "One-tap encountered a network error.");
                                        // Try again or just ignore.
                                        break;
                                    default:
                                        Log.d(TAG, "Couldn't get credential from result."
                                                + e.getLocalizedMessage());
                                        break;
                                }
                            }
                        }
                    }
                });

        oneTapSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(OneTapSignIn.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {

                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                intentSenderActivityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(OneTapSignIn.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No saved credentials found. Launch the One Tap sign-up flow, or
                                // do nothing and continue presenting the signed-out UI.
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });
            }
        });


        logOut_oneTap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.signOut();
                account_oneTap_tv.setText(null);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQ_ONE_TAP:
//                try {
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    String username = credential.getId();
//                    String password = credential.getPassword();
//
//                    logOut_oneTap_btn.setEnabled(true);
//                    Log.d("laLala", "idToken: "+idToken +"\n" + "password: "+password + "\n");
//
//                    if (idToken != null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with your backend.
//                        Log.d(TAG, "Got ID token.");
//                    } else if (password != null) {
//                        // Got a saved username and password. Use them to authenticate
//                        // with your backend.
//                        Log.d(TAG, "Got password.");
//                    }
//                } catch (ApiException e) {
//                    switch (e.getStatusCode()) {
//                        case CommonStatusCodes.CANCELED:
//                            Log.d(TAG, "One-tap dialog was closed.");
//                            // Don't re-prompt the user.
//                            showOneTapUI = false;
//                            oneTap_progressBar.setVisibility(View.VISIBLE);
//                            break;
//                        case CommonStatusCodes.NETWORK_ERROR:
//                            Log.d(TAG, "One-tap encountered a network error.");
//                            // Try again or just ignore.
//                            break;
//                        default:
//                            Log.d(TAG, "Couldn't get credential from result."
//                                    + e.getLocalizedMessage());
//                            break;
//                    }
//                }
//                break;
//        }
//    }
}