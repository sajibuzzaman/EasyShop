package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.adapter.AdapterForCustomerPayHistory;
import com.nabdroid.easyshop.model.AmountHistory;

import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity {
    private TextView customerNameTV, customerAddressTV, customerDueTV;
    private EditText paidAmountET, newDueET;
    private Button reduceButton, addDueButton;
    private RecyclerView customerPayHistoryRecyclerView;
    private String name, number, uId;
    private int due;
    private ArrayList<AmountHistory> amountHistories;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private AdapterForCustomerPayHistory adapterForCustomerPayHistory;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerDetailsActivity.this, MainActivity.class);
        intent.putExtra("loadPage", 3);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        init();


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        due = intent.getIntExtra("due", 0);

        customerNameTV.setText(name);
        customerAddressTV.setText(number);
        customerDueTV.setText("Due: "+due);
        initRecyclerView();
        downloadHistory();

        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int paidDue = Integer.parseInt(paidAmountET.getText().toString().trim());
                due = due - paidDue;
                customerDueTV.setText("Due: "+due);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(name).child("due");
                databaseReference.setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            AmountHistory amountHistory = new AmountHistory(paidDue, 0, "Paid");
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(name).child("history").push();
                            databaseReference.setValue(amountHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(CustomerDetailsActivity.this, MainActivity.class);
                                    intent.putExtra("loadPage", 3);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(CustomerDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                customerDueTV.setText("Due: "+due);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(name).child("due");
                databaseReference.setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            AmountHistory amountHistory = new AmountHistory(newDue, 0, "Due");
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(name).child("history").push();
                            databaseReference.setValue(amountHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(CustomerDetailsActivity.this, MainActivity.class);
                                    intent.putExtra("loadPage", 3);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(CustomerDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });





    }

    private void downloadHistory() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers").child(name).child("history");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        AmountHistory amountHistory = data.getValue(AmountHistory.class);
                        amountHistories.add(amountHistory);
                    }
                }
                adapterForCustomerPayHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void init() {
        customerNameTV = findViewById(R.id.name_customer_details_activity_TVID);
        customerAddressTV = findViewById(R.id.address_customer_details_activity_TVID);
        customerDueTV = findViewById(R.id.due_customer_details_activity_TVID);
        customerPayHistoryRecyclerView = findViewById(R.id.customer_pay_history_recyclerview_id);

        paidAmountET = findViewById(R.id.paid_amount_customer_details_activity_ETID);
        newDueET = findViewById(R.id.new_due_customer_details_activity_ETID);

        reduceButton = findViewById(R.id.reduce_due_customer_details_activity_BTID);
        addDueButton = findViewById(R.id.add_due_customer_details_activity_BTID);

        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();

        amountHistories = new ArrayList<>();
        adapterForCustomerPayHistory = new AdapterForCustomerPayHistory(amountHistories);
    }


    private void initRecyclerView() {
        customerPayHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerPayHistoryRecyclerView.setAdapter(adapterForCustomerPayHistory);
    }

}