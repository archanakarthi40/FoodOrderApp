package com.example.dell.hardhotel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dell.hardhotel.Model.FoodItem;
import com.example.dell.hardhotel.R;
import com.example.dell.hardhotel.adapter.CartItemAdapter;
import com.example.dell.hardhotel.adapter.FoodItemAdapter;
import com.example.dell.hardhotel.roomdatabase.FoodDatabase;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.viewCartItem {

    RecyclerView recyclerView;
     CartItemAdapter cartItemAdapter;
    FoodDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db= FoodDatabase.getDatabase(CartActivity.this);
        getCartItem();

        }

        private void getCartItem(){
            List<FoodItem> foodItems =db.foodDao().getFoodItemsInCart();
            cartItemAdapter = new CartItemAdapter(foodItems,CartActivity.this);
            recyclerView.setAdapter(cartItemAdapter);





        }
}
