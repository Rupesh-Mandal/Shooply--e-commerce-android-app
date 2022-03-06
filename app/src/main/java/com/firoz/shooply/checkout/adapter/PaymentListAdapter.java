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

import java.util.List;


public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.myViewHolder> {

    Context context;
    List<CartModel> cartModelList;


    public PaymentListAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.payment_list_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CartModel cartModel=cartModelList.get(position);
        holder.name.setText(cartModel.getProductName());
        holder.quantity.setText(String.valueOf(cartModel.getQuantity()));
        holder.rate.setText(cartModel.getProductRate());

        double q= Double.parseDouble(cartModel.getQuantity());
        double r= Double.parseDouble(cartModel.getProductRate());
        double a=q*r;

        holder.amount.setText(String.valueOf(a));

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
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
