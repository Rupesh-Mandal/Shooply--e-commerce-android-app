package com.firoz.shooply.checkout.adapter;

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

import com.firoz.shooply.R;
import com.firoz.shooply.model.AddressBookModel;
import com.firoz.shooply.util.AddressBookOnClick;

import java.util.ArrayList;
import java.util.List;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.myViewHolder> {

    Context context;
    List<AddressBookModel> addressArrayList;
    AddressBookOnClick addressBookOnClick;

    public AddressBookAdapter(Context context, List<AddressBookModel> addressArrayList, AddressBookOnClick addressBookOnClick) {
        this.context = context;
        this.addressArrayList = addressArrayList;
        this.addressBookOnClick = addressBookOnClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, null, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        AddressBookModel addressBookModel = addressArrayList.get(position);
        holder.productDeliverAddress.setText(addressBookModel.getProductDeliverAddress());
        holder.userPhoneNumber.setText(addressBookModel.getUserPhoneNumber());
        holder.menu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.menu);
            //inflating menu from xml resource
            popup.inflate(R.menu.address_book_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.onEdit:
                            addressBookOnClick.onEdit(addressBookModel);
                            break;
                        case R.id.onDelete:
                            addressBookOnClick.onDelete(addressBookModel);
                            break;
                        case R.id.onSetDefault:
                            addressBookOnClick.onSetDefault(addressBookModel);
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
        return addressArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView productDeliverAddress, userPhoneNumber;

        ImageView menu;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productDeliverAddress = itemView.findViewById(R.id.productDeliverAddress);
            userPhoneNumber = itemView.findViewById(R.id.userPhoneNumber);
            menu = itemView.findViewById(R.id.menu);
        }
    }
}
