 package com.nabdroid.easyshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabdroid.easyshop.activity.AddCustomerActivity;
import com.nabdroid.easyshop.model.Customer;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.adapter.AdapterForCustomerList;

import java.util.ArrayList;

public class CustomersFragment extends Fragment {
    private RecyclerView customersRecyclerView;
    private FloatingActionButton addCustomerFAB;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<Customer> customers;
    private AdapterForCustomerList adapterForCustomerList;
    private String uId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);
        init(view);
        initRecyclerView(view);
        downloadCustomersData(container.getContext());


        addCustomerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), AddCustomerActivity.class));
            }
        });


        return view;
    }


    private void init(View view) {
        customersRecyclerView = view.findViewById(R.id.customers_recyclerViewID);
        addCustomerFAB = view.findViewById(R.id.addCustomerFABID);
        mAuth = FirebaseAuth.getInstance();
        customers = new ArrayList<>();
        uId = mAuth.getCurrentUser().getUid();
        adapterForCustomerList = new AdapterForCustomerList(customers, view.getContext(), uId);
    }

    private void initRecyclerView(View view) {
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        customersRecyclerView.setAdapter(adapterForCustomerList);
    }

    private void downloadCustomersData(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customers.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Customer customer = data.getValue(Customer.class);
                        customers.add(customer);
                    }
                }
                adapterForCustomerList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

    }


}