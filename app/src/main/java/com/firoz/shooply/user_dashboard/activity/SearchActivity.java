package com.firoz.shooply.user_dashboard.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.user_dashboard.adapter.ProductAdapter;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.user_dashboard.helper.ProductHelper;
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView item_recycler_view;
    ArrayList<Product> productArrayList;

    ImageView filterBtn;

    SearchView search_view;
    String q;
    CartHelper cartHelper;
    ProductHelper productHelper;
    private static ProgressDialog progressDialog;
    ProductAdapter productAdapter;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    int pageNumber = 0;

    String sort = "ASC";
    String sortBy = "productName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cartHelper = new CartHelper(this);
        productHelper = new ProductHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        item_recycler_view = findViewById(R.id.item_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        item_recycler_view.setLayoutManager(gridLayoutManager);
        productArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(SearchActivity.this, productArrayList, new UserProductOnClick() {
            @Override
            public void addToCartOnclick(Product product) {
                showAddToCartDailog(product);
            }

            @Override
            public void onclick(Product product) {
                Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", new Gson().toJson(product));
                startActivity(intent);
            }
        });
        item_recycler_view.setAdapter(productAdapter);


        search_view = findViewById(R.id.search_view);
        filterBtn = findViewById(R.id.filterBtn);
        q = getIntent().getStringExtra("q");
        search_view.setQuery(q, true);
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

        loadSearch(q);

        item_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.e("abcd", dx + "" + dy);
                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            loadSearch(q);
                        }
                    }
                }
            }
        });

        filterBtn.setOnClickListener(view -> {
            showFilterDailog();
        });
    }

    private void showFilterDailog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.filter_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        RadioGroup fiterRadioGroup = v.findViewById(R.id.fiterRadioGroup);
        RadioGroup priceFilterRadioGroup = v.findViewById(R.id.priceFilterRadioGroup);

        RadioButton fiterByNameRadioBtn = v.findViewById(R.id.fiterByNameRadioBtn);
        RadioButton fiterByCategoryRadioBtn = v.findViewById(R.id.fiterByCategoryRadioBtn);
        RadioButton fiterByPriceRadioBtn = v.findViewById(R.id.fiterByPriceRadioBtn);

        RadioButton fiterHighToLowRadioBtn = v.findViewById(R.id.fiterHighToLowRadioBtn);
        RadioButton fiterLowToHighRadioBtn = v.findViewById(R.id.fiterLowToHighRadioBtn);

        Button searchBtn = v.findViewById(R.id.searchBtn);


        if (sortBy.equals("productName")) {
            fiterByNameRadioBtn.setChecked(true);
        } else if (sortBy.equals("sub_category")) {
            fiterByCategoryRadioBtn.setChecked(true);
        } else if (sortBy.equals("productRate")) {
            fiterByPriceRadioBtn.setChecked(true);
        }


        if (fiterRadioGroup.getCheckedRadioButtonId()==R.id.fiterByPriceRadioBtn){
            priceFilterRadioGroup.setVisibility(View.VISIBLE);
        }else {
            priceFilterRadioGroup.setVisibility(View.GONE);
        }

        fiterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId()==R.id.fiterByPriceRadioBtn){
                    priceFilterRadioGroup.setVisibility(View.VISIBLE);

                }
            }
        });

        searchBtn.setOnClickListener(view -> {
            pageNumber=0;
            productArrayList.clear();
            sort = "ASC";

            if (fiterRadioGroup.getCheckedRadioButtonId()==R.id.fiterByNameRadioBtn){
                sortBy = "productName";
                loadSearch(q);

            }else if (fiterRadioGroup.getCheckedRadioButtonId()==R.id.fiterByCategoryRadioBtn){
                sortBy = "sub_category";
                loadSearch(q);

            }else if (fiterRadioGroup.getCheckedRadioButtonId()==R.id.fiterByPriceRadioBtn){
                sortBy = "productRate";

                if (priceFilterRadioGroup.getCheckedRadioButtonId()==R.id.fiterHighToLowRadioBtn){
                    sort = "ASC";
                    loadSearch(q);

                }else {
                    sort = "DESC";
                    loadSearch(q);

                }
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void loadSearch(String q1) {
        progressDialog.show();
        loading = false;
        productHelper.searchProduct(q1, pageNumber, sort, sortBy, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                productArrayList.addAll(new Gson().fromJson(response, new TypeToken<List<Product>>() {
                }.getType()));
                productAdapter.notifyDataSetChanged();
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


    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView imageView = v.findViewById(R.id.img);
        TextView count_negative, count_positive;
        TextView count, item_name, item_rate;
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

            double r = Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());

            if (c > 1) {
                int i = c - 1;
                count.setText(String.valueOf(i));
                item_rate.setText(String.valueOf((c - 1) * r));
            }
        });
        count_positive.setOnClickListener(view1 -> {

            double r = Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());
            int i = c + 1;

            count.setText(String.valueOf(i));
            item_rate.setText(String.valueOf((c + 1) * r));

        });
        add_to_cart_btn.setOnClickListener(view1 -> {
            cartHelper.addToCart(product, Integer.parseInt(count.getText().toString()), new ResponsListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
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