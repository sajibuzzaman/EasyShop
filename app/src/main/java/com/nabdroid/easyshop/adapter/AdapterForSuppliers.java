package com.nabdroid.easyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.activity.SupplierDetailsActivity;
import com.nabdroid.easyshop.model.Supplier;

import java.util.ArrayList;

public class AdapterForSuppliers extends RecyclerView.Adapter<AdapterForSuppliers.ViewHolder>{
    private Context context;
    private ArrayList<Supplier> suppliers;
    private String uId;
    private DatabaseReference databaseReference;



    public AdapterForSuppliers(ArrayList<Supplier> suppliers, Context context, String uId) {
        this.context = context;
        this.suppliers = suppliers;
        this.uId = uId;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("suppliers");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_supplier_layout, parent, false);
        AdapterForSuppliers.ViewHolder viewHolder = new AdapterForSuppliers.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Supplier supplier = suppliers.get(position);
        holder.supplierNameTV.setText(supplier.getName());
        holder.supplierAddressTV.setText(supplier.getNumber());
        holder.dueAmountTV.setText("Due: "+supplier.getDue()+" BDT");

        if (supplier.isVisibilityStatus()){
            holder.deleteSupplierIV.setVisibility(View.GONE);
        }






        holder.rootCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SupplierDetailsActivity.class);
                intent.putExtra("name", supplier.getName());
                intent.putExtra("number", supplier.getNumber());
                intent.putExtra("due", supplier.getDue());
                context.startActivity(intent);
            }
        });



        holder.rootCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteSupplierIV.setVisibility(View.VISIBLE);
                supplier.setVisibilityStatus(false);
                return true;
            }
        });


        holder.deleteSupplierIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSupplier(supplier.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView supplierNameTV, supplierAddressTV, dueAmountTV;
        CardView rootCardView;
        ImageView deleteSupplierIV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            supplierNameTV = itemView.findViewById(R.id.single_supplier_name_textview);
            supplierAddressTV = itemView.findViewById(R.id.single_supplier_address_textview);
            dueAmountTV = itemView.findViewById(R.id.single_supplier_due_amount_textview);
            rootCardView = itemView.findViewById(R.id.single_supplier_cardview_id);
            deleteSupplierIV = itemView.findViewById(R.id.delete_supplier_IV);
        }
    }


    private void deleteSupplier(String supplierName) {
        DatabaseReference productReference = databaseReference.child(supplierName);
        productReference.removeValue();
    }
}
