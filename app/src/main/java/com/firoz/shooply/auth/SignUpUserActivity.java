package com.firoz.shooply.auth;

import static com.firoz.shooply.util.Constant.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUpUserActivity extends AppCompatActivity {

    EditText name, email, phoneNumber, password;
    TextView login_btn;
    AppCompatButton btn_signup;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        initView();
    }

    private void initView() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        btn_signup = findViewById(R.id.btn_signup);

        login_btn.setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

        btn_signup.setOnClickListener(view -> {
            if (isValid()) {
                try {
                    signUp();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp() throws JSONException {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        String url=SignUp;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name.getText().toString().trim());
        jsonBody.put("phoneNumber", phoneNumber.getText().toString().trim());
        jsonBody.put("email", email.getText().toString().trim());
        jsonBody.put("password", password.getText().toString().trim());
        jsonBody.put("userType", UserType.User);
        jsonBody.put("plan", Plan.Basic);
        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(SignUpUserActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        editor.putString("authUser",jsonObject.getJSONObject("authUser").toString());
                        editor.commit();
                        AuthUser authUser=new Gson().fromJson(jsonObject.getJSONObject("authUser").toString(), new TypeToken<AuthUser>() {}.getType());
                        FirebaseMessaging.getInstance().subscribeToTopic(authUser.getUserId());
                        if (authUser.getUserType()== UserType.User){
                            startActivity(new Intent(SignUpUserActivity.this, UserDashboardActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(SignUpUserActivity.this, SellerDashboardActivity.class));
                            finish();
                        }
                    }else {
                        Toast.makeText(SignUpUserActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd",e.getMessage());
                    Toast.makeText(SignUpUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("abcd",error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(SignUpUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    boolean isValid() {
        if (name.getText().toString().trim().isEmpty()) {
            name.setError("Please provide Full Name");
            name.requestFocus();
            return false;
        } else {
            if (email.getText().toString().trim().isEmpty()) {
                email.setError("Please provide Email");
                email.requestFocus();
                return false;
            } else {
                if (phoneNumber.getText().toString().trim().isEmpty()) {
                    phoneNumber.setError("Please provide Phone Number");
                    phoneNumber.requestFocus();
                    return false;
                } else {
                    if (password.getText().toString().trim().isEmpty()) {
                        password.setError("Please provide Password");
                        password.requestFocus();
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }
}