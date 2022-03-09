package com.firoz.shooply.user_dashboard.helper;

import static com.firoz.shooply.util.Constant.findByProductId;
import static com.firoz.shooply.util.Constant.findByStorecategory;
import static com.firoz.shooply.util.Constant.getAllProductSeller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

public class ProductHelper {
    Context context;
    SharedPreferences sharedpreferences;
    private String userId;

    public ProductHelper(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences("MyPREFERENCES", context.MODE_PRIVATE);
        String object = sharedpreferences.getString("authUser", "");
        try {
            JSONObject jsonObject = new JSONObject(object);
            userId = jsonObject.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProductByRecommendedByStore(String storeId, ResponsListener responsListener){
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


    public void getProductByStorecategory(String storecategory, ResponsListener responsListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = findByStorecategory;
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
                params.put("storecategory", storecategory);
                return params;
            }
        };
        queue.add(sr);
    }


    public void findByProductId(String productId, ResponsListener responsListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = findByProductId;
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
                params.put("productId", productId);
                return params;
            }
        };
        queue.add(sr);
    }



    public void findByStorecategory(String storecategory, ResponsListener responsListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = findByStorecategory;
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
                params.put("storecategory", storecategory);
                return params;
            }
        };
        queue.add(sr);
    }


}
