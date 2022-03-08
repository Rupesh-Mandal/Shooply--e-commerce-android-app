package com.firoz.shooply.user_dashboard.me.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.user_dashboard.helper.OrderHelper;
import com.firoz.shooply.user_dashboard.me.adapter.OrderHistoryAdapter;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderActivity extends AppCompatActivity {

    RecyclerView orderHistoryRecycler;
    OrderHelper orderHelper;
    ProgressDialog progressDialog;
    ArrayList<OrderModel> orderModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        orderHistoryRecycler=findViewById(R.id.orderHistoryRecycler);
        orderHistoryRecycler.setLayoutManager(new GridLayoutManager(this,1));

        orderHelper=new OrderHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.setCancelable(false);

        progressDialog.show();
        orderHelper.getOrderHistory(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                orderModelArrayList=new Gson().fromJson(response,new TypeToken<List<OrderModel>>() {}.getType());
                orderHistoryRecycler.setAdapter(new OrderHistoryAdapter(HistoryOrderActivity.this,orderModelArrayList));
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }
}