package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.fragment.CustomersFragment;
import com.nabdroid.easyshop.fragment.InventoryFragment;
import com.nabdroid.easyshop.fragment.SettingsFragment;
import com.nabdroid.easyshop.fragment.SuppliersFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNav);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        try {
            Intent intent = getIntent();
            int pageId = intent.getIntExtra("loadPage",0);
            if (pageId == 1){
                addFragment(new InventoryFragment());

            } else if (pageId == 2){
                addFragment(new SuppliersFragment());

            } else if (pageId == 3){
                addFragment(new CustomersFragment());
            } else {
                addFragment(new InventoryFragment());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.inventory_nav:
                    replaceFragment(new InventoryFragment());
                    return true;


                case R.id.suppliers_nav:
                    replaceFragment(new SuppliersFragment());
                    return true;


                case R.id.customers_nav:
                    replaceFragment(new CustomersFragment());
                    return true;


                case R.id.settings_nav:
                    replaceFragment(new SettingsFragment());
                    return true;
            }
            return false;
        }
    };


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFrameLayout, fragment);
        ft.commit();
    }


    private void addFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.mainFrameLayout, fragment);
        ft.commit();
    }


}