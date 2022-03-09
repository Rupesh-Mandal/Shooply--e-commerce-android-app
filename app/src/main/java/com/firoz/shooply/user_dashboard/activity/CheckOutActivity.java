package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.oderProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.firoz.shooply.model.AddressBookModel;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.model.CategoriesModel;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    ConstraintLayout addressLayout;
    RecyclerView payment_recyclerView;
    Button cash_on_delivery;
    ProgressDialog progressDialog;
    List<CartModel> selectCartModelList = new ArrayList<>();

    TextView productDeliverAddress, userPhoneNumber, addAddress, total, totalMrp;
    ImageView editAddress;
    CartHelper cartHelper;
    AddressBookModel addressBookModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        String object = getIntent().getStringExtra("selectCartModelList");
        selectCartModelList = new Gson().fromJson(object, new TypeToken<List<CartModel>>() {
        }.getType());
        initView();

        cartHelper = new CartHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");

        payment_recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        PaymentListAdapter paymentListAdapter = new PaymentListAdapter(this, selectCartModelList);
        payment_recyclerView.setAdapter(paymentListAdapter);

        loadDefaultAddress();

    }

    private void loadDefaultAddress() {
        progressDialog.show();
        cartHelper.getDefaultAddressByUserId(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        addressLayout.setVisibility(View.VISIBLE);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("AddressBookModel");
                        addressBookModel = new Gson().fromJson(jsonObject1.toString(), new TypeToken<AddressBookModel>() {
                        }.getType());

                        productDeliverAddress.setText(addressBookModel.getProductDeliverAddress());
                        userPhoneNumber.setText(addressBookModel.getUserPhoneNumber());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    private void initView() {
        addressLayout = findViewById(R.id.addressLayout);
        payment_recyclerView = findViewById(R.id.payment_recyclerView);
        cash_on_delivery = findViewById(R.id.cash_on_delivery);
        productDeliverAddress = findViewById(R.id.productDeliverAddress);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        addAddress = findViewById(R.id.addAddress);
        total = findViewById(R.id.total);
        totalMrp = findViewById(R.id.totalMrp);
        editAddress = findViewById(R.id.editAddress);



        double totalPr = 0;
        double totalMr = 0;

        for (int i = 0; i < selectCartModelList.size(); i++) {
            double rate = Double.parseDouble(selectCartModelList.get(i).getProductRate());
            double mrp = Double.parseDouble(selectCartModelList.get(i).getMrp());
            double quentity = Double.parseDouble(selectCartModelList.get(i).getQuantity());


            totalPr = totalPr + (rate * quentity);
            totalMr = totalMr + (mrp * quentity);
        }

        total.setText(String.valueOf(totalPr));
        totalMrp.setText(String.valueOf(totalMr));

        cash_on_delivery.setOnClickListener(view -> {
            if (addressLayout.getVisibility() == View.VISIBLE) {
                progressDialog.show();
                cartHelper.addOrder(selectCartModelList,addressBookModel, new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                Intent intent = new Intent(CheckOutActivity.this, UserDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckOutActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "Please Add Delivery Address", Toast.LENGTH_SHORT).show();
            }

        });
    }
}