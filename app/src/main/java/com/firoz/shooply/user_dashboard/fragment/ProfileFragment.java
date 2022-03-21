package com.firoz.shooply.user_dashboard.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firoz.shooply.R;
import com.firoz.shooply.user_dashboard.activity.AddressBookActivity;
import com.firoz.shooply.user_dashboard.me.activty.HistoryOrderActivity;
import com.firoz.shooply.user_dashboard.me.activty.PendingOrderActivity;
import com.firoz.shooply.user_dashboard.me.activty.SettingActivity;
import com.firoz.shooply.user_dashboard.me.activty.StartedOrderActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    View view;
    TextView name,email;
    CardView pending,started,history;
    LinearLayout setting,privacy_policy,about_us,addressBook;
    SharedPreferences sharedpreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        initView();
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        String object = sharedpreferences.getString("authUser", "");
        try {
            JSONObject jsonObject = new JSONObject(object);
            name.setText(jsonObject.getString("name"));
            email.setText(jsonObject.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        pending=view.findViewById(R.id.pending);
        started=view.findViewById(R.id.started);
        history=view.findViewById(R.id.history);
        setting=view.findViewById(R.id.setting);
        privacy_policy=view.findViewById(R.id.privacy_policy);
        about_us=view.findViewById(R.id.about_us);
        addressBook=view.findViewById(R.id.addressBook);

        pending.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), PendingOrderActivity.class));
        });
        started.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), StartedOrderActivity.class));

        });
        history.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), HistoryOrderActivity.class));

        });

        addressBook.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddressBookActivity.class));
        });

        setting.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), SettingActivity.class));
        });
    }
}