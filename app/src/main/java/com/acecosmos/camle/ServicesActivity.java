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

import com.acecosmos.camle.adapters.FitServicesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import co.ceryle.fitgridview.FitGridView;

public class ServicesActivity extends AppCompatActivity {

    private FitGridView fitgridView;

    private Button mAddService;

    DatabaseReference firebaseRef;
    private FirebaseAuth mAuth;

    Boolean isEdit;
    HashMap<String,Object> services;

    FitServicesAdapter fitGridAdapter;
    ProgressDialog p;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Select you Services");
        p=new ProgressDialog(this);


        isEdit = getIntent().getBooleanExtra("isEdit",false);
        services = (HashMap<String, Object>) getIntent().getSerializableExtra("services");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseRef = database.getReference("");
        mAuth = FirebaseAuth.getInstance();

        findViews();

    }

  private void findViews() {
    fitgridView = (FitGridView) findViewById(R.id.fitgridView);



    if(isEdit){
      fitGridAdapter = new FitServicesAdapter(this,services);
    }else{
      fitGridAdapter = new FitServicesAdapter(this);
    }
    fitgridView.setFitGridAdapter(fitGridAdapter);

    //    if(isEdit){
//      if(services.size() > 0){
//        Iterator it = services.entrySet().iterator();
//        while (it.hasNext()) {
//          HashMap.Entry pair = (HashMap.Entry)it.next();
//          int pos = fitGridAdapter.getServicePos(pair.getKey().toString().replace("_","/"));
//          fitGridAdapter.addSelectedPosition(pos,fitgridView.getAdapter().getView(pos,null,fitgridView));
//          it.remove(); // avoids a ConcurrentModificationException
//        }
//      }
//    }

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
      addServices();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

    public void addServices() {

        ArrayList<Integer> servicePositions = fitGridAdapter.getSelectedPositions();

        if(servicePositions.size()==0){
          Toast.makeText(ServicesActivity.this, "", Toast.LENGTH_LONG).show();
          return;
        }
        p.show();
        p.setMessage("Adding services to the user");
        p.setCancelable(false);
        if(mAuth.getCurrentUser().getUid().toString()==null){
            Toast.makeText(ServicesActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
            return;
        }
        firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("services").setValue(null);

        for(Integer service : servicePositions){
          String tempName = fitGridAdapter.getService(service).replace("/","_");
            firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("services/"+tempName).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    p.dismiss();
                    Toast.makeText(ServicesActivity.this, "User Registered Succesfully", Toast.LENGTH_LONG);

                    if(isEdit){
                      Intent returnIntent = new Intent();
                      setResult(Activity.RESULT_OK, returnIntent);
                      finish();
                    }else{
                      startActivity(new Intent(ServicesActivity.this, ProductSelectionActivity.class));
                    }
                }



            });
        }
    }
}
