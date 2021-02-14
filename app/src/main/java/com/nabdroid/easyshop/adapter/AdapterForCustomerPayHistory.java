package com.nabdroid.easyshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabdroid.easyshop.R;
import com.nabdroid.easyshop.model.AmountHistory;
import com.nabdroid.easyshop.model.Customer;

import java.util.ArrayList;

public class AdapterForCustomerPayHistory extends RecyclerView.Adapter<AdapterForCustomerPayHistory.ViewHolder>{
    private ArrayList<AmountHistory> amountHistories;

    public AdapterForCustomerPayHistory(ArrayList<AmountHistory> amountHistories) {
        this.amountHistories = amountHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_amount_history, parent, false);
        AdapterForCustomerPayHistory.ViewHolder viewHolder = new AdapterForCustomerPayHistory.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AmountHistory amountHistory = amountHistories.get(position);
        holder.amountTypeTV.setText(amountHistory.getAmountType());
        holder.amountTV.setText(amountHistory.getAmount()+" BDT");
    }

    @Override
    public int getItemCount() {
        return amountHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountTypeTV, amountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTypeTV = itemView.findViewById(R.id.amount_type_tvid);
            amountTV = itemView.findViewById(R.id.amount_tvid);
        }
    }
}
