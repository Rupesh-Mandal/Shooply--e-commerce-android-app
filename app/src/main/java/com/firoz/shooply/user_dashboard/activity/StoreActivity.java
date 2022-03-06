package com.firoz.shooply.user_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.user_dashboard.adapter.ProductAdapter;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.user_dashboard.helper.ProductHelper;
import com.firoz.shooply.user_dashboard.helper.UserStoreHelper;
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    String storeId;
    ImageView storeArtImage,storeLogoImage;
    TextView storeName,storeSlogan,storeEmail,storeDescription;
    RecyclerView allProductByStore;
    ProgressDialog progressDialog;

    UserStoreHelper userStoreHelper;
    CartHelper cartHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store);
        storeId=getIntent().getStringExtra("storeId");
        userStoreHelper=new UserStoreHelper(this);
        cartHelper=new CartHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        initView();
    }

    private void initView() {
        storeArtImage=findViewById(R.id.storeArtImage);
        storeLogoImage=findViewById(R.id.storeLogoImage);
        storeName=findViewById(R.id.storeName);
        storeSlogan=findViewById(R.id.storeSlogan);
        storeEmail=findViewById(R.id.storeEmail);
        storeDescription=findViewById(R.id.storeDescription);
        allProductByStore=findViewById(R.id.allProductByStore);

        allProductByStore.setLayoutManager(new GridLayoutManager(this,2));

        loadStoreData();
        loadStoreProduct();
    }

    private void loadStoreData() {
        progressDialog.show();
        userStoreHelper.loadStoreData(storeId, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                JSONObject responsObject= null;
                try {
                    responsObject = new JSONObject(response);
                    JSONObject jsonObject=responsObject.getJSONObject("StoreModel");
                    Glide.with(StoreActivity.this).load(jsonObject.getString("banner")).into(storeArtImage);
                    Glide.with(StoreActivity.this).load(jsonObject.getString("logo")).into(storeLogoImage);

                    storeName.setText(jsonObject.getString("storeName"));
                    storeSlogan.setText(jsonObject.getString("storeSlogan"));
                    storeDescription.setText(jsonObject.getString("storeDescription"));
                    storeEmail.setText(jsonObject.getString("storeEmail"));
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

    private void loadStoreProduct() {
        progressDialog.show();
        userStoreHelper.getProductByStoreId(storeId, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();

                ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                ProductAdapter productAdapter=new ProductAdapter(StoreActivity.this, productArrayList, new UserProductOnClick() {
                    @Override
                    public void addToCartOnclick(Product product) {
                        showAddToCartDailog(product);

                    }

                    @Override
                    public void onclick(Product product) {
                        Intent intent=new Intent(StoreActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product",new Gson().toJson(product));
                        startActivity(intent);
                    }
                });
                allProductByStore.setAdapter(productAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

            }
        });
    }
    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StoreActivity.this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(StoreActivity.this).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView imageView = v.findViewById(R.id.img);
        TextView count_negative,count_positive;
        TextView  count ,item_name,item_rate;
        AppCompatButton add_to_cart_btn;

        count_negative = v.findViewById(R.id.count_negative);
        count = v.findViewById(R.id.count);
        count_positive = v.findViewById(R.id.count_positive);
        item_name = v.findViewById(R.id.item_name);
        item_rate = v.findViewById(R.id.item_rate);
        add_to_cart_btn = v.findViewById(R.id.add_to_cart_btn);

        item_name.setText(product.getProductName());
        item_rate.setText(product.getProductRate());
        Glide.with(this).load(product.getProductImageLink()).into(imageView);

        count_negative.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());

            if (c>1){
                int i=c-1;
                count.setText(String.valueOf(i));
                item_rate.setText(String.valueOf((c-1)*r));
            }
        });
        count_positive.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());
            int i=c+1;

            count.setText(String.valueOf(i));
            item_rate.setText(String.valueOf((c+1)*r));

        });
        add_to_cart_btn.setOnClickListener(view1 -> {
            progressDialog.show();
            cartHelper.addToCart(product, Integer.parseInt(count.getText().toString()), new ResponsListener() {
                @Override
                public void onSuccess(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(StoreActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();

                    Toast.makeText(StoreActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

}