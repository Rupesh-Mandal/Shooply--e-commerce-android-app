package com.firoz.shooply.seller_dashboard.fragment;

import android.app.ProgressDialog;
import static com.firoz.shooply.seller_dashboard.SellerDashboardActivity.authUserForSeller;
import static com.firoz.shooply.util.Constant.acceptOrder;
import static com.firoz.shooply.util.Constant.cancelOrderBySeller;
import static com.firoz.shooply.util.Constant.deliverdFaildOrder;
import static com.firoz.shooply.util.Constant.getOrderAsSeller;
import static com.firoz.shooply.util.Constant.onDeliveryStarted;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.seller_dashboard.adapter.SellerOrderAdapter;
import com.firoz.shooply.util.OrderOnclick;
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


public class SellerOrdersFragment extends Fragment {

    View view;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    RecyclerView orderRecycler;

    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;


    public SellerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        String object=sharedpreferences.getString("authUser","");
        authUserForSeller=new Gson().fromJson(object, new TypeToken<AuthUser>() {}.getType());
        initView();

    }
    private void initView() {
        orderRecycler = view.findViewById(R.id.orderRecycler);
        orderRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        loadOrder();
    }

    private void loadOrder() {
        progressDialog.show();
        String url = getOrderAsSeller;
        RequestQueue queue = Volley.newRequestQueue(getContext());

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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("storeId",authUserForSeller.getUserId());
                return params;
            }
        };
        queue.add(sr);
    }

    private void setOrder() {

        SellerOrderAdapter sellerOrderAdapter = new SellerOrderAdapter(getContext(), orderModelArrayList, new OrderOnclick() {
            @Override
            public void onDeliveryStarted(OrderModel orderModel) {
                orderAction(orderModel,onDeliveryStarted);
            }

            @Override
            public void onDeliverdFaild(OrderModel orderModel) {
                orderAction(orderModel,deliverdFaildOrder);
            }

            @Override
            public void onCancel(OrderModel orderModel, String massege) {
                orderAction(orderModel,cancelOrderBySeller);
            }

            @Override
            public void onAccept(OrderModel orderModel) {
                orderAction(orderModel,acceptOrder);
            }
        });
        orderRecycler.setAdapter(sellerOrderAdapter);

    }

    private void orderAction(OrderModel orderModel,String url) {
        progressDialog.show();
        String URL = url;

        final String mRequestBody = new Gson().toJson(orderModel);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        loadOrder();
                    }else {
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong "+e.toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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