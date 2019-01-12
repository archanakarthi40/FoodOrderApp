package com.example.dell.hardhotel.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "food_table")
public class FoodItem {
    @NonNull
    @ColumnInfo(name = "item_price")
    private String item_price;
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "item_name")
    private String item_name;
    @NonNull
    @ColumnInfo(name = "image_url")
    private String image_url;
    @NonNull
    @ColumnInfo(name = "average_rating")
    private String average_rating;
    @NonNull
    @ColumnInfo(name = "count")
    private int count;

    public FoodItem(@NonNull String item_price, @NonNull String item_name, @NonNull String image_url, @NonNull String average_rating, @NonNull int count) {
        this.item_price = item_price;
        this.item_name = item_name;
        this.image_url = image_url;
        this.average_rating = average_rating;
        this.count = count;
    }



    public int getCount(){

        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public String getItem_price ()
    {
        return item_price;
    }

    public void setItem_price (String item_price)
    {
        this.item_price = item_price;
    }

    public String getItem_name ()
    {
        return item_name;
    }

    public void setItem_name (String item_name)
    {
        this.item_name = item_name;
    }

    public String getImage_url ()
    {
        return image_url;
    }

    public void setImage_url (String image_url)
    {
        this.image_url = image_url;
    }

    public String getAverage_rating ()
    {
        return average_rating;
    }

    public void setAverage_rating (String average_rating)
    {
        this.average_rating = average_rating;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [item_price = "+item_price+", item_name = "+item_name+", image_url = "+image_url+", average_rating = "+average_rating+"]";
    }

}
