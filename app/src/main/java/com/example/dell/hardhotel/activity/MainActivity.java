package com.example.dell.hardhotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dell.hardhotel.FoodApi;
import com.example.dell.hardhotel.Model.FoodItem;
import com.example.dell.hardhotel.R;
import com.example.dell.hardhotel.adapter.FoodItemAdapter;

import com.example.dell.hardhotel.roomdatabase.FoodDatabase;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements FoodItemAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    FoodItemAdapter foodItemAdapter;
    TextView textCartItemCount;
    int mCartItemCount = 0;

    FoodDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db= FoodDatabase.getDatabase(MainActivity.this);
        getfoods();
       /* FoodItem foodItem = new FoodItem("00", "dummy", "0", "4.5", 0);
        FoodItem foodItemSandwich = new FoodItem("00", "Sandwich", "0", "4.5", 0);
        FoodDatabase db = FoodDatabase.getDatabase(this);
        db.foodDao().insertFoodItem(foodItem);
        db.foodDao().insertFoodItem(foodItemSandwich);
        Log.e("DBSize=", "" + db.foodDao().getAllFoodItems().size());
*/}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge(mCartItemCount);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.action_cart:
                Intent i= new Intent(MainActivity.this,CartActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    private void getfoods() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        FoodApi api = retrofit.create(FoodApi.class);

        Call<List<FoodItem>> call = api.getFoods();
        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                List<FoodItem> foodItem = response.body();
//

                foodItemAdapter = new FoodItemAdapter(foodItem, MainActivity.this, MainActivity.this );

                recyclerView.setAdapter(foodItemAdapter);
                db.foodDao().insertFoodItem(foodItem);
                Log.e("DBSize=",""+db.foodDao().getAllFoodItems().size());

                }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e("throw","="+t.getMessage());

            }
        });
    }


    @Override
    public void onFoodItemClick(FoodItem item) {
        db.foodDao().updateFoodItem(item);
        List<FoodItem> foodItems =db.foodDao().getFoodItemsInCart();
        Log.e("foodupdate",""+db.foodDao().getCartItemCount(String.valueOf(item.getItem_name())));
        setupBadge(foodItems.size());


    }


}


