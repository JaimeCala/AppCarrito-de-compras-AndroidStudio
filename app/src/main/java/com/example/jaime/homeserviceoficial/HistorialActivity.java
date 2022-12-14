package com.example.jaime.homeserviceoficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jaime.homeserviceoficial.Adapter.CategoriaProductoAdapter;
import com.example.jaime.homeserviceoficial.Adapter.FavoriteAdapter;
import com.example.jaime.homeserviceoficial.Adapter.HistorialAdapter;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Favorite;
import com.example.jaime.homeserviceoficial.Model.Historial;
import com.example.jaime.homeserviceoficial.Model.Users;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.TokenManager.TokenManager;
import com.example.jaime.homeserviceoficial.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivity extends AppCompatActivity {


    ICarritoShopAPI mService;
    //LinearLayout rootLayout;

    List<Historial> historialList;

    RecyclerView listahistorial;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        setTitle("Historial");

        mService = Common.getAPI();
        //rootLayout = findViewById(R.id.rootLayout);

        tokenManager = new TokenManager(getApplicationContext());

        listahistorial = (RecyclerView) findViewById(R.id.recyclerview_historial_id);
        listahistorial.setLayoutManager(new LinearLayoutManager(this));
        listahistorial.setHasFixedSize(true);

        //probamos conexion de internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){

            if(tokenManager.verificarSesion()){
                loadHistorialItem(Common.currentUser.getIdusuario());
            }else{
                startActivity(new Intent(HistorialActivity.this,MainActivity.class));
                finish();

            }

        }else{
            Toast.makeText(HistorialActivity.this, "No tiene acceso a datos de  internet", Toast.LENGTH_SHORT).show();
        }



    }


    private void loadHistorialItem(int idusuario) {

       /* compositeDisposable.add(Common.currentUser.getIdusuario()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displalyFavoriteItem(favorites);
                    }
                }));*/
        Toast.makeText(HistorialActivity.this, "entra a loadhistorialitem", Toast.LENGTH_SHORT).show();
        mService.historialPedido(Common.currentUser.getIdusuario()).enqueue(new Callback<List<Historial>>() {
            @Override
            public void onResponse(Call<List<Historial>> call, Response<List<Historial>> response) {
                //Historial historial = response.body();

                Toast.makeText(HistorialActivity.this, "historial response"+response, Toast.LENGTH_SHORT).show();

                historialList = response.body();


                displalyHistorialItem(historialList);
            }

            @Override
            public void onFailure(Call<List<Historial>> call, Throwable t) {
                Toast.makeText(HistorialActivity.this, "fallo respuesta api"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void displalyHistorialItem(List<Historial> historial) {
        HistorialAdapter adapter = new HistorialAdapter(this,historial);
        listahistorial.setAdapter(adapter);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        //loadHistorialItem(int idusuario);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
