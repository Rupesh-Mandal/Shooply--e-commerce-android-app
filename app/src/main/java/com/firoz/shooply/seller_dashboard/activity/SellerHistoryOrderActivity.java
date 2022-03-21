package com.firoz.shooply.seller_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.seller_dashboard.helper.SellerHelper;
import com.firoz.shooply.user_dashboard.helper.OrderHelper;
import com.firoz.shooply.user_dashboard.me.activty.HistoryOrderActivity;
import com.firoz.shooply.user_dashboard.me.adapter.OrderHistoryAdapter;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SellerHistoryOrderActivity extends AppCompatActivity {
    RecyclerView orderHistoryRecycler;
    ProgressDialog progressDialog;
    ArrayList<OrderModel> orderModelArrayList;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    OrderHistoryAdapter orderHistoryAdapter;
    int pageNumber=0;
    SellerHelper sellerHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_history_order);

        orderModelArrayList=new ArrayList<>();
        orderHistoryRecycler=findViewById(R.id.orderHistoryRecycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        orderHistoryRecycler.setLayoutManager(gridLayoutManager);
        orderHistoryAdapter=new OrderHistoryAdapter(SellerHistoryOrderActivity.this,orderModelArrayList);
        orderHistoryRecycler.setAdapter(orderHistoryAdapter);

        sellerHelper=new SellerHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.setCancelable(false);


        loadHistory();

        orderHistoryRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.e("abcd",dx +""+dy);
                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            loadHistory();
                        }
                    }
                }
            }
        });
    }

    private void loadHistory() {
        progressDialog.show();
        loading = false;
        sellerHelper.getOrderHistory(pageNumber,new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                orderModelArrayList.addAll(new Gson().fromJson(response,new TypeToken<List<OrderModel>>() {}.getType()));
                orderHistoryAdapter.notifyDataSetChanged();
                pageNumber++;
                loading = true;
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                loading = true;

            }
        });
    }
}