package com.firoz.shooply.checkout.adapter;

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
import com.firoz.shooply.model.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.myViewHolder> {

    Context context;
    List<CartModel> selectCartModelList;

    public PaymentListAdapter(Context context, List<CartModel> selectCartModelList) {
        this.context = context;
        this.selectCartModelList = selectCartModelList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.checkout_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CartModel cartModel=selectCartModelList.get(position);
        Glide.with(context).load(cartModel.getProductImageLink()).into(holder.productImageLink);

        double r1= Double.parseDouble(cartModel.getProductRate());
        double m1= Double.parseDouble(cartModel.getMrp());
        double c1 = Double.parseDouble(cartModel.getQuantity());

        double total=r1*c1;

        double totalWithOutDiscount=r1*c1;
        double discountAmount=totalWithOutDiscount-total;

        double d = Double.parseDouble(cartModel.getDiscount());
        int discount = (int) d;


        holder.productName.setText(cartModel.getProductName());
       holder.productDescription.setText(cartModel.getProductDescription());
       holder.productRate.setText(cartModel.getProductRate());
       holder.mrp.setText(cartModel.getMrp());
       holder.discount.setText("-"+discount+"%");
       holder.productQuantity.setText(cartModel.getQuantity());
       holder.totalRate.setText(String.valueOf(total));
       holder.discountAmount.setText(String.valueOf(discountAmount));

    }

    @Override
    public int getItemCount() {
        return selectCartModelList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productDescription,productRate,mrp,discount,productQuantity,totalRate,discountAmount;
        ImageView productImageLink;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.productName);
            productDescription=itemView.findViewById(R.id.productDescription);
            productRate=itemView.findViewById(R.id.productRate);
            mrp=itemView.findViewById(R.id.mrp);
            discount=itemView.findViewById(R.id.discount);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            totalRate=itemView.findViewById(R.id.totalRate);
            discountAmount=itemView.findViewById(R.id.discountAmount);
            productImageLink=itemView.findViewById(R.id.productImageLink);
        }
    }
}
