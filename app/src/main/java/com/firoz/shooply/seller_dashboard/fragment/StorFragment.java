package com.firoz.shooply.seller_dashboard.fragment;

import static com.firoz.shooply.seller_dashboard.SellerDashboardActivity.authUserForSeller;
import static com.firoz.shooply.util.Constant.getStoreByStoreId;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.firoz.shooply.auth.SignInActivity;
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.auth.model.UserType;
import com.firoz.shooply.seller_dashboard.SellerDashboardActivity;
import com.firoz.shooply.seller_dashboard.activity.UpdateStoreActivity;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class StorFragment extends Fragment {

    LinearLayout setting,privacy_policy,about_us;
    View view;

    ImageView storeArtImage,storeLogoImage,editBtn;
    TextView storeName,storeSlogan,storeDescription,storeEmail;
    JSONObject jsonObject;


    public StorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        jsonObject=new JSONObject();
        initView();
    }

    private void initView() {
        setting=view.findViewById(R.id.setting);
        privacy_policy=view.findViewById(R.id.privacy_policy);
        about_us=view.findViewById(R.id.about_us);
        storeArtImage=view.findViewById(R.id.storeArtImage);
        storeLogoImage=view.findViewById(R.id.storeLogoImage);
        storeName=view.findViewById(R.id.storeName);
        storeSlogan=view.findViewById(R.id.storeSlogan);
        storeDescription=view.findViewById(R.id.storeDescription);
        storeEmail=view.findViewById(R.id.storeEmail);
        editBtn=view.findViewById(R.id.editBtn);

        editBtn.setOnClickListener(view1 -> {
            Intent intent=new Intent(getContext(), UpdateStoreActivity.class);
            intent.putExtra("StoreModel",jsonObject.toString());
            startActivity(intent);
        });

        loadStoreData();
    }

    private void setLoadedData() throws JSONException {
        Glide.with(getContext()).load(jsonObject.getString("banner")).into(storeArtImage);
        Glide.with(getContext()).load(jsonObject.getString("logo")).into(storeLogoImage);

        storeName.setText(jsonObject.getString("storeName"));
        storeSlogan.setText(jsonObject.getString("storeSlogan"));
        storeDescription.setText(jsonObject.getString("storeDescription"));
        storeEmail.setText(jsonObject.getString("storeEmail"));
    }


    private void loadStoreData() {
        String Url=getStoreByStoreId;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Waite");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject1=new JSONObject(response);
                            if (jsonObject1.getBoolean("status")){
                                jsonObject=jsonObject1.getJSONObject("StoreModel");
                              setLoadedData();
                            }else {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("abcd",e.getMessage());
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("storeId",authUserForSeller.getUserId());
                return params;
            }
        };
        queue.add(sr);
    }

}