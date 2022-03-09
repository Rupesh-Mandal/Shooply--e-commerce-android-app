package com.firoz.shooply.user_dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.StoreCategory;
import com.firoz.shooply.user_dashboard.activity.CategoryActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class CategorySliderAdapter extends SliderViewAdapter<CategorySliderAdapter.SliderAdapterVH>{

    Context context;
    ArrayList<StoreCategory> storeCategoryArrayList;

    public CategorySliderAdapter(Context context, ArrayList<StoreCategory> storeCategoryArrayList) {
        this.context = context;
        this.storeCategoryArrayList = storeCategoryArrayList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterVH(LayoutInflater.from(context).inflate(R.layout.category_slider_item,null,false));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        StoreCategory storeCategory=storeCategoryArrayList.get(position);

        viewHolder.category.setText(storeCategory.getCategory());
        Glide.with(context).load(storeCategory.getBanner()).into(viewHolder.banner);

        viewHolder.itemView.setOnClickListener(view -> {

            Intent intent=new Intent(context, CategoryActivity.class);
            intent.putExtra("storecategory",storeCategory.getCategory());
            context.startActivity(intent);
        });

    }

    @Override
    public int getCount() {
        return storeCategoryArrayList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView banner;
        TextView category;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            category = itemView.findViewById(R.id.category);

        }
    }

}
