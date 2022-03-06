package com.firoz.shooply.seller_dashboard;

import static com.firoz.shooply.seller_dashboard.fragment.ProductsFragment.loadProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firoz.shooply.R;
import com.firoz.shooply.auth.model.AuthUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SellerDashboardActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    boolean isReady = false;
    public static AuthUser authUserForSeller;
    SharedPreferences sharedpreferences;
    String object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        object=sharedpreferences.getString("authUser","");
        authUserForSeller=new Gson().fromJson(object, new TypeToken<AuthUser>() {}.getType());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        bottomNavigation = findViewById(R.id.sellerMeowBottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.product));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.all_order));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.store));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        navController.navigate(R.id.productsFragment);
                        break;
                    case 2:
                        navController.navigate(R.id.sellerOrdersFragment);
                        break;
                    case 3:
                        navController.navigate(R.id.storFragment);
                        break;
                }
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.e("abcd", String.valueOf(destination.getId()));
                switch (destination.getId()) {
                    case R.id.productsFragment:
                        if (isReady){
                            bottomNavigation.show(1, true);
                        }
                        break;
                    case R.id.sellerOrdersFragment:
                        if (isReady) {
                            bottomNavigation.show(2, true);
                        }
                        break;
                    case R.id.storFragment:
                        if (isReady){
                            bottomNavigation.show(3, true);
                        }
                        break;
                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
                isReady = true;
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.show(2, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            loadProduct(this);
        }
    }
}