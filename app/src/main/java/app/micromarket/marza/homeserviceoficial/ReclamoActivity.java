package app.micromarket.marza.homeserviceoficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import app.micromarket.marza.homeserviceoficial.R;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.micromarket.marza.homeserviceoficial.Model.Reclamo;
import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.TokenManager.TokenManager;
import app.micromarket.marza.homeserviceoficial.Utils.Common;

import java.net.URISyntaxException;

import io.reactivex.disposables.CompositeDisposable;
import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReclamoActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable;
    private TextInputLayout comentario_reclamo;
    private Button enviar_reclamo;
    ICarritoShopAPI mService;
    private TokenManager tokenManager;

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket(Common.URL_NOTIFICACION_PEDIDO);
            }catch (URISyntaxException e){}
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);
        mSocket.connect();
        Log.d("SOCKET", "execute oncreate");

        compositeDisposable = new CompositeDisposable();
        mService = Common.getAPI();
        tokenManager = new TokenManager(getApplicationContext()); //inicializamos para mandar token guardado

        comentario_reclamo =  findViewById(R.id.txt_reclamo_id);

        enviar_reclamo = (Button) findViewById(R.id.btn_enviar_reclamo);
        enviar_reclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String comentarioVal = comentario_reclamo.getEditText().getText().toString();

                //probamos conexion de internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()){

                    if(tokenManager.verificarSesion()){

                        Log.d("token", "entra a condicional está iniciado sesión");

                        if(!comentarioVal.isEmpty() ){
                            mService.comentarioReclamo(comentarioVal,Common.currentUser.getIdusuario()).enqueue(new Callback<Reclamo>() {
                                @Override
                                public void onResponse(Call<Reclamo> call, Response<Reclamo> response) {

                                    Toast.makeText(ReclamoActivity.this, "Enviado ", Toast.LENGTH_SHORT).show();
                                    String saludo = "saludo desde aplicacion";
                                    mSocket.emit("sendMessage", saludo );

                                    comentario_reclamo.getEditText().setText("");

                                }

                                @Override
                                public void onFailure(Call<Reclamo> call, Throwable t) {

                                }
                            });

                        }else{
                            Toast.makeText(ReclamoActivity.this, "Campo vacío", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        startActivity(new Intent(ReclamoActivity.this,MainActivity.class));
                        finish();

                    }

                }else{
                    Toast.makeText(ReclamoActivity.this, "No hay datos de internet ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("sendMessage");
    }
}