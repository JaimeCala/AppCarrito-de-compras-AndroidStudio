package com.example.jaime.homeserviceoficial;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
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
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelper;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelperListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Productos agregados");

        // Toast.makeText(this, "Se necesita Activar GPS sino lo tiene activado", Toast.LENGTH_SHORT).show();

        compositeDisposable = new CompositeDisposable();

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
        btn_cart_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarOrden();
            }
        });

        //INICIA LOS LAYOUT
        rootLayout = findViewById(R.id.rootLayout);

        //metodo para correr elementos de carrito
        loadCartElemntos();


    }


    public void enviarOrden() {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                       /* ActivityCompat.requestPermissions(CartActivity.this,new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                        },1000);*/
            } else {
                location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);

            }
        } else {
            location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
        }
        location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);



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


        //mostrar dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Su Pedido");
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




                    final String latitude = String.valueOf(location.getLatitude());
                    final String longitude = String.valueOf(location.getLongitude());

                    Toast.makeText(CartActivity.this, "latitud"+latitude, Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartActivity.this, "longitud"+longitude, Toast.LENGTH_SHORT).show();

                    //insertamos fecha y hora del dispositivo
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    final String currentDate= simpleDateFormat.format(new Date());
                    final String currentTime = simpleDateFormat2.format(new Date());

                    //declarar estado
                    final String estado = "proceso";
                    final int idcliente=0;
                    // final String latitude ="",  longitude="";
                    // final String currentDate="",currentTime="";
                    //final int  pedido = 6;
                    //submit order
                    compositeDisposable.add(
                            Common.cartRepository.getCartItem()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept( final List<Cart> carts) throws Exception {
                                            if(!TextUtils.isEmpty(orderAddress)) {


                                                //=================================================================
                                                 mService.createCliente(Common.currentUser.getIdusuario()).enqueue(new Callback<Cliente>() {
                                                    @Override
                                                    public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                                                        Common.currentCliente = response.body();



                                                        if(Common.currentCliente.getIdcliente()>0)
                                                        {
                                                            Toast.makeText(CartActivity.this, "el ID cliente ========>>"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();

                                                            //==================================================================================================================
                                                            //guardar pedido al servidor
                                                            //GuardarPedidoToServer(latitude, longitude,currentDate,currentTime,estado, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.sumPrecio());

                                                            Call<Pedido> pedidoCall = mService.createPedido(latitude, longitude,currentDate,currentTime,estado, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.sumPrecio());
                                                            pedidoCall.enqueue(new Callback<Pedido>() {
                                                                @Override
                                                                public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                                                                    Common.currentPedido = response.body();
                                                                    Toast.makeText(CartActivity.this, "insertado pedido========>>", Toast.LENGTH_SHORT).show();

                                                                    if(Common.currentPedido.getIdpedido()>0)
                                                                    {
                                                                        Toast.makeText(CartActivity.this, "el ID pedido========>>"+Common.currentPedido.getIdpedido(), Toast.LENGTH_SHORT).show();

                                                                        //===============================================================================================
                                                                        //guardar datos de pedido producto
                                                                        //List<Cart> carts1;

                                                                                /*String cart = new Gson().toJson( cartList);
                                                                                Log.d("JCM_DEBUG",cart);


                                                                                Gson gson = new Gson();


                                                                                Cart[] jsonob = gson.fromJson(cart,Cart[].class);

                                                                                //int idpedidos= Common.currentPedido.getIdpedido();


                                                                                mService.createPedidoProducto(jsonob).enqueue(new Callback<PedidoProducto>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<PedidoProducto> call, Response<PedidoProducto> response) {

                                                                                        Toast.makeText(CartActivity.this, "Pedido enviado", Toast.LENGTH_SHORT).show();


                                                                                        //limpiar cart
                                                                                        Common.cartRepository.emptyCart();

                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<PedidoProducto> call, Throwable t) {

                                                                                        Log.e("ERROR",t.getMessage());

                                                                                    }
                                                                                });*/

                                                                        //===============================================================================================

                                                                    }else{
                                                                        Toast.makeText(CartActivity.this, "Error al obtener ID pedido"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<Pedido> call, Throwable t) {
                                                                    Log.e("ERROR",t.getMessage());
                                                                    //Toast.makeText(CartActivity.this, "fallloooo la respuesta========>>", Toast.LENGTH_SHORT).show();
                                                                    GuardarPedidoProductoToServer(carts);

                                                                }
                                                            });



                                                            //===================================================================================================================

                                                        }else{
                                                            Toast.makeText(CartActivity.this, "no hay nada en id cliente", Toast.LENGTH_SHORT).show();

                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<Cliente> call, Throwable t) {

                                                    }
                                                });
                                                //=================================================================

                                                //GuardarClienteToServer(Common.currentUser.getIdusuario());
                                                //==//no//==GuardarPedidoToServer(latitude, longitude,currentDate,currentTime,estado, Common.currentCliente.getIdcliente(), orderComment, orderAddress, Common.cartRepository.sumPrecio());
                                                //GuardarPedidoToServer(latitude, longitude,currentDate,currentTime,estado, Common.currentUser.getIdusuario(), orderComment, orderAddress, Common.cartRepository.sumPrecio());

                                                //GuardarPedidoProductoToServer(carts);
                                                //Common.cartRepository.emptyCart();====>>>>causa duplicidad en insertar datos

                                            }else{

                                                Toast.makeText(CartActivity.this, "Su orden no puede ser null", Toast.LENGTH_SHORT).show();
                                            }

                                            //Common.cartRepository.emptyCart();

                                        }
                                    })

                    );


                }else {
                    Toast.makeText(CartActivity.this, "No tiene acceso a datos de  internet", Toast.LENGTH_SHORT).show();
                }




            }
        });

        AlertDialog dialog =  builder.show();

        if(!gpsEnabled || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
           // AlertDialog dialog = builder.create();

            (dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        }




    }

    /*private void GuardarClienteToServer(int idusuario) {

        mService.createCliente(idusuario).enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                Common.currentCliente = response.body();



                if(Common.currentCliente.getIdcliente()>0)
                {
                    Toast.makeText(CartActivity.this, "el ID cliente ========>>"+Common.currentCliente.getIdcliente(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CartActivity.this, "no hay nada en id cliente", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {

            }
        });
    }*/

    /*private void GuardarPedidoToServer(String latitude, String longitude, String currentDate, String currentTime, String estado, int idcliente, String orderComment, String orderAddress, double precio) {



        mService.createPedido(latitude, longitude,currentDate,currentTime,estado, idcliente, orderComment, orderAddress, precio)
                .enqueue(new Callback<Pedido>() {
                    @Override
                    public void onResponse(Call<Pedido> call, Response<Pedido> response) {

                        Common.currentPedido = response.body();

                        Toast.makeText(CartActivity.this, "Se inserto el id usuario", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Pedido> call, Throwable t) {

                        Log.e("ERROR",t.getMessage());

                    }
                });


    }*/

    private void GuardarPedidoProductoToServer(List<Cart> carts) {


        String cart = new Gson().toJson(carts);
        Log.d("JCM_DEBUG",cart);


        Gson gson = new Gson();


        Cart[] jsonob = gson.fromJson(cart,Cart[].class);

        //int idpedidos= Common.currentPedido.getIdpedido();


       mService.createPedidoProducto(jsonob).enqueue(new Callback<List<PedidoProducto>>() {
           @Override
           public void onResponse(Call<List<PedidoProducto>> call, Response<List<PedidoProducto>> response) {

               Toast.makeText(CartActivity.this, "Pedido enviado", Toast.LENGTH_SHORT).show();
               Common.currentPedidoProducto= response.body();


               //limpiar cart
               //Common.cartRepository.emptyCart(); si se ejecuta duplica registro en la base de datos

           }

           @Override
           public void onFailure(Call<List<PedidoProducto>> call, Throwable t) {

               Log.e("ERROR",t.getMessage());
               //Common.cartRepository.emptyCart();

           }
       });


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
        cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    //ctrl +o

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }




    @Override
    protected void onResume() {
        super.onResume();
        if(Common.cartRepository.sumPrecio()==0)
        {
            btn_cart_pedido.setEnabled(false);
        }
        loadCartElemntos();

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
}
