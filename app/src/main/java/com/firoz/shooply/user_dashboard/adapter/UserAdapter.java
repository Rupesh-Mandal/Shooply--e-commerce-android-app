package com.firoz.shooply.user_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.util.UserOrderOnclick;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.myViewHolder>{
    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    UserOrderOnclick userOrderOnclick;

    public UserAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, UserOrderOnclick userOrderOnclick) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.userOrderOnclick = userOrderOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.user_order_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel orderModel=orderModelArrayList.get(position);
        Glide.with(context).load(orderModel.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(orderModel.getProductName());
        holder.totalRate.setText(orderModel.getProductTotalRate());
        holder.productQuantity.setText(orderModel.getProductQuantity());
        holder.statusMessage.setText(orderModel.getStatusMessage());

        String status=orderModel.getStatus();

        if (status.trim().equals("1")){


        }
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName,totalRate,productQuantity,statusMessage;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            productName=itemView.findViewById(R.id.productName);
            totalRate=itemView.findViewById(R.id.totalRate);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            statusMessage=itemView.findViewById(R.id.statusMessage);

        }
    }
}
