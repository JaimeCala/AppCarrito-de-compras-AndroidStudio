package com.example.jaime.homeserviceoficial;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.jaime.homeserviceoficial.Adapter.CartAdapter;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Model.Cliente;
import com.example.jaime.homeserviceoficial.Model.Pedid;
import com.example.jaime.homeserviceoficial.Model.Pedido;
import com.example.jaime.homeserviceoficial.Model.PedidoProducto;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.TokenManager.TokenManager;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.example.jaime.homeserviceoficial.Utils.ProgressRequestBody;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelper;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelperListener;
import com.example.jaime.homeserviceoficial.Utils.UploadCallBacks;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener, UploadCallBacks {


    RecyclerView recycler_cart;
    Button btn_cart_pedido;
    public TextView txt_cart_preciototal;
    ElegantNumberButton txt_cartContador;
    final int h = 0;

    CompositeDisposable compositeDisposable;


    List<Cart> cartList = new ArrayList<>();

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;


    ICarritoShopAPI mService;

    //inicializamos para gps
    private LocationManager ubicacion;
    Location location;

    private TokenManager tokenManager;

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket(Common.URL_NOTIFICACION_PEDIDO);
        }catch (URISyntaxException e){}
    }

    Button btnUpload;
    TextView txt_nombrePDF;
    ImageView imageViewPDF;

    Uri selectedFileUri;
    Context context;


    ProgressDialog progressDialog;

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private static final int PICK_FILE_REQUEST = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Productos agregados");

        mSocket.connect();



        // Toast.makeText(this, "Se necesita Activar GPS sino lo tiene activado", Toast.LENGTH_SHORT).show();

        compositeDisposable = new CompositeDisposable();

        tokenManager = new TokenManager(getApplicationContext());

  /*      if(tokenManager.verificarSesion()){   */

            mService = Common.getAPI();

            recycler_cart = findViewById(R.id.recycler_cart);
            recycler_cart.setLayoutManager(new LinearLayoutManager(this));
            recycler_cart.setHasFixedSize(true);

            ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

            // mostrar precio total
            txt_cart_preciototal = findViewById(R.id.txt_preciototal_cart);
            txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));


            //---------------------------------aqui va el dialog para permiso---------------//
            //comprobamos si esta activado el gps
            ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            final boolean gpsEnabled = ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER);


            if (!gpsEnabled || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("CONDICIONES DE USO");
                View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.permiso_gps_layout, null);


                builder.setView(submit_order_layout);
                builder.setNegativeButton("CANCELAR:", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (!gpsEnabled) {
                            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(settingIntent);


                        }

                        //comprobamos si está habilitado los permisos de gps segun versión mayor a api 23

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(CartActivity.this, new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                                }, 1000);


                            }

                        }

                    }
                });
                builder.show();

            }
            //----------------------------------esto es el fin------------------------------//


        //para realizar pedido

            btn_cart_pedido = findViewById(R.id.btn_cart_pedir);
            if (Common.cartRepository.sumPrecio() == 0) {
                btn_cart_pedido.setEnabled(false);
            }
            btn_cart_pedido.setOnClickListener(v-> {

                enviarOrden();
                //eliminamos productos agregados al carrito
                //Common.cartRepository.emptyCart();
                //cambiamos de actividad
                //startActivity(new Intent(CartActivity.this,HomeActivity.class));
                //finish();


            });

            //INICIA LOS LAYOUT
            rootLayout = findViewById(R.id.rootLayout);

            //metodo para correr elementos de carrito
            loadCartElemntos();

 /*       }else{
            Intent intent =  new Intent(CartActivity.this,MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();

        }
*/




    }


    public void enviarOrden() {
/*--------------------------------------------------
        //comprobamos si esta activado el gps
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Inicializamos gps para actualizar cada instante
        //Localizacion localizacion = new Localizacion();
        //localizacion.setCartActivity(CartActivity.this);

        boolean gpsEnabled = ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER);
       /* if (!gpsEnabled)
        {
            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingIntent);

             //gpsEnabled = ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }*/

        //dialog para aceptar acceso a la ubicación
        //inicializando gps y comprobando si tiene permisos

        //ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

 /*---------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                       /* ActivityCompat.requestPermissions(CartActivity.this,new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                        },1000);*/
 /*-------------------------- } else {
                location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);

            }
        } else {
            location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
        }
        location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);

----------------------------------*/

        /*if(location!=null)
        {
            final String latitude = String.valueOf(location.getLatitude());
            final String longitude = String.valueOf(location.getLongitude());

            Toast.makeText(CartActivity.this, "latitud"+latitude, Toast.LENGTH_SHORT).show();
            Toast.makeText(CartActivity.this, "longitud"+longitude, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "no hay coordenadas", Toast.LENGTH_SHORT).show();
        }*/

       /* final String latitude = String.valueOf(location.getLatitude());
        final String longitude = String.valueOf(location.getLongitude());

        Toast.makeText(CartActivity.this, "latitud"+latitude, Toast.LENGTH_SHORT).show();
        Toast.makeText(CartActivity.this, "longitud"+longitude, Toast.LENGTH_SHORT).show();*/


        //mostrar dialog envio de pedido y pago

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forma de Pago");
        builder.setTitle("Total BS."+Common.cartRepository.sumPrecio());

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

        final EditText edt_comment = submit_order_layout.findViewById(R.id.edt_comentario);
        final EditText edt_other_address = submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address = submit_order_layout.findViewById(R.id.rdi_user_address);
        final RadioButton rdi_other_address = submit_order_layout.findViewById(R.id.rdi_other_address);


        //evento

        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    edt_other_address.setEnabled(false);
            }
        });

        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    edt_other_address.setEnabled(true);

            }
        });

        btnUpload = submit_order_layout.findViewById(R.id.btn_upload);
        txt_nombrePDF = submit_order_layout.findViewById(R.id.txt_nombrePDF);
        imageViewPDF = submit_order_layout.findViewById(R.id.pdf_view);
        //event
        btnUpload.setOnClickListener((v) -> {

                chooseFile();

        });

        //---------end upload file




        builder.setView(submit_order_layout);
        builder.setNegativeButton("CANCELAR:", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {






                //probamos conexion de internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

                    if(tokenManager.verificarSesion()){
                        //proceso al enviar pedido
                        final String orderComment = edt_comment.getText().toString();
                        final String orderAddress;
                        if (rdi_user_address.isChecked()) {
                            //poner de la tabla usuario la direccion,
                            orderAddress = Common.currentUser.getDireccion();
                            //orderAddress = "mi direccion calle los pardos";
                        } else if (rdi_other_address.isChecked()) {
                            orderAddress = edt_other_address.getText().toString();
                        } else {
                            orderAddress = "";
                        }




                    /*final String latitude = String.valueOf(location.getLatitude());
                    final String longitude = String.valueOf(location.getLongitude());*/

                        final String latitude = "sinconexion";
                        final String longitude = "sinconexion";

                        //Toast.makeText(CartActivity.this, "latitud"+latitude, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CartActivity.this, "longitud"+longitude, Toast.LENGTH_SHORT).show();

                        //insertamos fecha y hora del dispositivo
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"  );
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss" , Locale.getDefault());
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
                        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
                        String currentDate= simpleDateFormat.format(new Date());
                        String currentTime = simpleDateFormat2.format(new Date());

                        //declarar estado
                        //final String estado = "proceso";
                        final int idcliente=0;





                        compositeDisposable.add(
                                Common.cartRepository.getCartItem()
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(Cart -> {

                                            if(!TextUtils.isEmpty(orderAddress)) {



                                                //=====================guardar cliente============================================
                                                compositeDisposable.add(
                                                        mService.createCliente(Common.currentUser.getIdusuario())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribeOn(Schedulers.io())
                                                                .subscribe(Cliente ->{

                                                                    Common.currentCliente = Cliente;

                                                                    if(Common.currentCliente.getIdcliente()>0 )
                                                                    {
                                                                        //Toast.makeText(CartActivity.this, "el ID cliente ========>>"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();

                                                                        //==================================================================================================================
                                                                        //upload PDF y pedido


                                                                        if(selectedFileUri != null)
                                                                        {


                                                                            File file = FileUtils.getFile(CartActivity.this, selectedFileUri);
                                                                            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);
                                                                            //ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

                                                                            //RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);
                                                                            Log.d("ARCHIVO"+requestFile,"ARCHIVO");

                                                                            final MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),requestFile);


                                                                            GuardarPedidoToServer(latitude, longitude,currentDate,currentTime, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.sumPrecio(), body);



                                                                        }else{
                                                                            Toast.makeText(CartActivity.this, "Adjuntar pdf/imagen", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                        //===================================================================================================================

                                                                    }else{
                                                                        Toast.makeText(CartActivity.this, "No hay  id cliente", Toast.LENGTH_SHORT).show();

                                                                    }



                                                                },throwable -> {Toast.makeText(CartActivity.this, "[CREAR CLIENTE]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();}) );

                                           /*      mService.createCliente(Common.currentUser.getIdusuario()).enqueue(new Callback<Cliente>() {
                                                    @Override
                                                    public void onResponse(Call<Cliente> call, Response<Cliente> response) {



                                                        Common.currentCliente = response.body();

                                                        int contadorejecucion = 0;

                                                        if(Common.currentCliente.getIdcliente()>0 && contadorejecucion ==0)
                                                        {
                                                            Toast.makeText(CartActivity.this, "el ID cliente ========>>"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();

                                                            //==================================================================================================================
                                                            //guardar pedido al servidor
                                                            GuardarPedidoToServer(latitude, longitude,currentDate,currentTime, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.getPrecioTotal());




                                                            //===================================================================================================================

                                                        }else{
                                                            Toast.makeText(CartActivity.this, "no hay nada en id cliente", Toast.LENGTH_SHORT).show();

                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<Cliente> call, Throwable t) {

                                                    }
                                                });*/
                                                //=========================fin cliente========================================


                                            }else{

                                                Toast.makeText(CartActivity.this, "Su orden no puede ser null", Toast.LENGTH_SHORT).show();
                                            }

                                            //Common.cartRepository.emptyCart();


                                        }, throwable -> {Toast.makeText(CartActivity.this, "[OBTENER TODO DE CART]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();})

                        );

                        //eliminamos productos agregados al carrito
                        //Common.cartRepository.emptyCart();
                        //cambiamos de actividad
                        //startActivity(new Intent(CartActivity.this,HomeActivity.class));
                        //finish();
                        //Common.cartRepository.emptyCart();

                    }else{
                        Intent intent =  new Intent(CartActivity.this,MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish();
                    }



                }else {
                    Toast.makeText(CartActivity.this, "No tiene acceso a datos de  internet", Toast.LENGTH_SHORT).show();
                }




            }


        });

        AlertDialog dialog =  builder.show();

        /*if(!gpsEnabled || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
           // AlertDialog dialog = builder.create();

            (dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        }*/

        //probando vaciado de cart
        //Common.cartRepository.emptyCart();
        //startActivity(new Intent(CartActivity.this,HomeActivity.class));
        //finish();


    }



    //ctrl+o
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,  "Permiso concedido", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == PICK_FILE_REQUEST)
            {
                if(data != null)
                {

                    selectedFileUri = data.getData();
                    File file = FileUtils.getFile(this, selectedFileUri);
                    //Log.d(String.valueOf(file.getName()) ,"PDF_FILE");

                    if(selectedFileUri != null && !selectedFileUri.getPath().isEmpty())
                        //imageViewPDF.setImageURI(selectedFileUri);
                        txt_nombrePDF.setText(String.valueOf(file.getName()).toString() );

                    else
                        Toast.makeText(this, "No se puede subir archivo al servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void chooseFile() {
        //upload file -----
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSION_CODE);
        }else{
            Intent intent = new Intent();
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent = Intent.createChooser(FileUtils.createGetContentIntent( ).setType("application/pdf") ,"select PDF/image");
            intent = Intent.createChooser(FileUtils.createGetContentIntent( ).setType("*/*") ,"select PDF/image");
            startActivityForResult(intent, PICK_FILE_REQUEST);

            

        }

    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }


    private void GuardarPedidoToServer(String latitude, String longitude, String currentDate, String currentTime, int idcliente, String orderComment, String orderAddress, double precio, MultipartBody.Part body) {


        compositeDisposable.add(
                mService.createPedido(latitude, longitude,currentDate,currentTime, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.sumPrecio(), body)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(Pedido ->{

                                Common.currentPedido = Pedido;
                                Toast.makeText(CartActivity.this, "insertado pedido========>>", Toast.LENGTH_SHORT).show();

                                if(Common.currentPedido.getIdpedido()>0)
                                {
                                    Toast.makeText(CartActivity.this, "el ID pedido========>>"+Common.currentPedido.getIdpedido(), Toast.LENGTH_SHORT).show();

                                    GuardarPedidoProductoToServer(Common.currentPedido.getIdpedido(), cartList);


                                }else{
                                    Toast.makeText(CartActivity.this, "Error al obtener ID pedido"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();
                                }





                        },throwable -> {Toast.makeText(CartActivity.this, "[CREAR PEDIDO]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();}) );

        //===========================insertar pedido==================================
   /*     Call<Pedido> pedidoCall = mService.createPedido(latitude, longitude,currentDate,currentTime, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.getPrecioTotal());
        pedidoCall.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                Common.currentPedido = response.body();
                Toast.makeText(CartActivity.this, "insertado pedido========>>", Toast.LENGTH_SHORT).show();

                if(Common.currentPedido.getIdpedido()>0)
                {
                    Toast.makeText(CartActivity.this, "el ID pedido========>>"+Common.currentPedido.getIdpedido(), Toast.LENGTH_SHORT).show();
                    GuardarPedidoProductoToServer(cartList);


                }else{
                    Toast.makeText(CartActivity.this, "Error al obtener ID pedido"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.e("ERROR->>",t.getMessage());
                //Toast.makeText(CartActivity.this, "fallloooo la respuesta========>>", Toast.LENGTH_SHORT).show();
                //GuardarPedidoProductoToServer(carts);

            }
        });*/
        //==================================fin insertar pedido======================================
    }


    private void GuardarPedidoProductoToServer(int idpedido, List<Cart> carts) {

        String cart = new Gson().toJson(carts);
        Log.d("JCM_DEBUG",cart);

        Gson gson = new Gson();

        Cart[] jsonob = gson.fromJson(cart,Cart[].class);

        compositeDisposable.add(
                mService.createPedidoProducto(idpedido,jsonob)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(PedidoProducto ->{
                            Common.currentPedidoProducto = PedidoProducto;
                            if(Common.currentPedidoProducto.get(0).getIdpedidoproducto()>0){
                                Toast.makeText(CartActivity.this, "Pedido enviado", Toast.LENGTH_SHORT).show();

                                // Envío de notification al admin de pedido nuevo
                                String notification_pedido = "pedido realizado";
                                mSocket.emit("sendNotification",notification_pedido);
                                //Common.cartRepository.emptyCart(carts.get(0).id);
                                startActivity(new Intent(CartActivity.this,HomeActivity.class));
                                finish();







                            }

                        },throwable -> {Toast.makeText(CartActivity.this, "[CREAR PEDIDO PRODUCTO]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();}) );



        //================================insertar pedido producto===================================
    /*    String cart = new Gson().toJson(carts);
        Log.d("JCM_DEBUG",cart);


        Gson gson = new Gson();


        Cart[] jsonob = gson.fromJson(cart,Cart[].class);

        //int idpedidos= Common.currentPedido.getIdpedido();


       mService.createPedidoProducto(jsonob).enqueue(new Callback<List<PedidoProducto>>() {
           @Override
           public void onResponse(Call<List<PedidoProducto>> call, Response<List<PedidoProducto>> response) {

               Toast.makeText(CartActivity.this, "Pedido enviado", Toast.LENGTH_SHORT).show();
               //Common.currentPedidoProducto= response.body();


               //limpiar cart
               //Common.cartRepository.emptyCart(); si se ejecuta duplica registro en la base de datos
               //eliminamos productos agregados al carrito
               //Common.cartRepository.emptyCart();
               //cambiamos de actividad
               //startActivity(new Intent(CartActivity.this,HomeActivity.class));
               //finish();
               //Common.cartRepository.emptyCart();

           }

           @Override
           public void onFailure(Call<List<PedidoProducto>> call, Throwable t) {

               Log.e("ERROR",t.getMessage());
               //Common.cartRepository.emptyCart();

           }
       });*/
        //================================fin insertar pedido producto===============================

    }






    private void loadCartElemntos() {

        compositeDisposable.add(Common.cartRepository.getCartItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartElementos(carts);
                    }
                }));
    }

    private void displayCartElementos(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String nombre = cartList.get(viewHolder.getAdapterPosition()).nombre;
            final Cart deleteItem = cartList.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            //delete item from adapter
            cartAdapter.removeItem(deleteIndex);

            //delete item form Room database
            Common.cartRepository.deleteCartItem(deleteItem);
            Snackbar snackbar = Snackbar.make(rootLayout,new StringBuilder(nombre).append("Removido de la lista ").toString(),Snackbar.LENGTH_LONG);
            txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));
            if(Common.cartRepository.sumPrecio()==0)
            {
                btn_cart_pedido.setEnabled(false);
            }
            snackbar.setAction("CANCELAR", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem,deleteIndex);
                    Common.cartRepository.insertToCart(deleteItem);
                    txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    //ctrl +o

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        mSocket.disconnect();
        mSocket.off("sentNotification");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }




    @Override
    protected void onResume() {

        if(Common.cartRepository.sumPrecio()==0)
        {
            btn_cart_pedido.setEnabled(false);
        }
        loadCartElemntos();
        super.onResume();
    }



}
