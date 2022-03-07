package com.firoz.shooply.user_dashboard.helper;

import static com.firoz.shooply.util.Constant.addAddress;
import static com.firoz.shooply.util.Constant.addToCart;
import static com.firoz.shooply.util.Constant.deleteCart;
import static com.firoz.shooply.util.Constant.getAddressByUserId;
import static com.firoz.shooply.util.Constant.getAllCartList;
import static com.firoz.shooply.util.Constant.oderProduct;
import static com.firoz.shooply.util.Constant.updateCart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.firoz.shooply.auth.SignInActivity;
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.auth.model.UserType;
import com.firoz.shooply.checkout.adapter.AddressBookAdapter;
import com.firoz.shooply.model.AddressBookModel;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.seller_dashboard.SellerDashboardActivity;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.util.AddressBookOnClick;
import com.firoz.shooply.util.ResponsListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartHelper {
    Context context;
    SharedPreferences sharedpreferences;
    private String userId;

    public CartHelper(Context context) {
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

    public void addToCart(Product product, int quantity, ResponsListener responsListener) {
        try {
            String url = addToCart;

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject(new Gson().toJson(product));
            jsonBody.put("quantity", String.valueOf(quantity));
            jsonBody.put("userId", userId);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonBody);

            final String mRequestBody = jsonArray.toString();
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
        } catch (JSONException e) {
            e.printStackTrace();
            responsListener.onError(e.toString());
        }
    }

    public void getAllCartList(ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getAllCartList;
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

    public void loadAddress(ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = getAddressByUserId;

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

    public void uploadAddress(String productDeliverAddress, String userPhoneNumber, ResponsListener responsListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = addAddress;
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

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
                params.put("productDeliverAddress", productDeliverAddress);
                params.put("userId", userId);
                params.put("userPhoneNumber", userPhoneNumber);
                return params;
            }
        };
        queue.add(sr);
    }

    public void updateCart(CartModel cartModel, ResponsListener responsListener) {
        String url = updateCart;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String mRequestBody = new Gson().toJson(cartModel);
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

    public void deleteCart(CartModel cartModel, ResponsListener responsListener) {
        String url = deleteCart;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String mRequestBody = new Gson().toJson(cartModel);
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

    public void addOrder(JSONArray orderArray, ResponsListener responsListener){
        String url = oderProduct;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String mRequestBody = new Gson().toJson(orderArray);
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
