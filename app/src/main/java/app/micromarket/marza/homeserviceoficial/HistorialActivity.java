package app.micromarket.marza.homeserviceoficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import app.micromarket.marza.homeserviceoficial.Adapter.HistorialAdapter;
import app.micromarket.marza.homeserviceoficial.Model.Historial;

import app.micromarket.marza.homeserviceoficial.R;

import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.TokenManager.TokenManager;
import app.micromarket.marza.homeserviceoficial.Utils.Common;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
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
