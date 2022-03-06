package com.firoz.shooply.user_dashboard.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firoz.shooply.R;
import com.firoz.shooply.checkout.adapter.CartAdapter;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.util.CartOnclick;
import com.firoz.shooply.util.ResponsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    RecyclerView cart_list_recyclr;
    View view;
    CartAdapter cartAdapter;
    TextView total,check_out_btn;
    ProgressDialog progressDialog;
    private static List<CartModel> cartModelArrayList=new ArrayList<>();

    CartHelper cartHelper;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        initView();
    }

    private void initView() {
        cart_list_recyclr=view.findViewById(R.id.cart_list_recyclr);
        total=view.findViewById(R.id.total);
        check_out_btn=view.findViewById(R.id.check_out_btn);
        cart_list_recyclr.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        cartHelper=new CartHelper(getContext());

        loadCart();

        check_out_btn.setOnClickListener(v -> {
            if (cartModelArrayList.size()>0){

            }else {
                Toast.makeText(getContext(), "Please Add Some Item in Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCart() {
        cartHelper.getAllCartList(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                cartModelArrayList = new Gson().fromJson(response, new TypeToken<List<CartModel>>() {
                }.getType());
                setCartList();
                double totalAmount=0;
                for (int i=0;i<cartModelArrayList.size();i++){
                    CartModel cartModel=cartModelArrayList.get(i);
                    double rate= Double.parseDouble(cartModel.getProductRate());
                    double quntity= Double.parseDouble(cartModel.getQuantity());
                    double amount=rate*quntity;

                    totalAmount=totalAmount+amount;
                }
                total.setText(String.valueOf(totalAmount));
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void setCartList() {
        CartAdapter cartAdapter=new CartAdapter(getContext(), cartModelArrayList, new CartOnclick() {
            @Override
            public void increaseCart(CartModel cartModel) {
                cartHelper.updateCart(cartModel, new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        loadCart();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }

            @Override
            public void decreaseCart(CartModel cartModel) {
                cartHelper.updateCart(cartModel, new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        loadCart();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }

            @Override
            public void deleteCart(CartModel cartModel) {
                cartHelper.deleteCart(cartModel, new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        loadCart();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
        cart_list_recyclr.setAdapter(cartAdapter);
    }

}