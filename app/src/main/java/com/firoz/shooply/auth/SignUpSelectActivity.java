package com.firoz.shooply.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.firoz.shooply.R;

public class SignUpSelectActivity extends AppCompatActivity {

    Button seller,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_select);
        seller=findViewById(R.id.seller);
        user=findViewById(R.id.user);

        seller.setOnClickListener(view -> {
            startActivity(new Intent(this,SignUpSellerActivity.class));
            finish();
        });
        user.setOnClickListener(view -> {
            startActivity(new Intent(this,SignUpUserActivity.class));
            finish();
        });
    }
}