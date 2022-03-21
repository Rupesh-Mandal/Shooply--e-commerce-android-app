package com.firoz.shooply.user_dashboard.me.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

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
    ArrayList<OrderModel> orderModelArrayList;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    int pageNumber=0;

    OrderPendingAdapter orderPendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        orderModelArrayList=new ArrayList<>();

        orderPendingRecycler=findViewById(R.id.orderPendingRecycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        orderPendingRecycler.setLayoutManager(gridLayoutManager);
        orderPendingAdapter=new OrderPendingAdapter(PendingOrderActivity.this, orderModelArrayList, new PendingOrderOnclick() {
            @Override
            public void onCancel(OrderModel orderModel) {
                cancelOrder(orderModel);
            }
        });
        orderPendingRecycler.setAdapter(orderPendingAdapter);

        orderHelper=new OrderHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.setCancelable(false);

        loadPending();


        orderPendingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadPending();
                        }
                    }
                }
            }
        });
    }

    private void loadPending() {
        progressDialog.show();
        loading = false;
        orderHelper.getPendingOrder(pageNumber,new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                orderModelArrayList.addAll(new Gson().fromJson(response,new TypeToken<List<OrderModel>>() {}.getType()));
                orderPendingAdapter.notifyDataSetChanged();
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