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
import com.example.dell.hardhotel.roomdatabase.FoodDatabase;


import java.util.List;


public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private List<FoodItem> foodItems;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public FoodItemAdapter(List<FoodItem> foodItems, Context context, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.foodItems = foodItems;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        final FoodItem foodItem = foodItems.get(i);

        holder.foodtitle.setText(foodItem.getItem_name());
        holder.price.setText("Price:"+foodItem.getItem_price());
        holder.rating.setText("Rating:"+foodItem.getAverage_rating());

        String images = foodItem.getImage_url();

        Glide.with(context)
                .load(images)
                .into(holder.icon);
        holder.qtyview.setText(String.valueOf(foodItem.getCount()));


        holder.plsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodItem.setCount(foodItem.getCount() + 1);
                holder.qtyview.setText(String.valueOf(foodItem.getCount()));
              FoodDatabase.getDatabase(context).foodDao().updateFoodItem(foodItem);
                 onItemClickListener.onFoodItemClick(foodItem);



            }
        });

        holder.minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodItem.getCount() > 0) {
                    foodItem.setCount(foodItem.getCount() - 1);
                    holder.qtyview.setText(String.valueOf(foodItem.getCount()));
                   FoodDatabase.getDatabase(context).foodDao().updateFoodItem(foodItem);
                     onItemClickListener.onFoodItemClick(foodItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public interface OnItemClickListener {
        void onFoodItemClick(FoodItem Item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView foodtitle, price, rating, qtyview;
        ImageView icon, plsbtn, minusbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodtitle = itemView.findViewById(R.id.foodTitle);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
            qtyview = itemView.findViewById(R.id.qtyView);
            icon = itemView.findViewById(R.id.icon);
            plsbtn = itemView.findViewById(R.id.plusBtn);
            minusbtn = itemView.findViewById(R.id.minusBtn);

        }



    }
}
