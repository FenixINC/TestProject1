package com.taras.admin.app_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created on 12.03.2018
 * Author Taras
 */

public class AuthorizationActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;

    private EditText mUserName;
    private EditText mUserEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference("Authorization");

        mUserName = findViewById(R.id.user_name);
        mUserEmail = findViewById(R.id.user_email);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };

        findViewById(R.id.login).setOnClickListener(v -> {
            String userName = mUserName.getText().toString();
            String userEmail = mUserEmail.getText().toString();

            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail)) {
                mFirebaseAuth.signInWithEmailAndPassword(userName, userEmail).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.authorization_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }


}