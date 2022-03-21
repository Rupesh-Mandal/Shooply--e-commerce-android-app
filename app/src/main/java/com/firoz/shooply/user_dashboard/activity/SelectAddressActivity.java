package com.firoz.shooply.user_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firoz.shooply.R;
import com.firoz.shooply.checkout.adapter.AddressBookAdapter;
import com.firoz.shooply.model.AddressBookModel;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.util.AddressBookOnClick;
import com.firoz.shooply.util.ResponsListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {

    Button ad_address_btn;
    private static RecyclerView address_recycler;
    private static ProgressDialog progressDialog;
    BottomSheetDialog bottomSheetDialog;
    SharedPreferences sharedpreferences;

    String userName;
    CartHelper cartHelper;
    List<CartModel> selectCartModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        String userObject=sharedpreferences.getString("authUser","");
        try {
            JSONObject userJsonObject=new JSONObject(userObject);
            userName=userJsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String object = getIntent().getStringExtra("selectCartModelList");
        selectCartModelList = new Gson().fromJson(object, new TypeToken<List<CartModel>>() {
        }.getType());

        cartHelper = new CartHelper(this);

        address_recycler = findViewById(R.id.address_recycler);
        ad_address_btn = findViewById(R.id.ad_address_btn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        address_recycler.setLayoutManager(new GridLayoutManager(this, 1));
//        loadAddress();
        ad_address_btn.setOnClickListener(v1 -> {
            showAddAddressDailog();
        });
    }
//
//    private void loadAddress() {
//        progressDialog.show();
//        cartHelper.loadAddress(new ResponsListener() {
//            @Override
//            public void onSuccess(String response) {
//                progressDialog.dismiss();
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<AddressBookModel>>() {
//                }.getType();
//                List<AddressBookModel> addressBookModelList = gson.fromJson(response, type);
//                AddressBookAdapter addressBookAdapter = new AddressBookAdapter(SelectAddressActivity.this, addressBookModelList, new AddressBookOnClick() {
//                    @Override
//                    public void onClick(AddressBookModel addressBookModel) {
//                        try {
//                            JSONArray orderArray = new JSONArray();
//                            for (int i = 0; i < selectCartModelList.size(); i++) {
//                                JSONObject jsonObject = new JSONObject(new Gson().toJson(selectCartModelList.get(i)));
//                                jsonObject.put("productDeliverAddress",addressBookModel.getProductDeliverAddress());
//                                jsonObject.put("userPhoneNumber",addressBookModel.getUserPhoneNumber());
//                                jsonObject.put("userName", userName);
//
//                                double r1= Double.parseDouble(selectCartModelList.get(i).getProductRate());
//                                double c1 = Double.parseDouble(selectCartModelList.get(i).getQuantity());
//                                double rate=r1*c1;
//                                jsonObject.put("productTotalRate",String.valueOf(rate));
//
//                                orderArray.put(jsonObject);
//                            }
//                            Intent intent=new Intent(SelectAddressActivity.this,CheckOutActivity.class);
//                            intent.putExtra("orderArray",orderArray.toString());
//                            startActivity(intent);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                address_recycler.setAdapter(addressBookAdapter);
//            }
//
//            @Override
//            public void onError(String error) {
//                progressDialog.dismiss();
//            }
//        });
//    }

    private void showAddAddressDailog() {
//        bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
//        View v = LayoutInflater.from(this).
//                inflate(R.layout.add_address_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
//        bottomSheetDialog.setContentView(v);
//
//        EditText productDeliverAddress, userPhoneNumber;
//        Button addAddress;
//
//        productDeliverAddress = v.findViewById(R.id.productDeliverAddress);
//        userPhoneNumber = v.findViewById(R.id.userPhoneNumber);
//        addAddress = v.findViewById(R.id.addAddress);
//
//        addAddress.setOnClickListener(view1 -> {
//            if (productDeliverAddress.getText().toString().trim().isEmpty()) {
//                productDeliverAddress.setError("Please provide valid address");
//                productDeliverAddress.requestFocus();
//            } else if (userPhoneNumber.getText().toString().trim().isEmpty()) {
//                userPhoneNumber.setError("Please provide valid phone number");
//                userPhoneNumber.requestFocus();
//            } else {
//                progressDialog.show();
//                cartHelper.uploadAddress(productDeliverAddress.getText().toString().trim(), userPhoneNumber.getText().toString().trim(), new ResponsListener() {
//                    @Override
//                    public void onSuccess(String response) {
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        progressDialog.dismiss();
//                    }
//                });
//            }
//        });
//
//
//        bottomSheetDialog.show();

    }

}