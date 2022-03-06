package com.firoz.shooply.seller_dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.OrderModel;
import com.firoz.shooply.seller_dashboard.activity.OrderDetailActivity;
import com.firoz.shooply.util.OrderOnclick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.myViewHolder> {
    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    OrderOnclick orderOnclick;

    public SellerOrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, OrderOnclick orderOnclick) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.orderOnclick = orderOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.seller_order_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel orderModel=orderModelArrayList.get(position);
        Glide.with(context).load(orderModel.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(orderModel.getProductName());
        holder.totalRate.setText(orderModel.getProductTotalRate());
        holder.productQuantity.setText(orderModel.getProductQuantity());
        holder.deliverAddress.setText(orderModel.getProductDeliverAddress());

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, OrderDetailActivity.class);
            intent.putExtra("OrderModel",new Gson().toJson(orderModel));
            context.startActivity(intent);
        });

        String status=orderModel.getStatus();

        if (status.trim().equals("1")){
            holder.order_cancel.setVisibility(View.VISIBLE);
            holder.order_accept.setVisibility(View.VISIBLE);
            holder.deliverd_started.setVisibility(View.GONE);
            holder.deliverdFaild.setVisibility(View.GONE);
            holder.statusMessage.setVisibility(View.GONE);
        }else if (status.trim().equals("2")){
            holder.order_cancel.setVisibility(View.GONE);
            holder.order_accept.setVisibility(View.GONE);
            holder.deliverd_started.setVisibility(View.VISIBLE);
            holder.deliverdFaild.setVisibility(View.VISIBLE);
            holder.statusMessage.setVisibility(View.GONE);
        }else {
            holder.order_cancel.setVisibility(View.GONE);
            holder.order_accept.setVisibility(View.GONE);
            holder.deliverd_started.setVisibility(View.GONE);
            holder.deliverdFaild.setVisibility(View.GONE);
            holder.statusMessage.setVisibility(View.VISIBLE);
            holder.statusMessage.setText(orderModel.getStatusMessage());
        }


        holder.order_accept.setOnClickListener(view1 -> {
            orderOnclick.onAccept(orderModel);
        });

        holder.deliverd_started.setOnClickListener(view -> {
            orderOnclick.onDeliveryStarted(orderModel);
        });
        holder.deliverdFaild.setOnClickListener(view -> {
            orderOnclick.onDeliverdFaild(orderModel);
        });

        holder.order_cancel.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.bottom_shee_dailog_theam);
            View v = LayoutInflater.from(context).
                    inflate(R.layout.order_cancel_bottomsheet, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
            bottomSheetDialog.setContentView(v);

            ImageView productImageLink = v.findViewById(R.id.productImageLink);
            TextView productName = v.findViewById(R.id.productName);
            TextView productDescription = v.findViewById(R.id.productDescription);
            TextView productRate = v.findViewById(R.id.productRate);
            TextView deliverAddress = v.findViewById(R.id.deliverAddress);
            EditText cancel_reason=v.findViewById(R.id.cancel_reason);

            Button cancelBtn=v.findViewById(R.id.cancelBtn);

            Glide.with(context).load(orderModel.getProductImageLink()).into(productImageLink);

            productName.setText(orderModel.getProductName());
            productDescription.setText(orderModel.getProductDescription());
            productRate.setText(orderModel.getProductRate());
            deliverAddress.setText(orderModel.getProductDeliverAddress());

            cancelBtn.setOnClickListener(view1 -> {
                if (cancel_reason.getText().toString().trim().isEmpty()){
                    cancel_reason.setError("Please provide a reason");
                    cancel_reason.requestFocus();
                }else {
                    orderOnclick.onCancel(orderModel,cancel_reason.getText().toString().trim());
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName,totalRate,productQuantity,statusMessage,deliverAddress;
        Button order_cancel,order_accept, deliverd_started,deliverdFaild;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            productName=itemView.findViewById(R.id.productName);
            totalRate=itemView.findViewById(R.id.totalRate);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            order_cancel=itemView.findViewById(R.id.order_cancel);
            order_accept=itemView.findViewById(R.id.order_accept);
            deliverd_started =itemView.findViewById(R.id.deliverd_started);
            statusMessage=itemView.findViewById(R.id.statusMessage);
            deliverdFaild=itemView.findViewById(R.id.deliverdFaild);
            deliverAddress=itemView.findViewById(R.id.deliverAddress);

        }
    }
}
