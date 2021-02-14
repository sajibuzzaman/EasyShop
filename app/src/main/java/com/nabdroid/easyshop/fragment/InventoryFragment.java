package com.nabdroid.easyshop.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nabdroid.easyshop.activity.AddProductActivity;
import com.nabdroid.easyshop.model.Product;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.adapter.AdapterForInventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InventoryFragment extends Fragment {
    private FloatingActionButton addProductFAB;
    private RecyclerView productListRecyclerView;
    private AdapterForInventory adapterForInventory;
    private FirebaseAuth mAuth;
    private DatabaseReference productsReference;
    private DatabaseReference shopReference;
    private ArrayList<Product> products;
    private TextView tvShopname;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        init(view);
        getshopName();
        initRecyclerView(view);
        downloadProductsFromDatabase();





        addProductFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), AddProductActivity.class));
            }
        });






        return view;
    }

    private void getshopName() {
        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String shopName =snapshot.getValue(String.class);
               tvShopname.setText(shopName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void init(View view) {
        addProductFAB = view.findViewById(R.id.addNewProductFAB);
        mAuth = FirebaseAuth.getInstance();
        products = new ArrayList<>();
        String uId = mAuth.getCurrentUser().getUid();
        productsReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("products");
        shopReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("shopName");
        productListRecyclerView = view.findViewById(R.id.inventory_recyclerViewID);
        adapterForInventory = new AdapterForInventory(products, view.getContext(), uId);

        tvShopname = view.findViewById(R.id.tvShopname);

    }

    private void initRecyclerView(View view) {
        productListRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3, GridLayoutManager.VERTICAL, false));
        productListRecyclerView.setAdapter(adapterForInventory);
    }

    private void downloadProductsFromDatabase() {
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Product product = data.getValue(Product.class);
                        products.add(product);
                    }

                    adapterForInventory.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}