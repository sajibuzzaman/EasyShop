package com.nabdroid.easyshop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nabdroid.easyshop.model.Product;
import com.nabdroid.easyshop.R;

import java.io.File;

public class AddProductActivity extends AppCompatActivity {
    private EditText productNameEditText, unitPriceEditText, quantityExitText, supplierEditText;
    private Button addNewProductButton;
    private TextView addImageTV;
    private ImageView productImageIV;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private StorageReference mStorageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        init();
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String productName = productNameEditText.getText().toString().trim();
                    int unitPrice = Integer.parseInt(unitPriceEditText.getText().toString().trim());
                    int quantity = Integer.parseInt(quantityExitText.getText().toString().trim());
                    String supplier = supplierEditText.getText().toString().trim();
                    mAuth = FirebaseAuth.getInstance();
                    String uId = mAuth.getCurrentUser().getUid();
                    uploadProducts(productName, unitPrice, quantity, supplier, uId);
                } catch (Exception e) {
                    Toast.makeText(AddProductActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addImageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void init() {
        productNameEditText = findViewById(R.id.newProductNameEditTextID);
        unitPriceEditText = findViewById(R.id.newProductUnitPriceEditTextID);
        quantityExitText = findViewById(R.id.newProductQuantityEditTextID);
        supplierEditText = findViewById(R.id.new_product_supplier_edit_textID);
        addNewProductButton = findViewById(R.id.submitNewProductButtonID);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addImageTV = findViewById(R.id.add_product_Image_TVID);
        productImageIV = findViewById(R.id.product_image_IVID);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    private void uploadProducts(String productName, int unitPrice, int quantity, String supplier, String uId) {
        DatabaseReference shopProductDatabase = databaseReference.child("UserInfo").child(uId).child("products").child(productName);
        Product product = new Product(productName, supplier, "no_image", quantity, unitPrice);
        shopProductDatabase.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    uploadImage(productName, uId);
                    Toast.makeText(AddProductActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(AddProductActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            productImageIV.setImageURI(imageUri);
        }
    }

    private void uploadImage(String productName, String uId) {

        StorageReference imageRef = mStorageRef.child(uId).child(productName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.ge;
                        Toast.makeText(AddProductActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(AddProductActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}