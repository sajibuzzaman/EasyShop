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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.model.Shop;

public class RegistrationActivity extends AppCompatActivity {
    private EditText shopNameEditText, registrationEmailEditText, registrationPasswordEditText, registrationConfirmPasswordEditText;
    private Button registrationButton;
    private TextView gotoLoginTextView;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    processRegistrationForm();
                } catch (Exception e) {
                    Toast.makeText(RegistrationActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gotoLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    private void processRegistrationForm() {
        String shopName = shopNameEditText.getText().toString().trim();
        String email = registrationEmailEditText.getText().toString().trim();
        String password = registrationPasswordEditText.getText().toString().trim();
        String confirmPassword = registrationConfirmPasswordEditText.getText().toString().trim();
        if (password.equals(confirmPassword)){
            createUserInFirebase(shopName, email, password);
        }
    }

    private void createUserInFirebase(String shopName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String userId = mAuth.getCurrentUser().getUid();
                    Toast.makeText(RegistrationActivity.this, "UserID: "+userId+"", Toast.LENGTH_SHORT).show();
                    saveNewUsersDataInDatabase(userId, shopName, email, password);
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void saveNewUsersDataInDatabase(String userId, String shopName, String email, String password) {
        DatabaseReference userRef = databaseReference.child("UserInfo").child(userId);
        Shop shop = new Shop(shopName, email, password);
        userRef.setValue(shop).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                if (task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Error while uploading the data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        shopNameEditText = findViewById(R.id.registerShopNameEditTextID);
        registrationEmailEditText = findViewById(R.id.registerEmailEditTextID);
        registrationPasswordEditText = findViewById(R.id.registerPasswordEditTextID);
        registrationPasswordEditText = findViewById(R.id.registerPasswordEditTextID);
        registrationConfirmPasswordEditText = findViewById(R.id.registerConfirmPasswordEditTextID);
        registrationButton = findViewById(R.id.registerButtonID);
        gotoLoginTextView = findViewById(R.id.goForLoginTextView);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}