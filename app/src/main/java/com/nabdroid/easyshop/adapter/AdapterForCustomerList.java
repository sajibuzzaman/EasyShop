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
import com.nabdroid.easyshop.model.Customer;
import com.nabdroid.easyshop.activity.CustomerDetailsActivity;
import com.nabdroid.easyshop.R;

import java.util.ArrayList;

public class AdapterForCustomerList extends RecyclerView.Adapter<AdapterForCustomerList.ViewHolder>{
    private ArrayList<Customer> customers;
    private Context context;
    private DatabaseReference databaseReference;
    private String uId;


    public AdapterForCustomerList(ArrayList<Customer> customers, Context context, String userID) {
        this.customers = customers;
        this.context = context;
        this.uId = userID;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("customers");
    }

    @NonNull
    @Override
    public AdapterForCustomerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_customer_recyclerview, parent, false);
        AdapterForCustomerList.ViewHolder viewHolder = new AdapterForCustomerList.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForCustomerList.ViewHolder holder, int position) {
        Customer customer = customers.get(position);
        holder.customerNameTV.setText(customer.getCustomerName());
        holder.customerAddressTV.setText(customer.getNumber());
        holder.dueAmountTV.setText("Due: "+customer.getDue()+" BDT");
        if (customer.isVisibilityCheck()){
            holder.deleteIV.setVisibility(View.GONE);
        }





        holder.rootCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerDetailsActivity.class);
                intent.putExtra("name", customer.getCustomerName());
                intent.putExtra("number", customer.getNumber());
                intent.putExtra("due", customer.getDue());
                context.startActivity(intent);
            }
        });


        holder.rootCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteIV.setVisibility(View.VISIBLE);
                customer.setVisibilityCheck(false);
                return true;
            }
        });

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomer(customer.getCustomerName());
            }
        });





    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTV, customerAddressTV, dueAmountTV;
        CardView rootCardView;
        ImageView deleteIV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTV = itemView.findViewById(R.id.single_customer_name_textview);
            customerAddressTV = itemView.findViewById(R.id.single_customer_address_textview);
            dueAmountTV = itemView.findViewById(R.id.single_customer_due_amount_textview);
            rootCardView = itemView.findViewById(R.id.single_customer_cardview_id);
            deleteIV = itemView.findViewById(R.id.delete_customer_IV);
        }

    }

    private void deleteCustomer(String customerName) {
        DatabaseReference productReference = databaseReference.child(customerName);
        productReference.removeValue();
    }



}
