package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.loadSearch;

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
import android.widget.SearchView;
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
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    RecyclerView item_recycler_view;
    ArrayList<Product> productArrayList = new ArrayList<>();
    private TextView item_count_in_cart,check_out_btn;

    SearchView search_view;
    String q;
    CartHelper cartHelper;
    private  static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cartHelper=new CartHelper(this);
        item_count_in_cart=findViewById(R.id.item_count_in_cart);
        check_out_btn=findViewById(R.id.check_out_btn);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        item_recycler_view = findViewById(R.id.item_recycler_view);
        item_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));

        search_view=findViewById(R.id.search_view);
        q=getIntent().getStringExtra("q");
        search_view.setQuery(q,true);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadSearch(q);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        check_out_btn.setOnClickListener(view -> {
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        });
        loadSearch(q);
        loadCartCount();
    }

    private void loadSearch(String q1) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=loadSearch;
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        productArrayList = new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                        ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                        ProductAdapter productAdapter=new ProductAdapter(SearchActivity.this, productArrayList, new UserProductOnClick() {
                            @Override
                            public void addToCartOnclick(Product product) {
                                showAddToCartDailog(product);
                            }

                            @Override
                            public void onclick(Product product) {
                                Intent intent=new Intent(SearchActivity.this,ProductDetailActivity.class);
                                intent.putExtra("product",new Gson().toJson(product));
                                startActivity(intent);
                            }
                        });
                        item_recycler_view.setAdapter(productAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("key",q1);
                return params;

            }
        };
        queue.add(sr);
    }

    private void loadCartCount() {
        progressDialog.show();
        cartHelper.getAllCartList(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    item_count_in_cart.setText(jsonArray.length()+" Items");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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
        TextView  count,item_name,item_rate;
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
                        Toast.makeText(SearchActivity.this, jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}