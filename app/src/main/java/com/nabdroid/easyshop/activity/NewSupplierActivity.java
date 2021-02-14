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
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.model.Supplier;

public class NewSupplierActivity extends AppCompatActivity {
    private EditText supplierNameET, supplierAddressET, supplierDueET, supplierProductsET;
    private Button addSupplierButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_supplier);
        init();
        addSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplierName = supplierNameET.getText().toString().trim();
                String supplierAddress = supplierAddressET.getText().toString().trim();
                int due =Integer.parseInt(supplierDueET.getText().toString().trim());
                String products = supplierProductsET.getText().toString().trim();
                String uId = mAuth.getCurrentUser().getUid();
                saveSupplier(uId, supplierName, supplierAddress, due, products);
            }
        });
    }



    private void init() {
        supplierNameET = findViewById(R.id.supplierNameEditTextID);
        supplierAddressET = findViewById(R.id.supplierAddressEditTextID);
        supplierDueET = findViewById(R.id.supplierDueEditTextID);
        supplierProductsET = findViewById(R.id.supplierProductsEditTextID);
        addSupplierButton = findViewById(R.id.addSupplierButtonID);
        mAuth = FirebaseAuth.getInstance();
    }


    private void saveSupplier(String uId, String supplierName, String supplierAddress, int due, String products) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("suppliers").child(supplierName);
        Supplier supplier = new Supplier(supplierName, supplierAddress, products, due);
        databaseReference.setValue(supplier).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NewSupplierActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewSupplierActivity.this, MainActivity.class);
                    intent.putExtra("loadPage", 2);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(NewSupplierActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}