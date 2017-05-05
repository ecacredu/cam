package com.acecosmos.camle.fragments;

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
import com.acecosmos.camle.models.User;
import com.acecosmos.camle.viewholders.ProductsViewHolder;
import com.acecosmos.camle.viewholders.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by sd on 28-04-2017.
 */

public class UsersFragment extends Fragment {

  public View mView;
  private RecyclerView recyclerView;
  private DatabaseReference mDatabase;
  FirebaseAuth firebaseAuth;
  FirebaseRecyclerAdapter<User,UsersViewHolder> firebaseAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_users, container, false);

    mView = v;
    firebaseAuth=FirebaseAuth.getInstance();
    return v;
  }

  @Override
  public void onStart() {
    super.onStart();

    recyclerView = (RecyclerView) mView.findViewById(R.id.user_recycler_view);

    recyclerView.setHasFixedSize(false);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    mDatabase.keepSynced(true);

    firebaseAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
      User.class,
      R.layout.user_list_row,
      UsersViewHolder.class,
      mDatabase
    ) {
      @Override
      protected void populateViewHolder(UsersViewHolder userViewHolder, User user, int i) {

        HashMap<String,Object> tempProfile = user.getProfile();

        userViewHolder.setName(tempProfile.get("firstName")+" "+tempProfile.get("lastName"));
      }
    };

    recyclerView.setAdapter(firebaseAdapter);
  }

  public static UsersFragment newInstance(String text) {

    UsersFragment f = new UsersFragment();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
  }
}
