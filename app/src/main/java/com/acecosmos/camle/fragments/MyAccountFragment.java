package com.acecosmos.camle.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.acecosmos.camle.MyProductsActivity;
import com.acecosmos.camle.ProductSelectionActivity;
import com.acecosmos.camle.R;
import com.acecosmos.camle.ServicesActivity;
import com.acecosmos.camle.adapters.FitServicesAdapter;
import com.acecosmos.camle.adapters.PrefAdapter;
import com.acecosmos.camle.extras.ClickListener;
import com.acecosmos.camle.extras.RecyclerTouchListener;
import com.acecosmos.camle.models.Pref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.ceryle.fitgridview.FitGridView;

/**
 * Created by sd on 28-04-2017.
 */

public class MyAccountFragment extends Fragment {

  private static final int REQ_SERVICES = 111;
  private static final int REQ_PRODUCTS = 222;
  private List<Pref> prefList = new ArrayList<>();
  private RecyclerView recyclerView;
  private PrefAdapter mAdapter;

  private HashMap<String, Object> services;
  private HashMap<Object, Object> products;

  private DatabaseReference firebaseDatabase;
  private FirebaseAuth mAuth;
  private View mView;

  String uid;
  DataSnapshot profileSnap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_myaccount, container, false);
    mView = v;

    mAuth = FirebaseAuth.getInstance();
    uid = mAuth.getCurrentUser().getUid().trim();

    services = new HashMap<>();
    products = new HashMap<>();
    firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users/" + uid);
    firebaseDatabase.keepSynced(true);

    firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        profileSnap = dataSnapshot;
        for (DataSnapshot sv : dataSnapshot.child("services").getChildren()) {
          services.put(sv.getKey(), sv.getValue());
        }
        for (DataSnapshot ps : dataSnapshot.child("products").getChildren()) {
          products.put(ps.child("type").getValue(), ps.getKey());
        }
        addPrefs();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

    recyclerView = (RecyclerView) v.findViewById(R.id.account_recycler_view);

    mAdapter = new PrefAdapter(prefList);

    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);

    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
      @Override
      public void onClick(View view, int position) {
        Pref p = prefList.get(position);
//        Toast.makeText(getContext(), p.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
        if (p.getTitle().equals("My Services")) {
          Intent i = new Intent(getContext(), ServicesActivity.class);
          i.putExtra("services", services);
          i.putExtra("isEdit", true);
          startActivityForResult(i, REQ_SERVICES);
        } else if (p.getTitle().equals("My Products")) {
          Intent i = new Intent(getContext(), MyProductsActivity.class);
          startActivity(i);
        } else if(p.getSummary().equals("Change first name")){
          EditProfile("First Name","firstName", p.getTitle(),position);
        } else if(p.getSummary().equals("Change last name")){
          EditProfile("Last Name","lastName", p.getTitle(),position);
        }else if(p.getSummary().equals("Change email id")){
          EditProfile("Email Id","email", p.getTitle(),position);
        }else if(p.getSummary().equals("Change mobile number")){
          EditProfile("Mobile Number","mobile", p.getTitle(),position);
        }else if(p.getSummary().equals("Change city")){
          EditProfile("City","city", p.getTitle(),position);
        }else if(p.getSummary().equals("Change state")){
          EditProfile("State","state", p.getTitle(),position);
        }else if(p.getSummary().equals("Change country")){
          EditProfile("Country","country", p.getTitle(),position);
        }else if(p.getSummary().equals("Select your preference")){
          EditProfession("profession",p.getTitle(),position);
        }
      }

      @Override
      public void onLongClick(View view, int position) {

      }
    }));

    return v;
  }

  private void addPrefs() {

    Pref p = new Pref(profileSnap.child("profile/firstName").getValue().toString(), "Change first name");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/lastName").getValue().toString(), "Change last name");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/email").getValue().toString(), "Change email id");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/mobile").getValue().toString(), "Change mobile number");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/profession").getValue().toString(), "Select your preference");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/city").getValue().toString(), "Change city");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/state").getValue().toString(), "Change state");
    prefList.add(p);

    p = new Pref(profileSnap.child("profile/country").getValue().toString(), "Change country");
    prefList.add(p);

    p = new Pref("My Services", "Change services you provide");
    prefList.add(p);

    p = new Pref("My Products", "Change your proucts");
    prefList.add(p);

    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQ_SERVICES) {
      Toast.makeText(getContext(), "Services updated.", Toast.LENGTH_SHORT).show();
      updateDataRef();
    } else if (requestCode == REQ_PRODUCTS) {
      Toast.makeText(getContext(), "Products updated.", Toast.LENGTH_SHORT).show();
      updateDataRef();
    }
  }

  private void EditProfile(String label, final String property, String defaultValue, final int position) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Edit Profile");

    TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.setMargins(16,8,8,16);
    final EditText input = new EditText(getContext());
    input.setLayoutParams(params);
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    input.setText(defaultValue);
    builder.setView(input);

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String newValue = input.getText().toString();
        submitEdit(property,newValue,position);
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    builder.show();
  }

  private void EditProfession(final String prop, String value, final int position){
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
    builderSingle.setTitle("Select Your Profession:-");

    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice, getResources().getStringArray(R.array.professions));

    int spinnerPosition = arrayAdapter.getPosition(value);

    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    builderSingle.setSingleChoiceItems(arrayAdapter, spinnerPosition, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        String newValue = arrayAdapter.getItem(which);
        submitEdit(prop,newValue,position);
        dialog.cancel();
      }
    });

//    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//      @Override
//      public void onClick(DialogInterface dialog, int which) {
//        String strName = arrayAdapter.getItem(which);
////        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
////        builderInner.setMessage(strName);
////        builderInner.setTitle("Your Selected Item is");
////        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////          @Override
////          public void onClick(DialogInterface dialog,int which) {
////            dialog.dismiss();
////          }
////        });
////        builderInner.show();
//      }
//    });
    builderSingle.show();
  }

  private void submitEdit(String property, final String newValue, final int position) {

    firebaseDatabase.child("profile/"+property).setValue(newValue).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(getContext(),"Profile Edited",Toast.LENGTH_SHORT).show();
        updateDataRef();
        prefList.get(position).setTitle(newValue);
        mAdapter.notifyDataSetChanged();
      }
    });

  }

  private void updateDataRef() {
    firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        profileSnap = dataSnapshot;
        services.clear();
        products.clear();
        for (DataSnapshot sv : dataSnapshot.child("services").getChildren()) {
          services.put(sv.getKey(), sv.getValue());
        }
        for (DataSnapshot ps : dataSnapshot.child("products").getChildren()) {
          products.put(ps.child("type").getValue(), ps.getKey());
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }

  public static MyAccountFragment newInstance(String text) {

    MyAccountFragment f = new MyAccountFragment();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
  }

  public void onPrefItemClicked(View view) {

  }
}
