package com.example.jaime.homeserviceoficial.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class},version = 1)
public abstract class JCMRoomCartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();
    private static JCMRoomCartDatabase instance;

    public static JCMRoomCartDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = Room.databaseBuilder(context, JCMRoomCartDatabase.class,"CarritoShopDB")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
