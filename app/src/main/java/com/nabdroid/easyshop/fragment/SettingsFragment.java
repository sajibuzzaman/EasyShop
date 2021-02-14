package com.nabdroid.easyshop.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.activity.LoginActivity;
import com.nabdroid.easyshop.model.Shop;

public class SettingsFragment extends Fragment {
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private TextView shopNameTV, emailTV;
    private ImageView editNameIV, saveChangeIV, cancleChangeIV;
    private String uId;
    private EditText newNameET;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        downloadUserInfo();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(container.getContext(), LoginActivity.class));
            }
        });


        editNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNameET.setVisibility(View.VISIBLE);
                saveChangeIV.setVisibility(View.VISIBLE);
                cancleChangeIV.setVisibility(View.VISIBLE);
                shopNameTV.setVisibility(View.GONE);
                editNameIV.setVisibility(View.GONE);

            }
        });


        cancleChangeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNameET.setVisibility(View.GONE);
                saveChangeIV.setVisibility(View.GONE);
                cancleChangeIV.setVisibility(View.GONE);
                editNameIV.setVisibility(View.VISIBLE);
                shopNameTV.setVisibility(View.VISIBLE);
            }
        });


        saveChangeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String newName = newNameET.getText().toString().trim();
                    saveNewName(newName);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }

    private void downloadUserInfo() {
        uId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference dataRef = databaseReference.child("UserInfo").child(uId);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Shop shop = snapshot.getValue(Shop.class);
                    shopNameTV.setText(shop.getShopName());
                    emailTV.setText(shop.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveNewName(String newName) {
        DatabaseReference nameRef = databaseReference.child("UserInfo").child(uId).child("shopName");

        nameRef.setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    shopNameTV.setVisibility(View.VISIBLE);
                    shopNameTV.setText(newName);
                    newNameET.setVisibility(View.GONE);
                    saveChangeIV.setVisibility(View.GONE);
                    cancleChangeIV.setVisibility(View.GONE);
                    editNameIV.setVisibility(View.VISIBLE);


                }

            }
        });
    }

    private void init(View view) {
        logoutButton = view.findViewById(R.id.logout_BTNID);
        shopNameTV = view.findViewById(R.id.shop_name_settings_tv);
        emailTV = view.findViewById(R.id.email_settings_tv);
        editNameIV = view.findViewById(R.id.edit_name_IV);
        newNameET = view.findViewById(R.id.new_shop_name_settings_ET);
        saveChangeIV = view.findViewById(R.id.save_change);
        cancleChangeIV = view.findViewById(R.id.cancle_change);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }
}