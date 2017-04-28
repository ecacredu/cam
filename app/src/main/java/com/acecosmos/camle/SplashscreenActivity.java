package com.acecosmos.camle;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashscreenActivity extends AppCompatActivity {

  private DatabaseReference mDataRef;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splashscreen);

    mDataRef = FirebaseDatabase.getInstance().getReference();
    mAuth = FirebaseAuth.getInstance();

    if(mAuth.getCurrentUser() == null){
      new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
                /* Create an Intent that will start the Menu-Activity. */
          Intent mainIntent = new Intent(SplashscreenActivity.this,SignInActivity.class);
          SplashscreenActivity.this.startActivity(mainIntent);
          SplashscreenActivity.this.finish();
        }
      }, 1000);

    }else{
      mDataRef.child("users/"+mAuth.getCurrentUser().getUid().toString().trim())
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.child("profile").getValue()==null){

              startActivity(new Intent(SplashscreenActivity.this, SignUpActivity.class));
              finish();
            }
            else{
              if(dataSnapshot.child("profile/mobile").getValue().toString().trim()==null){
                startActivity(new Intent(SplashscreenActivity.this, SignUpActivity.class));
                finish();
              }
              else{
                if(dataSnapshot.child("services").getValue()==null){
                  startActivity(new Intent(SplashscreenActivity.this, ServicesActivity.class));
                  finish();
                }else{
                  if(dataSnapshot.child("products").getValue()==null){
                    startActivity(new Intent(SplashscreenActivity.this, ProductsActivity.class));
                    finish();
                  }
                }
              }

            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });
    }


  }
}
