package com.acecosmos.camle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "SignIn";
  private EditText mEmail;
  private EditText mPass;
  private Button mSubmit;
  private Button mSignUp;

  private FirebaseAuth mAuth;


  private TextInputLayout mEmailFloat;
  private TextInputLayout mPassFloat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    findViews();
    configViews();
  }

  private void configViews() {

    mSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Validation validator = new Validation();

        if(TextUtils.isEmpty(mEmail.getText().toString())){
          makeToast("Please enter a valid email address!");
          mEmailFloat.setError("Email address required");
          return;
        }

        if(TextUtils.isEmpty(mPass.getText().toString().trim())){
          makeToast("Password required!");
          mPassFloat.setError("Password required");
          return;
        }

        if(!validator.checkEmail(mEmail.getText().toString().trim())){
          makeToast("Invalid Email");
          mEmailFloat.setError("Invalid Email");
          return;
        }

        mEmailFloat.setErrorEnabled(false);
        mPassFloat.setErrorEnabled(false);
        signIn(mEmail.getText().toString().trim(),mPass.getText().toString().trim());
      }
    });

    mSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
      }
    });
  }

  private void signIn(String email, String password) {

    mAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();
            startActivity(new Intent(SignInActivity.this,ServicesActivity.class));
            finish();
          } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            makeToast("Authentication failed.");
          }

        }
      });
  }

  private void findViews() {
    mEmail = (EditText) findViewById(R.id.signin_mail);
    mPass = (EditText) findViewById(R.id.signin_pass);
    mSubmit = (Button) findViewById(R.id.signInSubmit);
    mSignUp = (Button) findViewById(R.id.signUpButton);
    mEmailFloat = (TextInputLayout) findViewById(R.id.signin_mailFloat);
    mPassFloat = (TextInputLayout) findViewById(R.id.signin_passFloat);

    mAuth = FirebaseAuth.getInstance();
  }

  public void makeToast(String message){
    Toast.makeText(SignInActivity.this,message, Toast.LENGTH_LONG).show();
  }
}
