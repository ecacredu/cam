package com.acecosmos.camle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    DatabaseReference firebaseRef;
    private FirebaseAuth mAuth;

    Toolbar toolbar;

    // Ids

    EditText firstName;
    EditText lastName;
    EditText city;
    EditText state;
    EditText country;
    EditText email;
    EditText mobile;
    Spinner profession;

    ProgressDialog p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseRef = database.getReference("");
        mAuth = FirebaseAuth.getInstance();

//        startActivity(new Intent(this,ProductsActivity.class));

        profession = (Spinner) findViewById(R.id.profession);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.professions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(adapter);

        p = new ProgressDialog(this);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void addUser(View view) {
//        firebaseRef.child("username").push().setValue("akashs25");

//        if(9>7){
//            startActivity(new Intent(this,ServicesActivity.class));
//            return;
//        }

        p.setMessage("Registering user..");
        p.setCancelable(false);
        p.show();

        final HashMap<String, String> user = new HashMap<String, String>();
        user.put("firstName", firstName.getText().toString().trim());
        user.put("lastName", lastName.getText().toString().trim());
        user.put("city", city.getText().toString().trim());
        user.put("state", state.getText().toString().trim());
        user.put("country", country.getText().toString().trim());
        user.put("email", email.getText().toString().trim());
        user.put("mobile", mobile.getText().toString().trim());
        user.put("profession", profession.getSelectedItem().toString().trim());

        if (!this.registrationValidation()) {
            p.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), "Password123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    p.setMessage("Saving User Data");
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstName.getText().toString().trim()).build();
                    mAuth.getCurrentUser().updateProfile(profileUpdates);
                    mAuth.getCurrentUser().sendEmailVerification();

                    firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("profile").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.clear();
                            p.dismiss();
                            this.clearForm();

                            Toast.makeText(MainActivity.this, "User Registered Succesfully", Toast.LENGTH_LONG);
                            startActivity(new Intent(MainActivity.this, ServicesActivity.class));
                        }

                        private void clearForm() {
                            firstName.setText("");
                            lastName.setText("");
                            city.setText("");
                            state.setText("");
                            country.setText("");
                            email.setText("");
                            mobile.setText("");
                            profession.setSelection(0);
                        }


                    });
                } else {
                    Toast.makeText(MainActivity.this, "User Registration Failed.. Please try again", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }

            }
        });


    }

    private boolean registrationValidation() {
        Validation validator = new Validation();
        boolean returnValue = true;
        if (validator.checkEmail(email.getText().toString().trim())) {
            TextInputLayout emailAddressFloat = (TextInputLayout) findViewById(R.id.emailAddressFloat);
            emailAddressFloat.setErrorEnabled(false);
        } else {
            TextInputLayout emailAddressFloat = (TextInputLayout) findViewById(R.id.emailAddressFloat);
            emailAddressFloat.setError("Invaid Email Address");
            returnValue = false;
        }

        if (validator.checkMobile(mobile.getText().toString().trim())) {
            TextInputLayout mobileNumberFloat = (TextInputLayout) findViewById(R.id.mobileNumberFloat);
            mobileNumberFloat.setErrorEnabled(false);
        } else {
            TextInputLayout mobileNumberFloat = (TextInputLayout) findViewById(R.id.mobileNumberFloat);
            mobileNumberFloat.setError("Invaid Email Address");
            returnValue = false;
        }

        if (validator.isSet3Chars(city.getText().toString().trim())) {
            TextInputLayout cityFloat = (TextInputLayout) findViewById(R.id.cityFloat);
            cityFloat.setErrorEnabled(false);
        } else {
            TextInputLayout cityFloat = (TextInputLayout) findViewById(R.id.cityFloat);
            cityFloat.setError("City Required ( Minimum 3 chars )");
            returnValue = false;
        }

        if (validator.isSet3Chars(state.getText().toString().trim())) {
            TextInputLayout stateFloat = (TextInputLayout) findViewById(R.id.stateFloat);
            stateFloat.setErrorEnabled(false);
        } else {
            TextInputLayout stateFloat = (TextInputLayout) findViewById(R.id.stateFloat);
            stateFloat.setError("State Required ( Minimum 3 chars )");
            returnValue = false;
        }

        if (validator.isSet3Chars(country.getText().toString().trim())) {
            TextInputLayout countryFloat = (TextInputLayout) findViewById(R.id.countryFloat);
            countryFloat.setErrorEnabled(false);
        } else {
            TextInputLayout countryFloat = (TextInputLayout) findViewById(R.id.countryFloat);
            countryFloat.setError("Country Required ( Minimum 3 chars )");
            returnValue = false;
        }


        if (validator.isSet3Chars(firstName.getText().toString().trim())) {
            TextInputLayout firstNameFloat = (TextInputLayout) findViewById(R.id.firstNameFloat);
            firstNameFloat.setErrorEnabled(false);
        } else {
            TextInputLayout firstNameFloat = (TextInputLayout) findViewById(R.id.firstNameFloat);
            firstNameFloat.setError("First Name Required ( Minimum 3 chars )");
            returnValue = false;
        }


        if (validator.isSet3Chars(lastName.getText().toString().trim())) {
            TextInputLayout lastNameFloat = (TextInputLayout) findViewById(R.id.lastNameFloat);
            lastNameFloat.setErrorEnabled(false);
        } else {
            TextInputLayout lastNameFloat = (TextInputLayout) findViewById(R.id.lastNameFloat);
            lastNameFloat.setError("Last Name Required ( Minimum 3 chars )");
            returnValue = false;
        }

        return returnValue;

    }


}
