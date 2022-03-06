package com.firoz.shooply.seller_dashboard.adapter;

import android.content.Context;
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
import com.firoz.shooply.model.Product;
import com.firoz.shooply.util.ProductOnclick;


import java.util.ArrayList;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.myViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    ProductOnclick productOnclick;

    public SellerProductAdapter(Context context, ArrayList<Product> productArrayList, ProductOnclick productOnclick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.productOnclick = productOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        Glide.with(context).load(product.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(product.getProductName());
        holder.productDescription.setText(product.getProductDescription());
        holder.productRate.setText(product.getProductRate());
        holder.mrp.setText(product.getMrp());

        double d = Double.parseDouble(product.getDiscount());
        int discount = (int) d;

        holder.discount.setText("-" + discount + "%");

        holder.menu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.menu);
            //inflating menu from xml resource
            popup.inflate(R.menu.seller_product_option_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.editBtn:
                            productOnclick.onEdit(product);
                            break;
                        case R.id.deletBtn:
                            productOnclick.onDelet(product);
                            break;

                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        });

//        holder.deletBtn.setOnClickListener(view -> {
//            productOnclick.onDelet(product);
//        });
//        holder.editBtn.setOnClickListener(view -> {
//            productOnclick.onEdit(product);
//        });`

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink, menu;
        TextView productRate, productDescription, productName, mrp, discount;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink = itemView.findViewById(R.id.productImageLink);
            menu = itemView.findViewById(R.id.menu);
//            deletBtn=itemView.findViewById(R.id.deletBtn);
//            editBtn=itemView.findViewById(R.id.editBtn);
            productRate = itemView.findViewById(R.id.productRate);
            productDescription = itemView.findViewById(R.id.productDescription);
            productName = itemView.findViewById(R.id.productName);
            mrp = itemView.findViewById(R.id.mrp);
            discount = itemView.findViewById(R.id.discount);

        }
    }
}
