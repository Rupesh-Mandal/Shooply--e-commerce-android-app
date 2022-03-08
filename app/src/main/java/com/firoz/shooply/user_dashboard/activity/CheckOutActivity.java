package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.oderProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.firoz.shooply.checkout.adapter.PaymentListAdapter;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.util.ResponsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    RecyclerView payment_recyclerView;
    Button cash_on_delivery;
    ProgressDialog progressDialog;
    JSONArray orderArray = new JSONArray();

    CartHelper cartHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        payment_recyclerView=findViewById(R.id.payment_recyclerView);
        cash_on_delivery=findViewById(R.id.cash_on_delivery);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        String object=getIntent().getStringExtra("orderArray");
        try {
            orderArray=new JSONArray(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cartHelper=new CartHelper(this);

        payment_recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        PaymentListAdapter paymentListAdapter=new PaymentListAdapter(this,orderArray);
        payment_recyclerView.setAdapter(paymentListAdapter);


        cash_on_delivery.setOnClickListener(view -> {
            progressDialog.show();
            cartHelper.addOrder(orderArray, new ResponsListener() {
                @Override
                public void onSuccess(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getBoolean("status")){
                            Intent intent=new Intent(CheckOutActivity.this,UserDashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(CheckOutActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CheckOutActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CheckOutActivity.this, e.toString() , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                }
            });
        });
    }
}