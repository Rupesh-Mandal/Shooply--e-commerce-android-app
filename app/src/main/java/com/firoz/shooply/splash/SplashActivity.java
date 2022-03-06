package com.firoz.shooply.splash;

import static com.firoz.shooply.util.Constant.LogIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.R;
import com.firoz.shooply.auth.SignInActivity;
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

public class SplashActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 1500; //This is 3 seconds
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    String object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        object=sharedpreferences.getString("authUser","");

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (object.trim().isEmpty()){
                    Intent mySuperIntent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }else {
                    AuthUser authUser=new Gson().fromJson(object, new TypeToken<AuthUser>() {}.getType());
                    checkLogin(authUser.getPhoneNumber(),authUser.getPassword());
                }
            }
        }, SPLASH_TIME);
    }


    private void checkLogin(String mobileNo, String pass) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=LogIn;

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                editor.putString("authUser",jsonObject.getJSONObject("authUser").toString());
                                editor.commit();
                                AuthUser authUser=new Gson().fromJson(jsonObject.getJSONObject("authUser").toString(), new TypeToken<AuthUser>() {}.getType());
                                if (authUser.getUserType()== UserType.User){
                                    startActivity(new Intent(SplashActivity.this, UserDashboardActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(SplashActivity.this, SellerDashboardActivity.class));
                                    finish();
                                }
                            }else {
                                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("abcd",error.getMessage());
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
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


    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(3000)
                .start();
    }
}