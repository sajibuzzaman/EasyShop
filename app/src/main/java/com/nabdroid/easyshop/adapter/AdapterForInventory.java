package com.nabdroid.easyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nabdroid.easyshop.model.Product;
import com.nabdroid.easyshop.activity.ProductDetailsActivity;
import com.nabdroid.easyshop.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterForInventory extends RecyclerView.Adapter<AdapterForInventory.ViewHolder> {
    private ArrayList<Product> products;
    private Context context;
    private StorageReference mStorageRef;
    private String uId;
    private DatabaseReference databaseReference;

    public AdapterForInventory(ArrayList<Product> products, Context context, String userId) {
        this.products = products;
        this.context = context;
        this.uId = userId;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uId).child("products");
    }

    @NonNull
    @Override
    public AdapterForInventory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForInventory.ViewHolder holder, int position) {
        Product product = products.get(position);
        String name = product.getProductName();
        int quantity = product.getQuantity();
        int price = product.getUnitPrice();
        String supplier = product.getSupplier();
        holder.productNameTV.setText(name + "(" + quantity + ")");
        if (product.isVisibilityStatus()){
            holder.deleteThisProductTV.setVisibility(View.GONE);
        }
        downloadImages(name, holder);


        holder.singleProductCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product", name);
                intent.putExtra("quantity", quantity);
                intent.putExtra("price", price);
                intent.putExtra("supplier", supplier);
                context.startActivity(intent);
            }
        });

        holder.singleProductCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (product.isVisibilityStatus()){
                    holder.deleteThisProductTV.setVisibility(View.VISIBLE);
                    product.setVisibilityStatus(false);
                }
                return true;
            }
        });


        holder.deleteThisProductTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(name);
            }
        });

        if (quantity == 0) {
            holder.singleProductCV.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTV;
        private CardView singleProductCV;
        public ImageView productIV;
        private TextView deleteThisProductTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.productNameTVID);
            singleProductCV = itemView.findViewById(R.id.singleProductCVID);
            productIV = itemView.findViewById(R.id.single_product_image_IVID);
            deleteThisProductTV = itemView.findViewById(R.id.delete_TV);
        }
    }


    private void downloadImages(String name, ViewHolder holder) {
        mStorageRef = FirebaseStorage.getInstance().getReference().child(uId).child(name);
        try {
            File localFile = File.createTempFile(name, "jpg");
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.productIV.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteProduct(String productName) {
        DatabaseReference productReference = databaseReference.child(productName);
        productReference.removeValue();
    }

}
