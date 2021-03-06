package com.acecosmos.camle.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acecosmos.camle.R;
import com.acecosmos.camle.models.Product;
import com.acecosmos.camle.viewholders.ProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProductsFragment extends Fragment {

  public View mView;
  private RecyclerView recyclerView;
  private DatabaseReference mDatabase;
  FirebaseAuth firebaseAuth;
  FirebaseRecyclerAdapter<Product,ProductsViewHolder> firebaseAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_products, container, false);

    mView = v;
    firebaseAuth=FirebaseAuth.getInstance();
    return v;
  }

  @Override
  public void onStart() {
    super.onStart();

    recyclerView = (RecyclerView) mView.findViewById(R.id.product_recycler_view);

    recyclerView.setHasFixedSize(false);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

    mDatabase = FirebaseDatabase.getInstance().getReference().child("products");
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
  }

  public static ProductsFragment newInstance(String text) {

    ProductsFragment f = new ProductsFragment();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
  }
}
