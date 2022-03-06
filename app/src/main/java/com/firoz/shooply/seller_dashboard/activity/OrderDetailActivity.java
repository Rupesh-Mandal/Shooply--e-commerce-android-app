package com.firoz.shooply.seller_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class OrderDetailActivity extends AppCompatActivity {

    ImageView productImageLink;
    TextView productName,productDescription,productRate,totalRate,productQuantity,deliverAddress,userName,userPhoneNumber;
    OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String object=getIntent().getStringExtra("OrderModel");
        orderModel=new Gson().fromJson(object, new TypeToken<OrderModel>() {
        }.getType());

        initView();
    }

    private void initView() {
        productImageLink=findViewById(R.id.productImageLink);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        productRate=findViewById(R.id.productRate);
        totalRate=findViewById(R.id.totalRate);
        productQuantity=findViewById(R.id.productQuantity);
        deliverAddress=findViewById(R.id.deliverAddress);
        userName=findViewById(R.id.userName);
        userPhoneNumber=findViewById(R.id.userPhoneNumber);


        Glide.with(this).load(orderModel.getProductImageLink()).into(productImageLink);
        productName.setText(orderModel.getProductName());
        productDescription.setText(orderModel.getProductDescription());
        productRate.setText(orderModel.getProductRate());
        totalRate.setText(orderModel.getProductTotalRate());
        productQuantity.setText(orderModel.getProductQuantity());
        deliverAddress.setText(orderModel.getProductDeliverAddress());
        userName.setText(orderModel.getUserName());
        userPhoneNumber.setText(orderModel.getUserPhoneNumber());
    }
}