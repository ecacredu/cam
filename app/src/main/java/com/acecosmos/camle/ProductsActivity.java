package com.acecosmos.camle;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity implements IPickResult {
  DatabaseReference firebaseRef;
  private FirebaseAuth mAuth;

  private ImageButton mProdImage;
  public Uri imageURI;
  public Uri downloadUrl;
  private FirebaseStorage storage;
  private StorageReference storageRef;

  private Button mSubmitButton;

  ArrayAdapter<CharSequence> adapter;

  ProgressDialog p;

  Spinner productType;

  String prodKey, uid, editImageUrl;
  String userName;
  String userCity;
  Boolean isEdit;

  EditText productBrand, productModelNumber, rent1Day, rent3Day, rent5Day, salePrice;

  HashMap<String, String> product;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    product = new HashMap<>();

    p = new ProgressDialog(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_products);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    toolbar.setTitle("My Products");

    prodKey = getIntent().getStringExtra("prodKey");
    isEdit = getIntent().getBooleanExtra("isEdit", false);

    productBrand = (EditText) findViewById(R.id.productBrand);
    productModelNumber = (EditText) findViewById(R.id.productModelNumber);
    rent1Day = (EditText) findViewById(R.id.rent1Day);
    rent3Day = (EditText) findViewById(R.id.rent3Day);
    rent5Day = (EditText) findViewById(R.id.rent5Day);
    salePrice = (EditText) findViewById(R.id.salePrice);
    mProdImage = (ImageButton) findViewById(R.id.prod_image);

    mSubmitButton = (Button) findViewById(R.id.addProductButton);

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    firebaseRef = database.getReference("");
    mAuth = FirebaseAuth.getInstance();
    uid = mAuth.getCurrentUser().getUid();

    storage = FirebaseStorage.getInstance();

    mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        prepareProductAdd();
      }
    });

    mProdImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        PickImageDialog.build(new PickSetup()
          .setSystemDialog(true)
        ).show(ProductsActivity.this);
      }
    });

    productType = (Spinner) findViewById(R.id.productType);
    adapter = ArrayAdapter.createFromResource(this,
      R.array.productTypes, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    productType.setAdapter(adapter);

    firebaseRef.child("users/" + mAuth.getCurrentUser().getUid() + "/profile").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.child("lastName").getValue() == null) {
          userName = dataSnapshot.child("firstName").getValue().toString().trim();
        } else {
          userName = dataSnapshot.child("firstName").getValue().toString().trim() + " " + dataSnapshot.child("lastName").getValue().toString().trim();
        }
        userCity = dataSnapshot.child("city").getValue().toString().trim();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

    if (isEdit) {
      setPreFills();
    }
  }

  private void setPreFills() {

    firebaseRef.child("users/" + uid + "/products/" + prodKey).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.child("type").getValue() != null) {
          int spinnerPosition = adapter.getPosition(dataSnapshot.child("type").getValue().toString());
          productType.setSelection(spinnerPosition);
        }
        if (dataSnapshot.child("brand").getValue() != null) {
          productBrand.setText(dataSnapshot.child("brand").getValue().toString());
        }
        if (dataSnapshot.child("modelNumber").getValue() != null) {
          productModelNumber.setText(dataSnapshot.child("modelNumber").getValue().toString());
        }
        if (dataSnapshot.child("rent1Day").getValue() != null) {
          rent1Day.setText(dataSnapshot.child("rent1Day").getValue().toString());
        }
        if (dataSnapshot.child("rent3Day").getValue() != null) {
          rent3Day.setText(dataSnapshot.child("rent3Day").getValue().toString());
        }
        if (dataSnapshot.child("rent5Day").getValue() != null) {
          rent5Day.setText(dataSnapshot.child("rent5Day").getValue().toString());
        }
        if (dataSnapshot.child("salePrice").getValue() != null) {
          salePrice.setText(dataSnapshot.child("salePrice").getValue().toString());
        }
        if (dataSnapshot.child("image").getValue() != null) {
          editImageUrl = dataSnapshot.child("image").getValue().toString();
          Glide.clear(mProdImage);
          Glide.with(ProductsActivity.this)
            .load(dataSnapshot.child("image").getValue().toString())
            .into(mProdImage);
          mProdImage.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

  }

  @Override
  public void onPickResult(PickResult r) {
    if (r.getError() == null) {
      imageURI = r.getUri();
      Glide.clear(mProdImage);
      Glide.with(ProductsActivity.this)
        .load(imageURI)
        .into(mProdImage);
      mProdImage.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    } else {
      Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public void addNewUser(View view) {
    startActivity(new Intent(this, SignUpActivity.class));
  }

  public void prepareProductAdd() {

    if (mAuth.getCurrentUser().getUid().toString() == null) {
      Toast.makeText(ProductsActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
      return;
    }

    if (imageURI != null) {

      mProdImage.setDrawingCacheEnabled(true);
      mProdImage.buildDrawingCache();
      Bitmap bitmap = mProdImage.getDrawingCache();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      if (getMimeType(getApplicationContext(), imageURI).equals("jpg") || getMimeType(getApplicationContext(), imageURI).equals("jpeg")) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
      } else if (getMimeType(getApplicationContext(), imageURI).equals("png")) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
      }
      byte[] data = baos.toByteArray();


      storageRef = storage.getReference().child("images/" + imageURI.getLastPathSegment());
      UploadTask uploadTask = storageRef.putBytes(data);

      // Register observers to listen for when the download is done or if it fails
      uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
          // Handle unsuccessful uploads
        }
      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
          // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
          downloadUrl = taskSnapshot.getDownloadUrl();
          addProduct();
        }
      });
    } else {
      addProduct();
    }


  }


  public void addProduct() {

    if (mAuth.getCurrentUser().getUid().toString() == null) {
      Toast.makeText(ProductsActivity.this, "No User Logged In.. Restart the app", Toast.LENGTH_LONG);
      return;
    }

    product.put("type", productType.getSelectedItem().toString().trim());
    product.put("brand", productBrand.getText().toString().trim());
    product.put("modelNumber", productModelNumber.getText().toString().trim());
    product.put("rent1Day", rent1Day.getText().toString().trim());
    product.put("rent3Day", rent3Day.getText().toString().trim());
    product.put("rent5Day", rent5Day.getText().toString().trim());
    product.put("salePrice", salePrice.getText().toString().trim());
    product.put("user", mAuth.getCurrentUser().getUid().toString().trim());
    product.put("userName", userName);
    product.put("userCity", userCity);

    if (imageURI != null && downloadUrl != null) {
      product.put("image", downloadUrl.toString());
    } else {
      if (isEdit) {
        if (editImageUrl != null) {
          product.put("image", editImageUrl);
        }
      }
    }

    p.show();
    p.setMessage("Adding Your Product");
    p.setCancelable(false);
    Log.d("com.acecosmos.camle", product.toString());
    final String newProductKey;

    if (isEdit) {
      newProductKey = prodKey;
    } else {
      newProductKey = firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("products").push().getKey();
    }
    firebaseRef.child("users").child(mAuth.getCurrentUser().getUid().toString().trim()).child("products/" + newProductKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {

          firebaseRef.child("products/" + newProductKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              p.dismiss();
            }
          });
          product.clear();

          this.clearForm();

          Toast.makeText(ProductsActivity.this, "Product Added Successfully", Toast.LENGTH_LONG);
        } else {
          Toast.makeText(ProductsActivity.this, "Could Not Add The Product..", Toast.LENGTH_SHORT).show();
          p.dismiss();
        }


      }

      private void clearForm() {
        productBrand.setText("");
        productModelNumber.setText("");
        rent1Day.setText("");
        rent3Day.setText("");
        rent5Day.setText("");
        salePrice.setText("");
        productType.setSelection(0);
      }


    });


  }

  public static String getMimeType(Context context, Uri uri) {
    String extension;
    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
      final MimeTypeMap mime = MimeTypeMap.getSingleton();
      extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
    } else {
      extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());
    }
    return extension;
  }

  public void makeToast(String message) {
    Toast.makeText(ProductsActivity.this, message, Toast.LENGTH_LONG).show();
  }


}

