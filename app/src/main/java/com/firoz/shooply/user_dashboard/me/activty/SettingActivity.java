package com.firoz.shooply.user_dashboard.me.activty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.firoz.shooply.R;
import com.firoz.shooply.splash.SplashActivity;
import com.firoz.shooply.user_dashboard.activity.AddressBookActivity;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    LinearLayout addressBook,logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);

        addressBook=findViewById(R.id.addressBook);
        logOut=findViewById(R.id.logOut);

        addressBook.setOnClickListener(view -> {
            startActivity(new Intent(this, AddressBookActivity.class));
        });

        logOut.setOnClickListener(view -> {
            sharedpreferences.edit().clear().commit();
            Intent intent = new Intent(SettingActivity.this,
                    SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}