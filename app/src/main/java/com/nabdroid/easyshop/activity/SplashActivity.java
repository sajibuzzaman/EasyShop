package com.nabdroid.easyshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nabdroid.easyshop.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        choseUserMode();
    }


    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    private void choseUserMode() {

        if (currentUser != null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));

        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    }


}