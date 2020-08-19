package com.example.jaime.homeserviceoficial.Database.DataSource;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IfavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);


    void insertFav(Favorite...favorites);


    void delete(Favorite favorite);
}
