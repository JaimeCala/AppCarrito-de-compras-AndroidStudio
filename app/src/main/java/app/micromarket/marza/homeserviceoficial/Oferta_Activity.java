package app.micromarket.marza.homeserviceoficial;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import app.micromarket.marza.homeserviceoficial.Adapter.CategoriaProductoAdapter;
import app.micromarket.marza.homeserviceoficial.Model.CategoriaProducto;

import app.micromarket.marza.homeserviceoficial.R;

import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.Utils.Common;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Oferta_Activity extends AppCompatActivity {

    ICarritoShopAPI mService;

    RecyclerView  lst_productos_oferta;
    CompositeDisposable compositeDisposable;
    List<CategoriaProducto>  categoriaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);

        mService = Common.getAPI();
        //productos en oferta
        lst_productos_oferta = findViewById(R.id.recyclerview_ofertas_id);
        lst_productos_oferta.setLayoutManager(new GridLayoutManager(this,2));
        lst_productos_oferta.setHasFixedSize(true);

        //productos en oferta
        getProductosOferta();
    }

    private void getProductosOferta() {
        /*compositeDisposable.add(mService.getProductosOferta()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CategoriaProducto>>() {
                    @Override
                    public void accept(List<CategoriaProducto> categoriaProductos) throws Exception {
                        displayProductosOferta(categoriaProductos);
                    }
                })


        );*/
        mService.getProductosOferta().enqueue(new Callback<List<CategoriaProducto>>() {
            @Override
            public void onResponse(Call<List<CategoriaProducto>> call, Response<List<CategoriaProducto>> response) {
                //Historial historial = response.body();

                //Toast.makeText(Oferta_Activity.this, "historial response"+response, Toast.LENGTH_SHORT).show();

                categoriaProductos = response.body();


               displayProductosOferta(categoriaProductos);
            }

            @Override
            public void onFailure(Call<List<CategoriaProducto>> call, Throwable t) {
                Toast.makeText(Oferta_Activity.this, "fallo respuesta api"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayProductosOferta(List<CategoriaProducto> categoriaProductos) {
        CategoriaProductoAdapter adapter = new CategoriaProductoAdapter(this,categoriaProductos);
        lst_productos_oferta.setAdapter(adapter);
    }
}