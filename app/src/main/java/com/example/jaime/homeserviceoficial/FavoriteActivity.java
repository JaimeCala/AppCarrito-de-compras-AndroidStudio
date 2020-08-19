package com.example.jaime.homeserviceoficial;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jaime.homeserviceoficial.Adapter.FavoriteAdapter;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Favorite;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelper;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener{


    RelativeLayout rootLayout;
    RecyclerView recycler_favorito;

    CompositeDisposable compositeDisposable;
    FavoriteAdapter favoriteAdapter;
    List<Favorite> localFavorites = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle("Favoritos");
        compositeDisposable = new CompositeDisposable();

        rootLayout = findViewById(R.id.rootLayout);

        recycler_favorito = findViewById(R.id.recycler_favorito);
        recycler_favorito.setLayoutManager(new LinearLayoutManager(this));
        recycler_favorito.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_favorito);
        loadFavoritesItem();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadFavoritesItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    private void loadFavoritesItem() {

        compositeDisposable.add(Common.favoriteRepository.getFavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displalyFavoriteItem(favorites);
                    }
                }));
    }

    private void displalyFavoriteItem(List<Favorite> favorites) {
        localFavorites = favorites;
        favoriteAdapter = new FavoriteAdapter(this,favorites);
        recycler_favorito.setAdapter(favoriteAdapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof FavoriteAdapter.FavoriteViewHolder)
        {
            String nombre = localFavorites.get(viewHolder.getAdapterPosition()).nombre;
            final Favorite deleteItem = localFavorites.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            //delete item from adapter
            favoriteAdapter.removeItem(deleteIndex);

            //delete item form Room database
            Common.favoriteRepository.delete(deleteItem);
            Snackbar snackbar = Snackbar.make(rootLayout,new StringBuilder(nombre).append("Removido de la lista de favoritos").toString(),Snackbar.LENGTH_LONG);
            snackbar.setAction("CANCELAR", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteAdapter.restoreItem(deleteItem,deleteIndex);
                    Common.favoriteRepository.insertFav(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
