package com.firoz.shooply.user_dashboard.helper;

import static com.firoz.shooply.util.Constant.cancelOrderByUser;
import static com.firoz.shooply.util.Constant.getOrderHistory;
import static com.firoz.shooply.util.Constant.getPendingOrder;
import static com.firoz.shooply.util.Constant.getStartedOrder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class OrderHelper {
    Context context;
    SharedPreferences sharedpreferences;
    private String userId;

    public OrderHelper(Context context) {
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

    public void getOrderHistory(ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getOrderHistory;
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
                params.put("userId", userId);
                return params;
            }
        };
        queue.add(sr);
    }

    public void getStartedOrder(ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getStartedOrder;
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
                params.put("userId", userId);
                return params;
            }
        };
        queue.add(sr);
    }

    public void getPendingOrder(ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getPendingOrder;
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
                params.put("userId", userId);
                return params;
            }
        };
        queue.add(sr);
    }


    public void cancelOrderByUser(OrderModel orderModel, ResponsListener responsListener) {
        String url = cancelOrderByUser;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String mRequestBody = new Gson().toJson(orderModel);
        Log.e("abcd", mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responsListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("abcd", error.getMessage());
                responsListener.onError(error.toString());
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

}
