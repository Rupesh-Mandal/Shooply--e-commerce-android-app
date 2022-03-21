package com.firoz.shooply.user_dashboard.fragment;


import static com.firoz.shooply.util.Constant.getAllCategory;
import static com.firoz.shooply.util.Constant.getAllProductUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.firoz.shooply.model.StoreCategory;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.firoz.shooply.user_dashboard.activity.CategoryActivity;
import com.firoz.shooply.user_dashboard.activity.ProductDetailActivity;
import com.firoz.shooply.user_dashboard.activity.SearchActivity;
import com.firoz.shooply.model.CategoriesModel;
import com.firoz.shooply.user_dashboard.adapter.CategoryAdapter;
import com.firoz.shooply.user_dashboard.adapter.CategorySliderAdapter;
import com.firoz.shooply.user_dashboard.adapter.ProductAdapter;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.user_dashboard.helper.ProductHelper;
import com.firoz.shooply.util.CategoryOnClick;
import com.firoz.shooply.util.ResponsListener;
import com.firoz.shooply.util.UserProductOnClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    View view;
    RecyclerView  all_product_recyclerView;
    SearchView search_view;
    SharedPreferences sharedpreferences;
    private static ProgressDialog progressDialog;
    SliderView sliderView;
    CartHelper cartHelper;
    ProductHelper productHelper;
    ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int pageNumber=0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        productArrayList=new ArrayList<>();

        all_product_recyclerView = view.findViewById(R.id.all_product_recyclerView);
        productAdapter=new ProductAdapter(getContext(), productArrayList, new UserProductOnClick() {
            @Override
            public void addToCartOnclick(Product product) {
                showAddToCartDailog(product);
            }

            @Override
            public void onclick(Product product) {
                Intent intent=new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product",new Gson().toJson(product));
                startActivity(intent);
            }
        });
        all_product_recyclerView.setAdapter(productAdapter);


        sliderView = view.findViewById(R.id.imageSlider);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");
        cartHelper=new CartHelper(getContext());
        productHelper=new ProductHelper(getContext());

        search_view = view.findViewById(R.id.search_view);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        all_product_recyclerView.setLayoutManager(gridLayoutManager);


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("q", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        loadCategory();
        loadAllProduct();

        all_product_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadAllProduct();
                        }
                    }
                }
            }
        });
    }

    private void loadCategory() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getAllCategory;
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        ArrayList<StoreCategory> storeCategoryArrayList=new Gson().fromJson(response, new TypeToken<List<StoreCategory>>() {}.getType());
                        sliderView.setSliderAdapter(new CategorySliderAdapter(getContext(),storeCategoryArrayList));

//                        CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(), categoriesModelArrayList, new CategoryOnClick() {
//                            @Override
//                            public void onClick(CategoriesModel categoriesModel) {
//                                Intent intent=new Intent(getContext(), CategoryActivity.class);
//                                intent.putExtra("productCategory",categoriesModel.getName());
//                                startActivity(intent);
//                            }
//                        });
//                        categories_recyclerView.setAdapter(categoryAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(sr);
    }

    private void loadAllProduct() {
        loading = false;
        productHelper.findAllProduct(pageNumber, new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                productArrayList.addAll(new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType()));
                productAdapter.notifyDataSetChanged();
                pageNumber++;
                loading = true;

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                loading = true;

            }
        });

    }
    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(getContext()).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
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
                        Toast.makeText(getContext(), jsonObject.getString("massage"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}