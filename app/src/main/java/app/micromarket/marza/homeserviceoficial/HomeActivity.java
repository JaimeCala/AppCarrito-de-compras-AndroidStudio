package app.micromarket.marza.homeserviceoficial;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import app.micromarket.marza.homeserviceoficial.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import app.micromarket.marza.homeserviceoficial.Adapter.CategoriaAdapter;
import app.micromarket.marza.homeserviceoficial.Database.DataSource.CartRepository;
import app.micromarket.marza.homeserviceoficial.Database.DataSource.FavoriteRepository;
import app.micromarket.marza.homeserviceoficial.Database.Local.CartDataSource;

import app.micromarket.marza.homeserviceoficial.Database.Local.FavoriteDataSource;
import app.micromarket.marza.homeserviceoficial.Database.Local.JCMRoomCartDatabase;
import app.micromarket.marza.homeserviceoficial.Model.Banner;
import app.micromarket.marza.homeserviceoficial.Model.Categoria;
import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.TokenManager.TokenManager;
import app.micromarket.marza.homeserviceoficial.Utils.Common;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.HashMap;
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


    private TokenManager tokenManager;

    SliderLayout sliderLayout;
    String url= "api/banner/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //probamos conexion a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            setContentView(R.layout.activity_home);
            setTitle("Categorias");
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



            mService = Common.getAPI();

            sliderLayout = findViewById(R.id.slader_banner);

            lst_categoria_menu = (RecyclerView) findViewById(R.id.recyclerview_categoria_id);
            lst_categoria_menu.setLayoutManager(new GridLayoutManager(this,2));
            lst_categoria_menu.setHasFixedSize(true);

            tokenManager = new TokenManager(getApplicationContext());




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
        /*txt_nombre.setText(Common.currentUser.getNombre());
        txt_correo.setText(Common.currentUser.getEmail());*/

            //get banner
            getBannerImagen();

            //categoria menu
            getCategoriaMenu();



            //init database room
            initDB();

        }else{
            Toast.makeText(HomeActivity.this, "No tiene acceso a datos de  internet", Toast.LENGTH_SHORT).show();
        }



    }

    

    private void getBannerImagen() {
        compositeDisposable.add(mService.getBanner()
            .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImageBanner(banners);
                    }
                })
                );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImageBanner(List<Banner> banners) {
        HashMap<String,String> bannerMap = new HashMap<>();
        for(Banner item:banners)
            bannerMap.put(item.getLinkimgbanner(), Common.BASE_URL+url+ item.getNombreimgbanner());

        for(String name:bannerMap.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    //.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }
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

        if (id == R.id.nav_historial) {
            startActivity(new Intent(HomeActivity.this,HistorialActivity.class));

        } else if (id == R.id.nav_login) {

            startActivity(new Intent(HomeActivity.this,MainActivity.class));

        } else if (id == R.id.nav_favorite) {

            startActivity(new Intent(HomeActivity.this,FavoriteActivity.class));

        } else if (id == R.id.nav_oferta) {

            startActivity(new Intent(HomeActivity.this,Oferta_Activity.class));

        } else if (id == R.id.nav_reclamo) {

            startActivity(new Intent(HomeActivity.this,ReclamoActivity.class));

        } else if (id == R.id.nav_salir_sesion) {

            if(tokenManager.verificarSesion()){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Salir de la aplicación");
                builder.setMessage("Estas seguro de salir de la aplicación ?");

                builder.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        tokenManager.cerrarSesion();

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

            }else{
                Toast.makeText(this, "Inicie sesion o regístrese por favor!", Toast.LENGTH_SHORT).show();

            }



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
