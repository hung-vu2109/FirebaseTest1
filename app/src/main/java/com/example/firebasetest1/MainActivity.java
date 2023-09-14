package com.example.firebasetest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasetest1.userObject.favouriteActivities;
import com.example.firebasetest1.userObject.userObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button logout_btn;
    TextView textView;
    FirebaseUser firebaseUser;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    EditText fullName_edt, age_edt, avatar_url_edt;
    Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logout_btn = findViewById(R.id.btn_main_logout);
        save_btn = findViewById(R.id.btn_save);

        textView = findViewById(R.id.tv_main_email);

        firebaseUser = mAuth.getCurrentUser();

        fullName_edt = findViewById(R.id.edt_fullName);
        age_edt = findViewById(R.id.edt_age);
        avatar_url_edt = findViewById(R.id.edt_avt_url);

        if (firebaseUser == null ){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(firebaseUser.getEmail());
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://my-first-project-640e5-default-rtdb.asia-southeast1.firebasedatabase.app/");
                databaseReference = rootNode.getReference("users1");
                userObject userObject1 = new userObject("le a", 16, new favouriteActivities(true, true));
                databaseReference.setValue(userObject1);
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}