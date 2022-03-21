package com.firoz.shooply.user_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.Product;
import com.firoz.shooply.util.UserProductOnClick;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    UserProductOnClick userProductOnClick;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, UserProductOnClick userProductOnClick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.userProductOnClick = userProductOnClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_product_item, null, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productRate.setText(product.getProductRate().trim());
        holder.mrp.setText(product.getMrp());

        double d = Double.parseDouble(product.getDiscount());
        int discount = (int) d;

        holder.discount.setText("-" + discount + "%");
        Glide.with(context).load(product.getProductImageLink()).into(holder.productImageLink);
        holder.itemView.setOnClickListener(v -> {
            userProductOnClick.onclick(product);
        });

//        if (position == productArrayList.size() - 1 || position == productArrayList.size() - 2) {
//            int paddingDp = 80;
//            float density = context.getResources().getDisplayMetrics().density;
//            int paddingPixel = (int)(paddingDp * density);
//            holder.itemView.setPadding(0, 0, 0, paddingPixel);
//        }
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName, productRate,mrp,discount;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink = itemView.findViewById(R.id.productImageLink);
            productName = itemView.findViewById(R.id.productName);
            productRate = itemView.findViewById(R.id.productRate);
            mrp = itemView.findViewById(R.id.mrp);
            discount = itemView.findViewById(R.id.discount);
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {


        view.requestLayout();
        view.setPadding(left,top,right,bottom);
    }
}
