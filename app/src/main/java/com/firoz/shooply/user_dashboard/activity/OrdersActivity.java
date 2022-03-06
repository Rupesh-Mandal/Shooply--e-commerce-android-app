package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.getOrderAsUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.user_dashboard.adapter.UserAdapter;
import com.firoz.shooply.util.UserOrderOnclick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    RecyclerView orderRecycler;

    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            Log.e("abcd", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();

    }

    private void initView() {
        orderRecycler = findViewById(R.id.orderRecycler);
        orderRecycler.setLayoutManager(new GridLayoutManager(OrdersActivity.this, 1));

        progressDialog = new ProgressDialog(OrdersActivity.this);
        progressDialog.setMessage("Please Wait");

        loadOrder();
    }
    private void loadOrder() {
        progressDialog.show();
        String url = getOrderAsUser;
        RequestQueue queue = Volley.newRequestQueue(OrdersActivity.this);

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("abcd", jsonArray.toString());
                            orderModelArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<OrderModel>>() {
                            }.getType());
                            setOrder();
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
                        Toast.makeText(OrdersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userId",userId);
                return params;
            }
        };
        queue.add(sr);
    }

    private void setOrder() {
        UserAdapter userAdapter=new UserAdapter(OrdersActivity.this, orderModelArrayList, new UserOrderOnclick() {
            @Override
            public void onReceived(OrderModel orderModel) {

            }

            @Override
            public void onCancel(OrderModel orderModel) {

            }
        });
        orderRecycler.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void orderAction(OrderModel orderModel,String url) {
        progressDialog.show();
        String URL = url;

        final String mRequestBody = new Gson().toJson(orderModel);
        RequestQueue requestQueue = Volley.newRequestQueue(OrdersActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(OrdersActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        loadOrder();
                    }else {
                        Toast.makeText(OrdersActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OrdersActivity.this, "Something went wrong "+e.toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(OrdersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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