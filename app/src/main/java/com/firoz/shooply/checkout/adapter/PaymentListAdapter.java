package com.firoz.shooply.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firoz.shooply.R;
import com.firoz.shooply.model.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.myViewHolder> {

    Context context;
    JSONArray orderArray;


    public PaymentListAdapter(Context context, JSONArray orderArray) {
        this.context = context;
        this.orderArray = orderArray;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.payment_list_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            JSONObject jsonObject=orderArray.getJSONObject(position);
            holder.name.setText(jsonObject.getString("productName"));
            holder.quantity.setText(jsonObject.getString("quantity"));
            holder.rate.setText(jsonObject.getString("productRate"));

            double q= Double.parseDouble(jsonObject.getString("quantity"));
            double r= Double.parseDouble(jsonObject.getString("productRate"));
            double a=q*r;

            holder.amount.setText(String.valueOf(a));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return orderArray.length();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView name,quantity,rate,amount;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.quantity);
            rate=itemView.findViewById(R.id.rate);
            amount=itemView.findViewById(R.id.amount);
        }
    }
}
