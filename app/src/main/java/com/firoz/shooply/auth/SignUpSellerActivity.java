package com.firoz.shooply.auth;

import static com.firoz.shooply.util.Constant.SignUp;
import static com.firoz.shooply.util.Constant.getAllCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.firoz.shooply.R;
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.auth.model.Plan;
import com.firoz.shooply.auth.model.UserType;
import com.firoz.shooply.seller_dashboard.SellerDashboardActivity;
import com.firoz.shooply.seller_dashboard.activity.AddProductActivity;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SignUpSellerActivity extends AppCompatActivity {

    EditText name,storeName,email,phoneNumber,password;
    TextView login_btn;
    AppCompatButton btn_signup;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Spinner sellerCategory;
    ProgressDialog progressDialog;
    JSONArray storeCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_seller);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        initView();
    }

    private void initView() {
        name=findViewById(R.id.name);
        storeName=findViewById(R.id.storeName);
        email=findViewById(R.id.email);
        phoneNumber=findViewById(R.id.phoneNumber);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        btn_signup=findViewById(R.id.btn_signup);
        sellerCategory=findViewById(R.id.sellerCategory);

        login_btn.setOnClickListener(view -> {
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        });

        btn_signup.setOnClickListener(view -> {
            if (isValid()){
                try {
                    signUp();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd",e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadCategoty();
    }
    private void loadCategoty() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=getAllCategory;

        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        ArrayList<String> stringArrayList=new ArrayList<>();
                        try {
                            storeCategoryList=new JSONArray(response);
                            for (int i=0;i<storeCategoryList.length();i++){
                                JSONObject jsonObject=storeCategoryList.getJSONObject(i);
                                stringArrayList.add(jsonObject.getString("category"));
                            }
                            sellerCategory.setAdapter(new ArrayAdapter<String>(SignUpSellerActivity.this, android.R.layout.simple_spinner_item,stringArrayList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(SignUpSellerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(sr);
    }

    private void signUp() throws JSONException {

        progressDialog.show();

        String url=SignUp;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name.getText().toString().trim());
        jsonBody.put("phoneNumber", phoneNumber.getText().toString().trim());
        jsonBody.put("email", email.getText().toString().trim());
        jsonBody.put("password", password.getText().toString().trim());
        jsonBody.put("storeName", storeName.getText().toString().trim());
        jsonBody.put("userType", UserType.Seller);
        jsonBody.put("plan", Plan.Basic);
        jsonBody.put("storeCategory", storeCategoryList.get(sellerCategory.getSelectedItemPosition()));
        final String mRequestBody = jsonBody.toString();

        Log.e("abcd",mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(SignUpSellerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        editor.putString("authUser",jsonObject.getJSONObject("authUser").toString());
                        editor.commit();
                        AuthUser authUser=new Gson().fromJson(jsonObject.getJSONObject("authUser").toString(), new TypeToken<AuthUser>() {}.getType());
                        FirebaseMessaging.getInstance().subscribeToTopic(authUser.getUserId());
                        if (authUser.getUserType()== UserType.User){
                            startActivity(new Intent(SignUpSellerActivity.this, UserDashboardActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(SignUpSellerActivity.this, SellerDashboardActivity.class));
                            finish();
                        }
                    }else {
                        Toast.makeText(SignUpSellerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd",e.getMessage());
                    Toast.makeText(SignUpSellerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("abcd",error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(SignUpSellerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    boolean isValid(){
        if (name.getText().toString().trim().isEmpty()){
            name.setError("Please provide Full Name");
            name.requestFocus();
            return false;
        }else {
            if (storeName.getText().toString().trim().isEmpty()){
                storeName.setError("Please provide Store Name");
                storeName.requestFocus();
                return false;
            }else {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Please provide Email");
                    email.requestFocus();
                    return false;
                }else {
                    if (phoneNumber.getText().toString().trim().isEmpty()){
                        phoneNumber.setError("Please provide Phone Number");
                        phoneNumber.requestFocus();
                        return false;
                    }else {
                        if (password.getText().toString().trim().isEmpty()){
                            password.setError("Please provide Password");
                            password.requestFocus();
                            return false;
                        }else {
                            return true;
                        }
                    }
                }
            }
        }
    }
}