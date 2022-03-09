package com.firoz.shooply.user_dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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

public class AddressBookActivity extends AppCompatActivity {

    RecyclerView addressBookRecycler;
    ImageView add_address;
    List<AddressBookModel> addressBookModelList;
    CartHelper cartHelper;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        cartHelper = new CartHelper(this);
        initView();
        loadAddress();
    }


    private void initView() {
        addressBookRecycler = findViewById(R.id.addressBookRecycler);
        add_address = findViewById(R.id.add_address);

        addressBookRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        add_address.setOnClickListener(view -> {
            showAddAddressDailog();
        });
    }

    private void loadAddress() {
        progressDialog.show();
        cartHelper.loadAddress(new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                List<AddressBookModel> addressBookModelList = new Gson().fromJson(response, new TypeToken<List<AddressBookModel>>() {
                }.getType());
                AddressBookAdapter addressBookAdapter = new AddressBookAdapter(AddressBookActivity.this, addressBookModelList, new AddressBookOnClick() {
                    @Override
                    public void onEdit(AddressBookModel addressBookModel) {
                        onEditDailog(addressBookModel);
                    }

                    @Override
                    public void onDelete(AddressBookModel addressBookModel) {
                        progressDialog.show();
                        Log.e("abcd",addressBookModel.getAddressId());
                        cartHelper.deleteByAddressId(addressBookModel.getAddressId(), new ResponsListener() {
                            @Override
                            public void onSuccess(String response) {
                                progressDialog.dismiss();
                                loadAddress();
                            }

                            @Override
                            public void onError(String error) {
                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onSetDefault(AddressBookModel addressBookModel) {
                        progressDialog.show();
                        setDefaultDeliveryAddress(addressBookModel);

                    }
                });
                addressBookRecycler.setAdapter(addressBookAdapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    private void setDefaultDeliveryAddress(AddressBookModel addressBookModel) {
        progressDialog.show();
        cartHelper.setDefaultAddress(addressBookModel.getAddressId(), new ResponsListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                loadAddress();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    private void onEditDailog(AddressBookModel addressBookModel) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.update_address_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        EditText productDeliverAddress, userPhoneNumber;
        Button addAddress;

        productDeliverAddress = v.findViewById(R.id.productDeliverAddress);
        userPhoneNumber = v.findViewById(R.id.userPhoneNumber);
        addAddress = v.findViewById(R.id.addAddress);

        productDeliverAddress.setText(addressBookModel.getProductDeliverAddress());
        userPhoneNumber.setText(addressBookModel.getUserPhoneNumber());

        addAddress.setOnClickListener(view1 -> {
            if (productDeliverAddress.getText().toString().trim().isEmpty()) {
                productDeliverAddress.setError("Please provide valid address");
                productDeliverAddress.requestFocus();
            } else if (userPhoneNumber.getText().toString().trim().isEmpty()) {
                userPhoneNumber.setError("Please provide valid phone number");
                userPhoneNumber.requestFocus();
            } else {
                bottomSheetDialog.dismiss();
                progressDialog.show();
                addressBookModel.setProductDeliverAddress(productDeliverAddress.getText().toString().trim());
                addressBookModel.setUserPhoneNumber(userPhoneNumber.getText().toString().trim());
                cartHelper.updateAddress(addressBookModel, new ResponsListener() {
                    @Override
                    public void onSuccess(String response) {
                        progressDialog.dismiss();
                        loadAddress();
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

    private void showAddAddressDailog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.add_address_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        EditText productDeliverAddress, userPhoneNumber;
        Button addAddress;
        CheckBox useAsDefaultAddress=v.findViewById(R.id.useAsDefaultAddress);

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
                        if (useAsDefaultAddress.isChecked()){
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                JSONObject addressBookObj=jsonObject.getJSONObject("AddressBookModel");
                                AddressBookModel addressBookModel = new Gson().fromJson(addressBookObj.toString(), new TypeToken<AddressBookModel>() {}.getType());
                                setDefaultDeliveryAddress(addressBookModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                loadAddress();
                            }
                        }else {
                            loadAddress();
                        }

                        bottomSheetDialog.dismiss();
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