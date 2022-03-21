package com.firoz.shooply.seller_dashboard.helper;

import static com.firoz.shooply.util.Constant.addProduct;
import static com.firoz.shooply.util.Constant.getOrderHistory;
import static com.firoz.shooply.util.Constant.getSellerOrderHistory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.seller_dashboard.activity.AddProductActivity;
import com.firoz.shooply.util.ResponsListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SellerHelper {
    Activity activity;
    AuthUser authUserForSeller;
    SharedPreferences sharedpreferences;
    private StorageReference storageReference;
    JSONObject productObject=new JSONObject();

    ProgressDialog progressDialog;

    public SellerHelper(Activity activity1) {
        this.activity = activity1;
        sharedpreferences = activity.getSharedPreferences("MyPREFERENCES", activity.MODE_PRIVATE);
        String object=sharedpreferences.getString("authUser","");
        authUserForSeller=new Gson().fromJson(object, new TypeToken<AuthUser>() {}.getType());
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait");
    }

    public void uploadImage(Bitmap bitmap1,JSONObject jsonObject) {
        this.productObject = jsonObject;

        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = bitmap1;

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalimage = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("product").child(finalimage+".jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(activity, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    String imageDownloadUri = String.valueOf(uri);
                                    Log.e("abcd",imageDownloadUri);
                                    try {
                                        uploadProduct(imageDownloadUri);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("abcd",e.toString());
                                    }

                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Something went worng", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadProduct(String imageDownloadUri) throws JSONException {
        progressDialog.show();
        productObject.put("productImageLink",imageDownloadUri);
        productObject.put("storeEmail",authUserForSeller.getEmail());
        productObject.put("storeId",authUserForSeller.getUserId());

        String URL=addProduct;
        final String mRequestBody = productObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(activity);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(activity, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        activity.setResult(2);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("abcd",e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
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

    }


    public void getOrderHistory(int page, ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        String Url = getSellerOrderHistory;
        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        responsListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        responsListener.onError(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("storeId", authUserForSeller.getUserId());
                params.put("page", String.valueOf(page));
                params.put("pageSize", "10");
                params.put("sort", "ASC");
                params.put("sortBy", "createdTime");
                return params;
            }
        };
        queue.add(sr);
    }


}
