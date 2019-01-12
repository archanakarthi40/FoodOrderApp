package com.example.dell.hardhotel.roomdatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.dell.hardhotel.Model.FoodItem;

@Database(entities = {FoodItem.class},version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();

    private static FoodDatabase INSTANCE;

    private static final String DATABASE_NAME = "food_database";

    public static FoodDatabase getDatabase(final Context context) {


        if (INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) {
                    // Creates Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodDatabase.class,
                            DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FoodDao foodDao;

        PopulateDbAsync(FoodDatabase db) {
            foodDao = db.foodDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
       /*  FoodItem foodItem = new FoodItem("00", "dummy", "0");
           foodDao.insertFoodItem(foodItem);
          int foodItemSize = foodDao.getAllFoodItems().size();
         Log.e("FoodSize","="+foodItemSize);*/
            return null;


        }

    }
}

