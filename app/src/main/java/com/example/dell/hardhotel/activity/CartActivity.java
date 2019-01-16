package com.example.dell.hardhotel.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.hardhotel.Model.FoodItem;
import com.example.dell.hardhotel.R;
import com.example.dell.hardhotel.adapter.CartItemAdapter;
import com.example.dell.hardhotel.adapter.FoodItemAdapter;
import com.example.dell.hardhotel.roomdatabase.FoodDao;
import com.example.dell.hardhotel.roomdatabase.FoodDatabase;


import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.viewCartItem {
    private static final String TAG = CartActivity.class.getSimpleName();
    RecyclerView recyclerView;
    CartItemAdapter cartItemAdapter;
    FoodDatabase db;

    private List<FoodItem> cartfoodItems;
    private Context context;
    TextView tv_cart_gst, tv_cart_delivery, tv_cart_total, tv_cart_discount, rupee_symbol, tv_cart_grand_total;
    EditText et_coupon;
    Button btn_apply_coupon,btn_view_coupon;
    int deliveryFee = 30;
    int netTotal;
    boolean isCouponValid = false;
    double grandTotal;
    int discountValue;
    String enteredCoupon;
    private static final String COUPON_F22LABS = "F22LABS";
    private static final String COUPON_FREEDEL = "FREEDEL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tv_cart_gst = (TextView) findViewById(R.id.tv_cart_gst);
        tv_cart_delivery = (TextView) findViewById(R.id.tv_cart_delivery);
        tv_cart_total = (TextView) findViewById(R.id.tv_cart_total);
        tv_cart_discount = (TextView) findViewById(R.id.tv_cart_discount);
        et_coupon=(EditText)findViewById(R.id.et_coupon);
        rupee_symbol = (TextView) findViewById(R.id.rupee_symbol);
        tv_cart_grand_total = (TextView) findViewById(R.id.tv_cart_grand_total);
        btn_apply_coupon = (Button) findViewById(R.id.btn_apply_coupon);
        btn_view_coupon = (Button) findViewById(R.id.btn_view_coupon);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FoodDatabase.getDatabase(CartActivity.this);
        getCartItem();


    }


    private void getCartItem() {
        List<FoodItem> foodItems = db.foodDao().getFoodItemsInCart();
        cartItemAdapter = new CartItemAdapter(foodItems, CartActivity.this);
        updateCostTotal();
        recyclerView.setAdapter(cartItemAdapter);



    }

    private void updateCostTotal() {
        List<FoodItem> foodItems = db.foodDao().getFoodItemsInCart();
        double itemTotal = 0;
        for (int i = 0; i < foodItems.size(); i++) {
            FoodItem cartItem = foodItems.get(i);
            double itemPrice = Double.valueOf(cartItem.getItem_price());
            int itemQuantity = Integer.valueOf(cartItem.getCount());
            if (itemQuantity > 0)
                itemTotal = itemTotal + (itemPrice * itemQuantity);
        }
        int gstRate = (int) itemTotal * 6 / 100;
        tv_cart_total.setText(Double.toString(itemTotal));
        tv_cart_delivery.setText(String.valueOf(String.valueOf(deliveryFee)));
        tv_cart_gst.setText(String.valueOf(gstRate));

        netTotal = (int) itemTotal + gstRate;
        if (isCouponValid) {
            grandTotal = netTotal - discountValue;
            tv_cart_discount.setText(Integer.toString(discountValue));
            if (rupee_symbol.getVisibility() == View.INVISIBLE)
                rupee_symbol.setVisibility(View.VISIBLE);
            if (enteredCoupon.equals(COUPON_FREEDEL)) {
                tv_cart_delivery.setText(getString(R.string.free_del));

            }


            else {

                grandTotal = netTotal + deliveryFee;
                tv_cart_discount.setText(getString(R.string.nill));
                if (rupee_symbol.getVisibility() == View.VISIBLE)
                    rupee_symbol.setVisibility(View.INVISIBLE);
            }
            tv_cart_grand_total.setText(Double.toString(grandTotal));
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    public void onApplyCouponClicked(View view) {
        enteredCoupon = et_coupon.getText().toString().trim();
        switch (enteredCoupon) {
            case COUPON_F22LABS:
                if (netTotal > 400) {
                    isCouponValid = true;
                    discountValue = (int) netTotal * 20 / 100;


                } else
                    Toast.makeText(this, getString(R.string.total_less), Toast.LENGTH_LONG).show();
                break;
            case COUPON_FREEDEL:
                if (netTotal > 100) {
                    isCouponValid = true;
                    discountValue = 30;

                } else
                    Toast.makeText(this, getString(R.string.total_less), Toast.LENGTH_LONG).show();
                break;
            default:
                isCouponValid = false;
                discountValue = 0;
                Toast.makeText(this, getString(R.string.invalid_coupon), Toast.LENGTH_LONG).show();
                break;
        }
       updateCostTotal();
    }
    public void onViewCouponClicked(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.coupon_layout, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final CardView cardViewF22Labs = dialogView.findViewById(R.id.coupon_f22);
        final CardView cardViewFreeDel = dialogView.findViewById(R.id.coupon_freedel);
        final Button cancelDialogButton = dialogView.findViewById(R.id.btn_coupon_close);

        cardViewF22Labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_coupon.setText(COUPON_F22LABS);
                alertDialog.dismiss();
            }
        });

        cardViewFreeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_coupon.setText(COUPON_FREEDEL);
                alertDialog.dismiss();
            }
        });

        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_coupon.setText("");
                alertDialog.dismiss();
            }
        });
    }
}
