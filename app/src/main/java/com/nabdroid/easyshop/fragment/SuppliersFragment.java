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
import com.nabdroid.easyshop.activity.NewSupplierActivity;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.model.Supplier;
import com.nabdroid.easyshop.adapter.AdapterForSuppliers;

import java.util.ArrayList;


public class SuppliersFragment extends Fragment {
    private FloatingActionButton addNewSuppliersFAB;
    private RecyclerView suppliersRecyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<Supplier> suppliers;
    private AdapterForSuppliers adapterForSuppliers;
    private String uId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_suppliers, container, false);
        init(view);
        initRecyclerView(view);
        downloadSuppliersData(container.getContext());



        addNewSuppliersFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), NewSupplierActivity.class));
            }
        });





        return view;
    }

    private void init(View view) {
        addNewSuppliersFAB = view.findViewById(R.id.addNewSuppliersFABID);
        suppliersRecyclerView = view.findViewById(R.id.suppliers_recyclerViewID);
        suppliers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
    }

    private void initRecyclerView(View view) {
        adapterForSuppliers = new AdapterForSuppliers(suppliers, view.getContext(), uId);
        suppliersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        suppliersRecyclerView.setAdapter(adapterForSuppliers);

    }

    private void downloadSuppliersData(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("suppliers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                suppliers.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Supplier supplier = data.getValue(Supplier.class);
                        suppliers.add(supplier);
                    }
                }
                adapterForSuppliers.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }




}