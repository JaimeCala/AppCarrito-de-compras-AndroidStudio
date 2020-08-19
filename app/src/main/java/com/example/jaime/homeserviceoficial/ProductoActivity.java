package com.example.jaime.homeserviceoficial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.jaime.homeserviceoficial.Adapter.CategoriaProductoAdapter;
import com.example.jaime.homeserviceoficial.Model.CategoriaProducto;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductoActivity extends AppCompatActivity {


    ICarritoShopAPI mService;

    RecyclerView lst_producto_menu;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        setTitle("Productos");

        mService = Common.getAPI();

        lst_producto_menu = (RecyclerView) findViewById(R.id.recycler_todoproductos_id);
        lst_producto_menu.setLayoutManager(new GridLayoutManager(this,2));
        lst_producto_menu.setHasFixedSize(true);

        loadListProducto(Common.currentCategory.getIdcategoria());

    }

    private void loadListProducto(int idcategoria) {

        compositeDisposable.add(mService.getProductosCate(idcategoria)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<CategoriaProducto>>() {
                        @Override
                        public void accept(List<CategoriaProducto> categoriaProductos) throws Exception {

                            displayProductoList(categoriaProductos);

                        }
                    }));
    }

    private void displayProductoList(List<CategoriaProducto> categoriaProductos) {
        CategoriaProductoAdapter adapter = new CategoriaProductoAdapter(this,categoriaProductos);
        lst_producto_menu.setAdapter(adapter);
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}
