package com.acecosmos.camle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.acecosmos.camle.adapters.FitProductsAdapter;
import com.acecosmos.camle.adapters.FitServicesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import co.ceryle.fitgridview.FitGridView;

/**
 * Created by sd on 01-05-2017.
 */

public class ProductSelectionActivity  extends AppCompatActivity {

  private FitGridView fitgridView;

  int iteration=0;
  String userName;
  String userCity;
  String newProductKey;
  ArrayList<Integer> productPositions;
  HashMap<String, String> productMap;

  Boolean isEdit;
  HashMap<Object,Object> products;

  private Button mAddService;

  DatabaseReference firebaseRef;
  private FirebaseAuth mAuth;

  FitProductsAdapter fitGridAdapter;
  ProgressDialog p;

  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_selection);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    toolbar.setTitle("Select you Products");
    p=new ProgressDialog(this);

//    isEdit = getIntent().getBooleanExtra("isEdit",false);
//    products = (HashMap<Object, Object>) getIntent().getSerializableExtra("products");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    firebaseRef = database.getReference("");
    mAuth = FirebaseAuth.getInstance();

    findViews();

  }

  private void findViews() {
    fitgridView = (FitGridView) findViewById(R.id.fitgridProductView);
    fitGridAdapter = new FitProductsAdapter(this);

    fitgridView.setFitGridAdapter(fitGridAdapter);

    firebaseRef.child("users/"+mAuth.getCurrentUser().getUid()+"/profile").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.child("lastName").getValue() == null){
          userName = dataSnapshot.child("firstName").getValue().toString().trim();
        }else{
          userName = dataSnapshot.child("firstName").getValue().toString().trim()+" "+dataSnapshot.child("lastName").getValue().toString().trim();
        }
        userCity = dataSnapshot.child("city").getValue().toString().trim();
      }
      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.services_selection_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_services_done) {
      addProducts();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void addProducts() {

    String uid =mAuth.getCurrentUser().getUid().toString().trim();
    iteration=0;
    productPositions = fitGridAdapter.getSelectedPositions();

    if(productPositions.size()==0){
        Toast.makeText(ProductSelectionActivity.this, "", Toast.LENGTH_LONG).show();
        return;
    }

    if(mAuth.getCurrentUser().getUid().toString()==null){
      Toast.makeText(ProductSelectionActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
      return;
    }

    p.show();
    p.setMessage("Adding products");
    p.setCancelable(false);
    for (Integer productPos : productPositions){

      productMap=new HashMap<>();
      productMap.put("user",uid);
      productMap.put("userName",userName);
      productMap.put("userCity",userCity);
      productMap.put("type",fitGridAdapter.getProduct(productPos));
      newProductKey = firebaseRef.child("users").child(uid).child("products").push().getKey();

      firebaseRef.child("users/"+uid+"/products/"+newProductKey).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

        }
      });
      firebaseRef.child("products/"+newProductKey).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          iteration++;
          if(productPositions.size() == iteration){
            if(p.isShowing()){
              p.dismiss();
            }
            startActivity(new Intent(ProductSelectionActivity.this, HomeActivity.class));
          }
        }
      });
    }
  }


}

//if(isEdit){
//  fitGridAdapter = new FitProductsAdapter(this,products);
//  }else{
//
//  }

//  public void addProducts() {
//
//    String uid =mAuth.getCurrentUser().getUid().toString().trim();
//
//    iteration=0;
//
//    productPositions = fitGridAdapter.getSelectedPositions();
//
//    if(productPositions.size()==0){
//      if(!isEdit){
//        Toast.makeText(ProductSelectionActivity.this, "", Toast.LENGTH_LONG).show();
//        return;
//      }
//    }
//
//    if(mAuth.getCurrentUser().getUid().toString()==null){
//      Toast.makeText(ProductSelectionActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
//      return;
//    }
//
//    if(isEdit){
//      HashMap<Object,Object> tempProducts = fitGridAdapter.getProducts();
//      Toast.makeText(this,"Products size "+fitGridAdapter.getProducts().size(),Toast.LENGTH_SHORT).show();
//      Iterator it = fitGridAdapter.getProducts().entrySet().iterator();
//      while (it.hasNext()) {
//        HashMap.Entry pair = (HashMap.Entry)it.next();
//
//        Toast.makeText(this,fitGridAdapter.getRemovedKeys().size()+" | "+fitGridAdapter.getProducts().size(),Toast.LENGTH_SHORT).show();
//
//        for(String rk: fitGridAdapter.getRemovedKeys()){
//          if(rk.equals(pair.getValue())){
//            fitGridAdapter.getProducts().remove(pair.getKey());
//            firebaseRef.child("users/"+uid+"/products/"+rk).removeValue();
//            firebaseRef.child("products/"+rk).removeValue();
//          }
//        }
//        it.remove(); // avoids a ConcurrentModificationException
//      }
//
//      for (Integer productPos : fitGridAdapter.getNewlyAddedPositions()){
//
//        productMap=new HashMap<>();
//        productMap.put("user",uid);
//        productMap.put("userName",userName);
//        productMap.put("userCity",userCity);
//        productMap.put("type",fitGridAdapter.getProduct(productPos));
//        newProductKey = firebaseRef.child("users").child(uid).child("products").push().getKey();
//
//        firebaseRef.child("users/"+uid+"/products/"+newProductKey).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//          @Override
//          public void onComplete(@NonNull Task<Void> task) {
//
//          }
//        });
//        firebaseRef.child("products/"+newProductKey).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//          @Override
//          public void onComplete(@NonNull Task<Void> task) {
//            iteration++;
//            if(productPositions.size() == iteration){
//              if(p.isShowing()){
//                p.dismiss();
//              }
//
//            }
//          }
//        });
//      }
//
//      Intent returnIntent = new Intent();
//      setResult(Activity.RESULT_OK,returnIntent);
//      finish();
//
//    }else{
//
//
//    }
//
//
//  }
