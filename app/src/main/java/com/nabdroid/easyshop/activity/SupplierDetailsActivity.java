package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabdroid.easyshop.R;

public class SupplierDetailsActivity extends AppCompatActivity {
    private TextView supplierNameTV, supplierAddressTV, supplierDueTV;
    private EditText paidAmountET, newDueET;
    private Button reduceButton, addDueButton;
    String name, address, uId;
    int due;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SupplierDetailsActivity.this, MainActivity.class);
        intent.putExtra("loadPage", 2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_details);
        init();



        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("number");
        due = intent.getIntExtra("due", 0);

        supplierNameTV.setText(name);
        supplierAddressTV.setText(address);
        supplierDueTV.setText("Due: "+due);



        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int paidDue = Integer.parseInt(paidAmountET.getText().toString().trim());
                due = due - paidDue;
                supplierDueTV.setText("Due: "+due);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("suppliers").child(name).child("due");
                databaseReference.setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(SupplierDetailsActivity.this, MainActivity.class);
                            intent.putExtra("loadPage", 2);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SupplierDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        addDueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newDue = Integer.parseInt(newDueET.getText().toString().trim());
                due = due + newDue;
                supplierDueTV.setText("Due: "+due);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("suppliers").child(name).child("due");
                databaseReference.setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(SupplierDetailsActivity.this, MainActivity.class);
                            intent.putExtra("loadPage", 2);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SupplierDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });



    }

    private void init() {
        supplierNameTV = findViewById(R.id.name_supplier_details_activity_TVID);
        supplierAddressTV = findViewById(R.id.address_supplier_details_activity_TVID);
        supplierDueTV = findViewById(R.id.due_supplier_details_activity_TVID);

        paidAmountET = findViewById(R.id.paid_amount_supplier_details_activity_ETID);
        newDueET = findViewById(R.id.new_due_supplier_details_activity_ETID);

        reduceButton = findViewById(R.id.reduce_due_supplier_details_activity_BTID);
        addDueButton = findViewById(R.id.add_due_supplier_details_activity_BTID);

        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
    }
}