package com.firoz.shooply.user_dashboard.helper;


import static com.firoz.shooply.util.Constant.getAllProductSeller;
import static com.firoz.shooply.util.Constant.getStoreByStoreId;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.util.ResponsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserStoreHelper {

    Context context;
    SharedPreferences sharedpreferences;
    private String userId;

    public UserStoreHelper(Context context) {
        this.context = context;
    }

    public void getProductByStoreId(String storeId, ResponsListener responsListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getAllProductSeller;
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
                params.put("storeId", storeId);
                return params;
            }
        };
        queue.add(sr);
    }

    public void loadStoreData(String storeId, ResponsListener responsListener) {
        String Url=getStoreByStoreId;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        responsListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        responsListener.onError(error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("storeId",storeId);
                return params;
            }
        };
        queue.add(sr);
    }

}
