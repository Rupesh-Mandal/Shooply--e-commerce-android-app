package com.firoz.shooply.user_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.firoz.shooply.user_dashboard.helper.CartHelper;
import com.firoz.shooply.util.AddressBookOnClick;
import com.firoz.shooply.util.ResponsListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {

    Button ad_address_btn;
    private static RecyclerView address_recycler;
    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId;
    BottomSheetDialog bottomSheetDialog;

    CartHelper cartHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        sharedpreferences =getSharedPreferences("MyPREFERENCES",MODE_PRIVATE);
        cartHelper=new CartHelper(this);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            Log.e("abcd", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        address_recycler = findViewById(R.id.address_recycler);
        ad_address_btn = findViewById(R.id.ad_address_btn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        address_recycler.setLayoutManager(new GridLayoutManager(this, 1));
        loadAddress();
        ad_address_btn.setOnClickListener(v1 -> {
            showAddAddressDailog();
        });
    }

    private void loadAddress() {
        progressDialog.show();
        cartHelper.loadAddress(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<List<AddressBookModel>>(){}.getType();
                List<AddressBookModel> addressBookModelList = gson.fromJson(response, type);
                AddressBookAdapter addressBookAdapter = new AddressBookAdapter(SelectAddressActivity.this, addressBookModelList, new AddressBookOnClick() {
                    @Override
                    public void onClick(AddressBookModel addressBookModel) {

                    }
                });
                address_recycler.setAdapter(addressBookAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    private void showAddAddressDailog() {
        bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.add_address_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        EditText productDeliverAddress, userPhoneNumber;
        Button addAddress;

        productDeliverAddress = v.findViewById(R.id.productDeliverAddress);
        userPhoneNumber = v.findViewById(R.id.userPhoneNumber);
        addAddress = v.findViewById(R.id.addAddress);

        addAddress.setOnClickListener(view1 -> {
            if (productDeliverAddress.getText().toString().trim().isEmpty()) {
                productDeliverAddress.setError("Please provide valid address");
                productDeliverAddress.requestFocus();
            } else if (userPhoneNumber.getText().toString().trim().isEmpty()) {
                userPhoneNumber.setError("Please provide valid phone number");
                userPhoneNumber.requestFocus();
            } else {
                progressDialog.show();
                cartHelper.uploadAddress(productDeliverAddress.getText().toString().trim(), userPhoneNumber.getText().toString().trim(), new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                    }
                });
            }
        });


        bottomSheetDialog.show();

    }

}