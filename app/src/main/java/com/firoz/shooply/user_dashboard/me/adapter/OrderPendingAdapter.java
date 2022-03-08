package com.firoz.shooply.user_dashboard.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.user_dashboard.activity.ProductDetailByProductIdActivity;
import com.firoz.shooply.util.PendingOrderOnclick;

import java.util.ArrayList;

public class OrderPendingAdapter extends RecyclerView.Adapter<OrderPendingAdapter.myViewHolder>{

    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    PendingOrderOnclick pendingOrderOnclick;


    public OrderPendingAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, PendingOrderOnclick pendingOrderOnclick) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.pendingOrderOnclick = pendingOrderOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.pending_order_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel orderModel=orderModelArrayList.get(position);
        Glide.with(context).load(orderModel.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(orderModel.getProductName());
        holder.totalRate.setText(orderModel.getProductTotalRate());
        holder.productQuantity.setText(orderModel.getQuantity());
        holder.statusMessage.setText(orderModel.getStatusMessage());

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ProductDetailByProductIdActivity.class);
            intent.putExtra("productId",orderModel.getProductId());
            context.startActivity(intent);
        });

        holder.menu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.menu);
            //inflating menu from xml resource
            popup.inflate(R.menu.pending_order_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.cancel:
                            pendingOrderOnclick.onCancel(orderModel);
                            break;

                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink,menu;
        TextView productName,totalRate,productQuantity,statusMessage;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            productName=itemView.findViewById(R.id.productName);
            totalRate=itemView.findViewById(R.id.totalRate);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            statusMessage=itemView.findViewById(R.id.statusMessage);
            menu=itemView.findViewById(R.id.menu);

        }
    }
}
