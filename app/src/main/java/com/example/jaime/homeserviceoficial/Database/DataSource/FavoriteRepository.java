package com.example.jaime.homeserviceoficial.Database.DataSource;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IfavoriteDataSource{

    private IfavoriteDataSource favoriteDataSource;

    public FavoriteRepository(IfavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    private static FavoriteRepository instance;
    public static FavoriteRepository getInstance(IfavoriteDataSource favoriteDataSource)
    {
        if(instance == null)
            instance = new FavoriteRepository(favoriteDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDataSource.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDataSource.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDataSource.delete(favorite);
    }
}
