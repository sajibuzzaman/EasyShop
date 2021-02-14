package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabdroid.easyshop.model.AmountHistory;
import com.nabdroid.easyshop.model.Customer;
import com.nabdroid.easyshop.R;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText customerNameEditText, customerAddressEditText, dueEditText;
    private Button addCustomerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference newCustomerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        init();
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = customerNameEditText.getText().toString().trim();
                String address = customerAddressEditText.getText().toString().trim();
                int due = Integer.parseInt(dueEditText.getText().toString().trim());
                String uId = mAuth.getCurrentUser().getUid();
                uploadCustomerDetails(uId, customerName, address, due);
            }
        });
    }



    private void init() {
        customerNameEditText = findViewById(R.id.customerNameEditTextID);
        customerAddressEditText = findViewById(R.id.customerAddressEditTextID);
        dueEditText = findViewById(R.id.customerDueEditTextID);
        addCustomerButton = findViewById(R.id.addCustomerButtonID);
        mAuth = FirebaseAuth.getInstance();
    }

    private void uploadCustomerDetails(String uId, String customerName, String address, int due) {
        newCustomerRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(customerName);
        Customer customer = new Customer(customerName, address, due);
        newCustomerRef.setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddCustomerActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    AmountHistory amountHistory = new AmountHistory(due, 0, "Due");
                    newCustomerRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(customerName).child("history").push();
                    newCustomerRef.setValue(amountHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
                            intent.putExtra("loadPage", 3);
                            startActivity(intent);
                        }
                    });


                }
                else {
                    Toast.makeText(AddCustomerActivity.this, "Error in data write", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}