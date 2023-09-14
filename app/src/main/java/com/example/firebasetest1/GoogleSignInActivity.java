package com.example.firebasetest1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class GoogleSignInActivity extends Activity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 465;
    SignInButton signInButton;
    TextView account_gg_tv,email_gg_tv;
    Button logOut_gg_btn;

    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        signInButton = findViewById(R.id.button_google_sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        account_gg_tv = findViewById(R.id.tv_gg_signIn);
        email_gg_tv = findViewById(R.id.tv_gg_email);
        logOut_gg_btn = findViewById(R.id.btn_gg_logOut);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        signInButton.setOnClickListener(this);
        logOut_gg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_google_sign_in:
                signIn();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account );
        }catch (ApiException e){
            Log.w("handle sign in result", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void logOut(){
//        googleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ... This code clears which account is connected to the app. To sign in again, the user must choose their account again.
//                    }
//                });
        googleSignInClient.signOut();
        updateUI(null);
    }
    private void updateUI(GoogleSignInAccount account){
        if (account != null){
            account_gg_tv.setText(account.getDisplayName());
            email_gg_tv.setText(account.getEmail());

            signInButton.setVisibility(View.GONE);
            logOut_gg_btn.setEnabled(true);
        } else {
            account_gg_tv.setText("account");
            email_gg_tv.setText("email");
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            signInButton.setVisibility(View.VISIBLE);
            logOut_gg_btn.setEnabled(false);
        }
    }

}