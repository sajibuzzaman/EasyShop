package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nabdroid.easyshop.R;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailEditText, loginPasswordEditText;
    private Button loginButton;
    private TextView gotoRegistrationTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   String email = loginEmailEditText.getText().toString().trim();
                   String password = loginPasswordEditText.getText().toString().trim();
                   loginUser(email, password);
               } catch (Exception e) {
                   Toast.makeText(LoginActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
               }
            }
        });



        gotoRegistrationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        loginEmailEditText = findViewById(R.id.loginEmailEditTextID);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditTextID);
        loginButton = findViewById(R.id.loginButtonID);
        gotoRegistrationTextView = findViewById(R.id.goForRegistrationTextView);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uId = mAuth.getCurrentUser().getUid();
                    Toast.makeText(LoginActivity.this, "Uid: ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "Couldent login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}