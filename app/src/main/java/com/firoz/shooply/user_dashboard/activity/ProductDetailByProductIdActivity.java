package com.firoz.shooply.user_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.user_dashboard.adapter.HorizentalProductAdapter;
import com.firoz.shooply.user_dashboard.adapter.ProductAdapter;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.user_dashboard.helper.ProductHelper;
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailByProductIdActivity extends AppCompatActivity {
    Product productObject;
    ImageView productImageLink;
    TextView productRate,mrp,discount,productName,productDescription;
    RecyclerView recommendedByStoreRecycler,storecategoryRecycler;
    Button storeBtn,addToCartBtn;

    ProductHelper productHelper;
    CartHelper cartHelper;
    ProgressDialog progressDialog;

    SharedPreferences sharedpreferences;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_by_product_id);
        productHelper=new ProductHelper(this);
        cartHelper=new CartHelper(this);
        String productId=getIntent().getStringExtra("productId");
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        String object = sharedpreferences.getString("authUser", "");
        try {
            JSONObject jsonObject = new JSONObject(object);
            userId = jsonObject.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.setCancelable(false);

        progressDialog.show();
        productHelper.findByProductId(productId, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                productObject=new Gson().fromJson(response,new TypeToken<Product>() {}.getType());
                initView();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }


    private void initView() {
        productImageLink=findViewById(R.id.productImageLink);
        storeBtn=findViewById(R.id.storeBtn);
        productRate=findViewById(R.id.productRate);
        mrp=findViewById(R.id.mrp);
        discount=findViewById(R.id.discount);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        recommendedByStoreRecycler=findViewById(R.id.recommendedByStoreRecycler);
        storecategoryRecycler=findViewById(R.id.storecategoryRecycler);
        addToCartBtn=findViewById(R.id.addToCartBtn);

        Glide.with(this).load(productObject.getProductImageLink()).into(productImageLink);
        productRate.setText(productObject.getProductRate());
        mrp.setText(productObject.getMrp());

        double d = Double.parseDouble(productObject.getDiscount());
        int d1 = (int) d;
        discount.setText("-" + d1 + "%");

        productName.setText(productObject.getProductName());
        productDescription.setText(productObject.getProductDescription());

        recommendedByStoreRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        storecategoryRecycler.setLayoutManager(new GridLayoutManager(this,2));

        loadRecommendedByStore();
        loadStorecategory();

        addToCartBtn.setOnClickListener(view -> {
            showAddToCartDailog(productObject);
        });

        storeBtn.setOnClickListener(view -> {
            Intent intent=new Intent(this,StoreActivity.class);
            intent.putExtra("storeId",productObject.getStoreId());
            startActivity(intent);
        });
    }


    private void loadStorecategory() {
        progressDialog.show();
        productHelper.getProductByStorecategory(productObject.getStorecategory(), new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                ProductAdapter productAdapter=new ProductAdapter(ProductDetailByProductIdActivity.this, productArrayList, new UserProductOnClick() {
                    @Override
                    public void addToCartOnclick(Product product) {
                        showAddToCartDailog(product);

                    }

                    @Override
                    public void onclick(Product product) {
                        Intent intent=new Intent(ProductDetailByProductIdActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product",new Gson().toJson(product));
                        startActivity(intent);
                    }
                });
                storecategoryRecycler.setAdapter(productAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

            }
        });
    }

    private void loadRecommendedByStore() {
        progressDialog.show();
        productHelper.getProductByRecommendedByStore(productObject.getStoreId(), new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                HorizentalProductAdapter horizentalProductAdapter=new HorizentalProductAdapter(ProductDetailByProductIdActivity.this, productArrayList, new UserProductOnClick() {
                    @Override
                    public void addToCartOnclick(Product product) {
                        showAddToCartDailog(product);
                    }

                    @Override
                    public void onclick(Product product) {
                        Intent intent=new Intent(ProductDetailByProductIdActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product",new Gson().toJson(product));
                        startActivity(intent);
                    }
                });
                recommendedByStoreRecycler.setAdapter(horizentalProductAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

            }
        });
    }

    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductDetailByProductIdActivity.this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(ProductDetailByProductIdActivity.this).
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
                        Toast.makeText(ProductDetailByProductIdActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();

                    Toast.makeText(ProductDetailByProductIdActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void showOrderDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductDetailByProductIdActivity.this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(ProductDetailByProductIdActivity.this).
                inflate(R.layout.order_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView imageView = v.findViewById(R.id.img);
        TextView count_negative,count_positive;
        TextView  count ,item_name,item_rate;
        AppCompatButton order;

        count_negative = v.findViewById(R.id.count_negative);
        count = v.findViewById(R.id.count);
        count_positive = v.findViewById(R.id.count_positive);
        item_name = v.findViewById(R.id.item_name);
        item_rate = v.findViewById(R.id.item_rate);
        order = v.findViewById(R.id.order);

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
        order.setOnClickListener(view -> {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject(new Gson().toJson(product));
                jsonBody.put("quantity",count.getText().toString());
                jsonBody.put("userId", userId);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonBody);

                final String mRequestBody = jsonArray.toString();
                Intent intent=new Intent(ProductDetailByProductIdActivity.this, CheckOutActivity.class);
                intent.putExtra("selectCartModelList",mRequestBody);
                startActivity(new Intent(intent));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        bottomSheetDialog.show();
    }

}