package com.firoz.shooply.seller_dashboard.activity;

import static com.firoz.shooply.util.Constant.updateStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.seller_dashboard.SellerDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UpdateStoreActivity extends AppCompatActivity {
    private final int storegePermision = 13;

    private final int logoCode = 1;
    private final int bannerCode = 2;


    ImageView banner, logo;
    TextInputEditText storeName, storeSlogan, storeDescription, storeEmail;

    JSONObject jsonObject;
    ProgressDialog progressDialog;
    private StorageReference storageReference;
    Button update_btn,cancel_btn;

    String bannerUrl,logoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store);
        String object = getIntent().getStringExtra("StoreModel");
        try {
            jsonObject = new JSONObject(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storageReference = FirebaseStorage.getInstance().getReference();

        Log.e("abcd",jsonObject.toString());
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        initView();
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        logo = findViewById(R.id.logo);
        storeName = findViewById(R.id.storeName);
        storeSlogan = findViewById(R.id.storeSlogan);
        storeDescription = findViewById(R.id.storeDescription);
        storeEmail = findViewById(R.id.storeEmail);
        update_btn = findViewById(R.id.update_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        cancel_btn.setOnClickListener(view -> {
            finish();
        });

        update_btn.setOnClickListener(view -> {
            uploadBanner();
        });

        banner.setOnClickListener(view -> {
            if (checkPermissionForGallery()) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, bannerCode);
            }

        });
        logo.setOnClickListener(view -> {
            if (checkPermissionForGallery()) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, logoCode);
            }
        });

        try {
            setLoadedData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLoadedData() throws JSONException {
        Glide.with(this).load(jsonObject.getString("banner")).into(banner);
        Glide.with(this).load(jsonObject.getString("logo")).into(logo);

        storeName.setText(jsonObject.getString("storeName"));
        storeSlogan.setText(jsonObject.getString("storeSlogan"));
        storeDescription.setText(jsonObject.getString("storeDescription"));
        storeEmail.setText(jsonObject.getString("storeEmail"));
    }


    private void upDate() {
        try {
            String url=updateStore;
            Log.e("abcd",jsonObject.toString());

            jsonObject.put("storeName",storeName.getText().toString().trim());
            jsonObject.put("storeDescription",storeDescription.getText().toString().trim());
            jsonObject.put("storeSlogan",storeSlogan.getText().toString().trim());
            jsonObject.put("storeEmail",storeEmail.getText().toString().trim());
            jsonObject.put("banner",bannerUrl);
            jsonObject.put("logo",logoUrl);

            final String mRequestBody = jsonObject.toString();
            RequestQueue requestQueue = Volley.newRequestQueue(this);


            Log.e("abcd",mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                            Toast.makeText(UpdateStoreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateStoreActivity.this,
                                    SellerDashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                        } else {
                            Toast.makeText(UpdateStoreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateStoreActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("abcd",e.toString());

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("abcd", error.toString());
                    Toast.makeText(UpdateStoreActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void uploadBanner(){
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) banner.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalimage = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("banner").child(finalimage+".jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateStoreActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    bannerUrl = String.valueOf(uri);
                                    Log.e("abcd",bannerUrl);
                                    uploadLogo();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(UpdateStoreActivity.this, "Something went worng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadLogo(){
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) logo.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalimage = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("logo").child(finalimage+".jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateStoreActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    logoUrl = String.valueOf(uri);
                                    Log.e("abcd",logoUrl);
                                    upDate();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(UpdateStoreActivity.this, "Something went worng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermissionForGallery() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storegePermision);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case logoCode:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            logo.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case bannerCode:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            banner.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}