package com.firoz.shooply.auth;

import static com.firoz.shooply.util.Constant.LogIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.R;
import com.firoz.shooply.seller_dashboard.SellerDashboardActivity;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.auth.model.UserType;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    EditText mobile,password;
    Button login_btn;
    TextView signup;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        initView();
    }

    private void initView() {
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        signup=findViewById(R.id.signup);

        signup.setOnClickListener(view -> {

            startActivity(new Intent(this,SignUpSelectActivity.class));
        });


        login_btn.setOnClickListener(view -> {
            if (isValid()){
                checkLogin(mobile.getText().toString().trim(),password.getText().toString().trim());
            }
        });

    }

    private void checkLogin(String mobileNo, String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=LogIn;
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                Toast.makeText(SignInActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                editor.putString("authUser",jsonObject.getJSONObject("authUser").toString());
                                editor.commit();
                                AuthUser authUser=new Gson().fromJson(jsonObject.getJSONObject("authUser").toString(), new TypeToken<AuthUser>() {}.getType());
                                FirebaseMessaging.getInstance().subscribeToTopic(authUser.getUserId());

                                if (authUser.getUserType()== UserType.User){
                                    startActivity(new Intent(SignInActivity.this, UserDashboardActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(SignInActivity.this, SellerDashboardActivity.class));
                                    finish();
                                }
                            }else {
                                Toast.makeText(SignInActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("abcd",e.getMessage());
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phoneNumber",mobileNo);
                params.put("password",pass);
                return params;
            }
        };
        queue.add(sr);
    }

    boolean isValid(){
        if (mobile.getText().toString().trim().isEmpty()){
            mobile.setError("Please provide mobile number");
            mobile.requestFocus();
            return false;
        }else {
            if (password.getText().toString().trim().isEmpty()){
                password.setError("Please provide password");
                password.requestFocus();
                return false;
            }else {
                return true;
            }
        }
    }
}