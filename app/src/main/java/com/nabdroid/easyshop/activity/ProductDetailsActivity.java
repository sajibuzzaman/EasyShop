package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabdroid.easyshop.R;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView nameTV, priceTV, supplierTV, quantityTV;
    private Button saveButton, cancelButton;
    private ImageView increase, decrease;
    private int quantity, price;
    private String uId;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        intent.putExtra("loadPage", 1);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();

        Intent intent = getIntent();
        String productName = intent.getStringExtra("product");
        quantity =  intent.getIntExtra("quantity", 0);
        price =  intent.getIntExtra("price", 0);
        String supplier = intent.getStringExtra("supplier");



        nameTV.setText(productName);
        priceTV.setText("Price: "+price);
        quantityTV.setText("Available: "+quantity);
        supplierTV.setText(supplier);


        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTV.setText("Available: "+quantity);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity--;
                quantityTV.setText("Available: "+quantity);
            }
        });











        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("products").child(productName).child("quantity");
                databaseReference.setValue(quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
                        }
                        else {
                            Toast.makeText(ProductDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });




    }

    private void init() {
        nameTV = findViewById(R.id.productNameDetailsActivityTVID);
        priceTV = findViewById(R.id.productPriceDetailsActivityTVID);
        supplierTV = findViewById(R.id.productSupplierDetailsActivityTVID);
        quantityTV = findViewById(R.id.productQuantityDetailsActivityTVID);
        saveButton = findViewById(R.id.saveNewQuantityButton);
        cancelButton = findViewById(R.id.cancleNewQuantityButton);
        increase = findViewById(R.id.incereaseIV);
        decrease = findViewById(R.id.decereaseIV);
        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
    }
}