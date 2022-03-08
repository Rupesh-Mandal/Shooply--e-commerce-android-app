package com.firoz.shooply.user_dashboard.me.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.user_dashboard.helper.OrderHelper;
import com.firoz.shooply.user_dashboard.me.adapter.OrderPendingAdapter;
import com.firoz.shooply.util.PendingOrderOnclick;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderActivity extends AppCompatActivity {
    RecyclerView orderPendingRecycler;
    OrderHelper orderHelper;
    ProgressDialog progressDialog;
    ArrayList<OrderModel> orderModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        orderPendingRecycler=findViewById(R.id.orderPendingRecycler);
        orderPendingRecycler.setLayoutManager(new GridLayoutManager(this,1));
        orderHelper=new OrderHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.setCancelable(false);

        loadPending();
    }

    private void loadPending() {
        progressDialog.show();
        orderHelper.getPendingOrder(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                orderModelArrayList=new Gson().fromJson(response,new TypeToken<List<OrderModel>>() {}.getType());
                orderPendingRecycler.setAdapter(new OrderPendingAdapter(PendingOrderActivity.this, orderModelArrayList, new PendingOrderOnclick() {
                    @Override
                    public void onCancel(OrderModel orderModel) {
                        cancelOrder(orderModel);
                    }
                }));
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

            }
        });
    }

    private void cancelOrder(OrderModel orderModel) {
        progressDialog.show();
        orderHelper.cancelOrderByUser(orderModel, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                loadPending();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }
}