package com.example.dell.hardhotel.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.hardhotel.FoodApi;
import com.example.dell.hardhotel.Model.FoodItem;
import com.example.dell.hardhotel.R;
import com.example.dell.hardhotel.adapter.FoodItemAdapter;

import com.example.dell.hardhotel.roomdatabase.FoodDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements FoodItemAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;
    FoodItemAdapter foodItemAdapter;
    TextView textCartItemCount;
    int mCartItemCount = 0;

    private int buttonCode;
    List<FoodItem> mFoodItemList;
    private static final int RADIO_RATING = 111;
    private static final int RADIO_LOW = 122;
    private static final int RADIO_HIGH = 133;

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
            case R.id.action_filter:
                showPopUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_layout, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final RadioButton radioRating = dialogView.findViewById(R.id.radio_rating);
        final RadioButton radioLow = dialogView.findViewById(R.id.radio_sort_low);
        final RadioButton radioHigh = dialogView.findViewById(R.id.radio_sort_high);
        Button applyButton = dialogView.findViewById(R.id.btn_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioRating.isChecked())
                    buttonCode = RADIO_RATING;
                else if (radioHigh.isChecked())
                    buttonCode = RADIO_HIGH;
                else if (radioLow.isChecked())
                    buttonCode = RADIO_LOW;
                Log.v(TAG, "Button Code: " + buttonCode);
                changeSort(buttonCode);
                alertDialog.dismiss();
            }
        });

        Button closeButton = (Button) dialogView.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    private void changeSort(int radioButtonCode) {
        if (mFoodItemList != null) {
            ArrayList<FoodItem> foodItemArrayList = new ArrayList<>(mFoodItemList.size());
            foodItemArrayList.addAll(mFoodItemList);
            switch (radioButtonCode) {

                case RADIO_RATING:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {

                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            double rating1=Double.parseDouble(foodItem1.getAverage_rating());
                            double rating2=Double.parseDouble(foodItem2.getAverage_rating());
                            return Double.valueOf(rating1).compareTo(Double.valueOf(rating2));
                        }
                    });
                    break;
                case RADIO_HIGH:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {
                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            return Double.valueOf(foodItem2.getAverage_rating()).compareTo(Double.valueOf(foodItem1.getItem_price()));
                        }
                    });
                    break;
                case RADIO_LOW:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {
                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            double price1=Double.parseDouble(foodItem1.getItem_price());
                            double price2=Double.parseDouble(foodItem2.getItem_price());
                            return Double.valueOf(price1).compareTo(Double.valueOf(price2));
                        }
                    });
            }
            foodItemAdapter.setFoodItems(mFoodItemList);
           // getfoods();
        }
    }

   /* public void onDoneLoadingFoodData(List<FoodItem> foodItemsList) {

        if (foodItemsList == null)
            Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_LONG).show();
        else {
            mFoodItemList = foodItemsList;
            foodItemAdapter.setFoodItems(foodItemsList);
        }
    }
*/
    private void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.INVISIBLE) {
                    textCartItemCount.setVisibility(View.INVISIBLE);
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
                mFoodItemList = response.body();
//

                foodItemAdapter = new FoodItemAdapter(mFoodItemList, MainActivity.this, MainActivity.this );

                recyclerView.setAdapter(foodItemAdapter);
                db.foodDao().insertFoodItem(mFoodItemList);
                Log.e("DBSize=",""+db.foodDao().getAllFoodItems().size());

             //  onDoneLoadingFoodData(mFoodItemList);
                }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e("throw","="+t.getMessage());
              //  onDoneLoadingFoodData(mFoodItemList);

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


