package com.example.dell.hardhotel;

import com.example.dell.hardhotel.Model.FoodItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodApi {

    String BASE_URL = "https://android-full-time-task.firebaseio.com/";

    @GET("data.json")
    Call<List<FoodItem>>getFoods();
}
