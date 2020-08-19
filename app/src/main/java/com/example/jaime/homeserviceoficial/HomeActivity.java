package com.example.jaime.homeserviceoficial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaime.homeserviceoficial.Adapter.CategoriaAdapter;
import com.example.jaime.homeserviceoficial.Adapter.ImgCategoriaAdapter;
import com.example.jaime.homeserviceoficial.Database.DataSource.CartRepository;
import com.example.jaime.homeserviceoficial.Database.DataSource.FavoriteRepository;
import com.example.jaime.homeserviceoficial.Database.Local.CartDataSource;

import com.example.jaime.homeserviceoficial.Database.Local.FavoriteDataSource;
import com.example.jaime.homeserviceoficial.Database.Local.JCMRoomCartDatabase;
import com.example.jaime.homeserviceoficial.Model.Categoria;
import com.example.jaime.homeserviceoficial.Model.ImgCategoria;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.TokenManager.TokenManager;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   private TextView txt_nombre, txt_correo;

   ICarritoShopAPI mService;

   RecyclerView lst_categoria_menu;

   CompositeDisposable compositeDisposable = new CompositeDisposable();

    NotificationBadge badge;
    ImageView cart_icon;

    TokenManager manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Categorias");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mService = Common.getAPI();

        lst_categoria_menu = (RecyclerView) findViewById(R.id.recyclerview_categoria_id);
        lst_categoria_menu.setLayoutManager(new GridLayoutManager(this,2));
        lst_categoria_menu.setHasFixedSize(true);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //poner datos donde icono del usuario
        View headerView = navigationView.getHeaderView(0);
        txt_nombre = (TextView) headerView.findViewById(R.id.txt_nombreHeader);
        txt_correo = (TextView) headerView.findViewById(R.id.txt_correoHeader);

        //colocamos los datos en el header
        txt_nombre.setText(Common.currentUser.getNombre());
        txt_correo.setText(Common.currentUser.getEmail());

        //categoria menu
        getCategoriaMenu();
        
        //init database room 
        initDB();

    }

    private void initDB() {
        Common.jcmRoomCartDatabase = JCMRoomCartDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.jcmRoomCartDatabase.cartDAO()));
        Common.favoriteRepository= FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.jcmRoomCartDatabase.favoriteDAO()));
    }

    private void getCategoriaMenu() {

        compositeDisposable.add(mService.nombreCategoria()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Categoria>>() {
                    @Override
                    public void accept(List<Categoria> categoriaList) throws Exception {
                        displayCategoria(categoriaList);
                    }
                }));


    }


    private void displayCategoria(List<Categoria> categoriaList) {

        CategoriaAdapter adapter = new CategoriaAdapter(this,categoriaList);
        lst_categoria_menu.setAdapter(adapter);

    }

    //salir del la app cuando click en buton atras
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(isBackButtonClicked){
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked= true;
            Toast.makeText(this, "Por favor click en boton atras para salir", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accion_bar, menu);

        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = view.findViewById(R.id.notificacion_badges);
        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class));

            }
        });

        actualizarCartCount();
        return true;
    }

    private void actualizarCartCount() {

        if(badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Common.cartRepository.countCartItems() ==0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_favorite) {

            startActivity(new Intent(HomeActivity.this,FavoriteActivity.class));

        } else if (id == R.id.nav_salir_sesion) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Salir de la aplicación");
            builder.setMessage("Estas seguro de salir de la aplicación ?");

            builder.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    manager.cerrarSesion();

                    //limpiar todas la actividad
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                }
            });
            builder.setPositiveButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //ctrl +o


    @Override
    protected void onResume() {
        super.onResume();

        actualizarCartCount();

        //para salir de la app
        isBackButtonClicked = false;
    }
}
