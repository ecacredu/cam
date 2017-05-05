package com.acecosmos.camle;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.acecosmos.camle.extras.ClickListener;
import com.acecosmos.camle.extras.RecyclerTouchListener;
import com.acecosmos.camle.models.Product;
import com.acecosmos.camle.viewholders.ProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProductsActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private DatabaseReference mDatabase;
  FirebaseAuth firebaseAuth;
  FirebaseRecyclerAdapter<Product,ProductsViewHolder> firebaseAdapter;

  String uid;
  AlertDialog diaBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_products);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    toolbar.setTitle("My Products");


    findViews();
    setViews();
  }

  private void setViews() {

    recyclerView.setHasFixedSize(false);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    mDatabase = FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/products");
    mDatabase.keepSynced(true);

    firebaseAdapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(
      Product.class,
      R.layout.product_list_row,
      ProductsViewHolder.class,
      mDatabase
    ) {
      @Override
      protected void populateViewHolder(ProductsViewHolder productViewHolder, Product product, int i) {

        productViewHolder.setTitle(product.getBrand()+" "+product.getModelNumber());
        productViewHolder.setRent(product.getRent3Day());
        productViewHolder.setUser(product.getUserName());
        productViewHolder.setCity(product.getUserCity());
      }
    };

    recyclerView.setAdapter(firebaseAdapter);

    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
      @Override
      public void onClick(View view, int position) {
        Intent i = new Intent(MyProductsActivity.this,ProductsActivity.class);
        i.putExtra("prodKey",firebaseAdapter.getRef(position).getKey());
        i.putExtra("isEdit",true);
        startActivity(i);
      }

      @Override
      public void onLongClick(View view, int position) {
        confirmDelete(position);
      }
    }));
  }

  private void findViews() {
    firebaseAuth=FirebaseAuth.getInstance();
    uid = firebaseAuth.getCurrentUser().getUid();
    recyclerView = (RecyclerView) findViewById(R.id.myproduct_recycler_view);
  }
  private void confirmDelete(int position) {
    diaBox = AskOption(position);
    if(!diaBox.isShowing()){
      diaBox.show();
    }
  }

  private AlertDialog AskOption(final int position)
  {
    final DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference().child("products");

    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
      //set message, title, and icon
      .setTitle("Delete")
      .setMessage("Do you want to Delete")
      .setIcon(R.drawable.ic_delete)

      .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int whichButton) {
          //your deleting code
          final String prodKey = firebaseAdapter.getRef(position).getKey();

          firebaseAdapter.getRef(position).removeValue(
            new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                prodRef.child(prodKey).removeValue(new DatabaseReference.CompletionListener() {
                  @Override
                  public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    makeToast("Product Deleted !");
                  }
                });
              }
            }
          );


          dialog.dismiss();
        }

      })

      .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      })
      .create();
    return myQuittingDialogBox;

  }

  public void makeToast(String message){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.myproducts_selection_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_product_add) {
      Intent i = new Intent(MyProductsActivity.this,ProductsActivity.class);
      startActivity(i);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
