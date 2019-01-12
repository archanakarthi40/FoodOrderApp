package com.example.dell.hardhotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.hardhotel.Model.FoodItem;
import com.example.dell.hardhotel.R;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {
    private List<FoodItem> foodItems;
    private Context context;


    public CartItemAdapter(List<FoodItem> foodItems, Context context) {
        this.foodItems = foodItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
         CartViewHolder holder =new CartViewHolder(view);
         return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        final FoodItem foodItem = foodItems.get(i);
        String images = foodItem.getImage_url();
        int getcount=foodItem.getCount();
        String getprice=foodItem.getItem_price();
        int convertprice=Integer.parseInt(getprice);
        int total=getcount*convertprice;

        cartViewHolder.foodtitle.setText(foodItem.getItem_name());
        cartViewHolder.price.setText("Price:"+foodItem.getItem_price());
        cartViewHolder.totalPrice.setText("Quantity-"+getcount+":Total:"+String.valueOf(total));

        Glide.with(context)
                .load(images)
                .into(cartViewHolder.icon);


    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public interface viewCartItem{


    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView foodtitle, price,totalPrice;
        ImageView icon;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodtitle = itemView.findViewById(R.id.foodTitle);
            price = itemView.findViewById(R.id.price);
            icon = itemView.findViewById(R.id.icon);
            totalPrice = itemView.findViewById(R.id.totalPrice);


        }
    }



}
