package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.findByStorecategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.user_dashboard.adapter.ProductAdapter;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.user_dashboard.helper.ProductHelper;
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    TextView categoriName;
    RecyclerView categoryRecycler;
    ImageView cart;
    String storecategory;
    ProgressDialog progressDialog;
    CartHelper cartHelper;
    ProductHelper productHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoriName=findViewById(R.id.categoriName);
        categoryRecycler=findViewById(R.id.categoryRecycler);
        cart=findViewById(R.id.cart);

        cart.setOnClickListener(view -> {
            startActivity(new Intent(this,CartActivity.class));
        });

        productHelper=new ProductHelper(this);
        cartHelper=new CartHelper(this);

        storecategory=getIntent().getStringExtra("storecategory");
        categoriName.setText(storecategory);

        categoryRecycler.setLayoutManager(new GridLayoutManager(this,2));
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        loadProduct();


    }

    private void loadProduct() {
        progressDialog.show();
        productHelper.findByStorecategory(storecategory, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                ProductAdapter productAdapter=new ProductAdapter(CategoryActivity.this, productArrayList, new UserProductOnClick() {
                    @Override
                    public void addToCartOnclick(Product product) {
                        showAddToCartDailog(product);
                    }

                    @Override
                    public void onclick(Product product) {
                        Intent intent=new Intent(CategoryActivity.this,ProductDetailActivity.class);
                        intent.putExtra("product",new Gson().toJson(product));
                        startActivity(intent);

                    }
                });
                categoryRecycler.setAdapter(productAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();

            }
        });

    }

    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
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
            cartHelper.addToCart(product, Integer.parseInt(count.getText().toString()), new ResponsListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(CategoryActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(CategoryActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

}