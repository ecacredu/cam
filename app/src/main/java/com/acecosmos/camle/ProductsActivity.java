package com.acecosmos.camle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity {
    DatabaseReference firebaseRef;
    private FirebaseAuth mAuth;

    ProgressDialog p;

    Spinner productType;

    HashMap<String, String> product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        product = new HashMap<>();

        p=new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseRef = database.getReference("");
        mAuth = FirebaseAuth.getInstance();

        productType=(Spinner) findViewById(R.id.productType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.productTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productType.setAdapter(adapter);
    }

    public void addNewUser(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }


    public void addProduct(View view) {

        if(mAuth.getCurrentUser().getUid().toString()==null){
            Toast.makeText(ProductsActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
            return;
        }

        final TextView productBrand = (TextView) findViewById(R.id.productBrand);
        final TextView productModelNumber = (TextView) findViewById(R.id.productModelNumber);
        final TextView rent1Day = (TextView) findViewById(R.id.rent1Day);
        final TextView rent3Day = (TextView) findViewById(R.id.rent3Day);
        final TextView rent5Day = (TextView) findViewById(R.id.rent5Day);
        final TextView salePrice = (TextView) findViewById(R.id.salePrice);


        product.put("type",productType.getSelectedItem().toString().trim());
        product.put("brand",productBrand.getText().toString().trim());
        product.put("modelNumber",productModelNumber.getText().toString().trim());
        product.put("rent1Day",rent1Day.getText().toString().trim());
        product.put("rent3Day",rent3Day.getText().toString().trim());
        product.put("rent5Day",rent5Day.getText().toString().trim());
        product.put("user",mAuth.getCurrentUser().getUid().toString().trim());

        p.show();
        p.setMessage("Adding Your Product");
        p.setCancelable(false);
        Log.d("com.acecosmos.camle",product.toString());
        final String newProductKey = firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("products").push().getKey();
        firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("products/"+newProductKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                    firebaseRef.child("products/"+newProductKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            p.dismiss();
                        }
                    });
                    product.clear();

                    this.clearForm();

                    Toast.makeText(ProductsActivity.this, "Product Added Successfully", Toast.LENGTH_LONG);
                }
                else {
                    Toast.makeText(ProductsActivity.this, "Could Not Add The Product..", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }


            }

            private void clearForm() {
                productBrand.setText("");
                productModelNumber.setText("");
                rent1Day.setText("");
                rent3Day.setText("");
                rent5Day.setText("");
                salePrice.setText("");
                productType.setSelection(0);
            }


        });


    }


    }

