package com.example.firebasetest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login.java";
    TextInputEditText email_edt, pass_edt;
    Button loginBtn;
    ProgressBar login_progressBar;
    TextView toRegister_textView, signInWithGoogle, oneTapSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email_edt =findViewById(R.id.email);
        pass_edt = findViewById(R.id.password);



        loginBtn = findViewById(R.id.btn_login);

        login_progressBar = findViewById(R.id.progressBar);

        toRegister_textView = findViewById(R.id.tv_toRegister);
        signInWithGoogle = findViewById(R.id.tv_signInWithGoogle);
        oneTapSignIn = findViewById(R.id.tv_oneTap_signIn);

        oneTapSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, OneTapSignIn.class);
                startActivity(intent);
                finish();
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, GoogleSignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        toRegister_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_progressBar.setVisibility(View.VISIBLE);

                String email, password;

                // use valueOf to catch "null"
                email = String.valueOf(email_edt.getText());
                password = String.valueOf(pass_edt.getText());
//                email = Objects.requireNonNull(editTextEmail.getText()).toString();
//                password = Objects.requireNonNull(editTextPassword.getText()).toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                login_progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
//            reload();
        }
    }

    private void reload(){

        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}