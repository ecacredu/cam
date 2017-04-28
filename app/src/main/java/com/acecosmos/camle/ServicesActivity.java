package com.acecosmos.camle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {

    DatabaseReference firebaseRef;
    private FirebaseAuth mAuth;

    ProgressDialog p;

    ArrayList<String> selectedServices = new ArrayList<String>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        p=new ProgressDialog(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseRef = database.getReference("");
        mAuth = FirebaseAuth.getInstance();

    }

    public void serviceChanged(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.cinematography :
                if(checked){

                    selectedServices.add("cinematographhy");
                }
                else{
                    selectedServices.remove("cinematographhy");
                }
                break;

            case R.id.maternity :
                if(checked){
                    selectedServices.add("maternity");
                }
                else{
                    selectedServices.remove("maternity");
                }
                break;

            case R.id.traditional :
                if(checked){
                    selectedServices.add("traditional");
                }
                else{
                    selectedServices.remove("traditional");
                }
                break;

            case R.id.birthday :
                if(checked){
                    selectedServices.add("birthday");
                }
                else{
                    selectedServices.remove("birthday");
                }
                break;


            case R.id.videoEditing :
                if(checked){
                    selectedServices.add("videoEditing");
                }
                else{
                    selectedServices.remove("videoEditing");
                }
                break;


            case R.id.preWedding :
                if(checked){
                    selectedServices.add("preWedding");
                }
                else{
                    selectedServices.remove("preWedding");
                }
                break;


            case R.id.kids :
                if(checked){
                    selectedServices.add("kids");
                }
                else{
                    selectedServices.remove("kids");
                }
                break;


            case R.id.events :
                if(checked){
                    selectedServices.add("events");
                }
                else{
                    selectedServices.remove("events");
                }
                break;


            case R.id.commercial :
                if(checked){
                    selectedServices.add("commercial");
                }
                else{
                    selectedServices.remove("commercial");
                }
                break;


            case R.id.makeup :
                if(checked){
                    selectedServices.add("makeup");
                }
                else{
                    selectedServices.remove("makeup");
                }
                break;


            case R.id.conceptionAndArtisticDirection :
                if(checked){
                    selectedServices.add("conceptionAndArtisticDirection");
                }
                else{
                    selectedServices.remove("conceptionAndArtisticDirection");
                }
                break;

            case R.id.soundEditing :
                if(checked){
                    selectedServices.add("soundEditing");
                }
                else{
                    selectedServices.remove("soundEditing");
                }
                break;


            case R.id.products :
                if(checked){
                    selectedServices.add("products");
                }
                else{
                    selectedServices.remove("products");
                }
                break;

            case R.id.candid :
                if(checked){
                    selectedServices.add("candid");
                }
                else{
                    selectedServices.remove("candid");
                }
                break;

            case R.id.scripting :
                if(checked){
                    selectedServices.add("scripting");
                }
                else{
                    selectedServices.remove("scripting");
                }
                break;

            case R.id.fashionOrModel :
                if(checked){
                    selectedServices.add("fashionOrModel");
                }
                else{
                    selectedServices.remove("fashionOrModel");
                }
                break;

            case R.id.corporate :
                if(checked){
                    selectedServices.add("corporate");
                }
                else{
                    selectedServices.remove("corporate");
                }
                break;
            case R.id.wedding :
                if(checked){
                    selectedServices.add("wedding");
                }
                else{
                    selectedServices.remove("wedding");
                }
                break;

            case R.id.vfxArtist :
                if(checked){
                    selectedServices.add("vfxArtist");
                }
                else{
                    selectedServices.remove("vfxArtist");
                }
                break;

            case R.id.retouchingAndManipulation :
                if(checked){
                    selectedServices.add("retouchingAndManipulation");
                }
                else{
                    selectedServices.remove("retouchingAndManipulation");
                }
                break;



        }
    }

    public void addServicesButton(View view) {
        p.show();
        p.setMessage("Adding services to the user");
        p.setCancelable(false);
        if(mAuth.getCurrentUser().getUid().toString()==null){
            Toast.makeText(ServicesActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
            return;
        }
        firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("services").setValue(null);

        for(String service : selectedServices){
            firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("services/"+service).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    p.dismiss();
                    Toast.makeText(ServicesActivity.this, "User Registered Succesfully", Toast.LENGTH_LONG);
                    startActivity(new Intent(ServicesActivity.this, ProductsActivity.class));
                }



            });
        }
    }
}
