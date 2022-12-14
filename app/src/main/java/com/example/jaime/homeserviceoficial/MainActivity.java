package com.example.jaime.homeserviceoficial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaime.homeserviceoficial.Model.JWTToken;
import com.example.jaime.homeserviceoficial.Model.Login;
import com.example.jaime.homeserviceoficial.Model.Users;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.TokenManager.TokenManager;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable;

    private TextView registrarse ;
    private EditText usuario;
    private TextInputLayout contrasena;
    private Button login;

    private MaterialEditText ci,nombre,paterno,materno,celular,direccion,ciudad,correo;
    private TextInputLayout passwordUser;
    private TextView expedido, sexo;
    private Spinner comboExpedido, comboSexo;
    private Button registrar;

    ICarritoShopAPI mService;

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //para anular el titulo

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        tokenManager = new TokenManager(getApplicationContext()); //inicializamos para mandar token guardado

        usuario = (EditText) findViewById(R.id.edtUsuario);
        contrasena = (TextInputLayout) findViewById(R.id.edtContrasena);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //mService = Common.getAPI();

                final String usuarioVal = usuario.getText().toString();
                final String contrasenaVal = contrasena.getEditText().getText().toString();

                if(!usuarioVal.isEmpty() && !contrasenaVal.isEmpty())
                {
                    //aler

                   /* final AlertDialog esperarDialog = new SpotsDialog(MainActivity.this);
                    esperarDialog.show();
                    esperarDialog.setMessage("Por favor espere...");*/


                    //login
                    Call<JWTToken> jwtTokenCall = mService.userLogin(usuarioVal,contrasenaVal);
                    jwtTokenCall.enqueue(new Callback<JWTToken>() {
                        @Override
                        public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {

                            if(response.isSuccessful())
                            {


                                JWTToken jwtToken = response.body();


                                tokenManager.createSesion(usuarioVal, jwtToken.getToken());  //para mandar token guardada

                                //Toast.makeText(MainActivity.this, "el token es"+jwtToken.getToken(), Toast.LENGTH_SHORT).show();

                                mService.getUserDrawerIcon("Bearer "+jwtToken.getToken(),usuarioVal).enqueue(new Callback<Users>() {
                                    @Override
                                    public void onResponse(Call<Users> call, Response<Users> response) {


                                       Users users = response.body();
                                        //Toast.makeText(MainActivity.this, "PRobando resul"+users.getNombre(), Toast.LENGTH_SHORT).show();
                                       if(users.getNombre()!=null)
                                       {
                                           //esperarDialog.dismiss();
                                           //hasta mientras para asignar al currentUser
                                           Common.currentUser = response.body();
                                           //pasamos a otro actividad home
                                           startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                           finish();
                                       }
                                       else
                                       {
                                           Toast.makeText(MainActivity.this, "Error inesperado ", Toast.LENGTH_SHORT).show();
                                       }


                                    }

                                    @Override
                                    public void onFailure(Call<Users> call, Throwable t) {

                                        Toast.makeText(MainActivity.this, "Ocurrio un error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                               // startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                //finish();



                            }else {
                                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();

                            }




                        }

                        @Override
                        public void onFailure(Call<JWTToken> call, Throwable t) {

                            Toast.makeText(MainActivity.this, "Ocurrio algun error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Complete los campos vacíos", Toast.LENGTH_SHORT).show();
                }





            }
        });

        registrarse =  findViewById(R.id.txtRegistrarse);
        registrarse.setOnClickListener(res-> {

             mostrarRegistroDialog();


        });


    }

    private void mostrarRegistroDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTRO");



        LayoutInflater inflater = this.getLayoutInflater();
        View registrouser_layout = inflater.inflate(R.layout.registrouser_layout,null);


        expedido =  findViewById(R.id.txtExpedidoUser);
        comboExpedido =  registrouser_layout.findViewById(R.id.spinExpedidoUser);
        sexo =  findViewById(R.id.txtSexoUser);
        comboSexo =  registrouser_layout.findViewById(R.id.spinSexoUser);
        ArrayAdapter<CharSequence> adapterExpedido = ArrayAdapter.createFromResource(this,R.array.combo_expedido, android.R.layout.simple_spinner_item);
        comboExpedido.setAdapter(adapterExpedido);
        ArrayAdapter<CharSequence> adapterSexo = ArrayAdapter.createFromResource(this,R.array.combo_sexo, android.R.layout.simple_spinner_item);
        comboSexo.setAdapter(adapterSexo);


        ci =  registrouser_layout.findViewById(R.id.edtCedulaUser);
        nombre =  registrouser_layout.findViewById(R.id.edtNombreUser);
        paterno =  registrouser_layout.findViewById(R.id.edtPaternoUser);
        materno =  registrouser_layout.findViewById(R.id.edtMaternoUser);
        celular =  registrouser_layout.findViewById(R.id.edtCelularUser);
        direccion =  registrouser_layout.findViewById(R.id.edtDireccionUser);
        ciudad =  registrouser_layout.findViewById(R.id.edtCiudaduser);
        correo =  registrouser_layout.findViewById(R.id.edtCorreoUser);
        passwordUser =  registrouser_layout.findViewById(R.id.edtPasswordUser);
        registrar =  registrouser_layout.findViewById(R.id.btnRegistrarUser);







        //cerrar dialogo
        builder.setView(registrouser_layout);
       final AlertDialog dialog = builder.create();

        //evento boton registrar
        registrar.setOnClickListener(res-> {

                //cerrar dialogo despues del click
                dialog.dismiss();







                String seleccioneExpedido = "Seleccione";
                String seleccioneSexo = "Seleccione";

                if(TextUtils.isEmpty(ci.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su cedúla de identidad", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.equals(comboExpedido.getSelectedItem().toString() , seleccioneExpedido ))
                {
                    Toast.makeText(MainActivity.this, "Seleccione la sigla expedido", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(nombre.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(paterno.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su apellido paterno", Toast.LENGTH_SHORT).show();
                    return;
                }
//-------------------------
                /*if(TextUtils.isEmpty(materno.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su apellido materno", Toast.LENGTH_SHORT).show();
                    return;
                }*/
//---------------------------
                if(TextUtils.isEmpty(celular.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su número de celular", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(direccion.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su dirección", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(ciudad.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su ciudad de residencia", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.equals(comboSexo.getSelectedItem().toString() , seleccioneSexo ))
                {
                    Toast.makeText(MainActivity.this, "Seleccione sexo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(correo.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordUser.getEditText().getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordUser.getEditText().getText().toString().length() <8){

                    Toast.makeText(MainActivity.this, "Introduzca 8 caracteres o mas contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }


                String ciUser = ci.getText().toString();
                String expedidoUser = comboExpedido.getSelectedItem().toString();
                String nombreUser = nombre.getText().toString().toUpperCase();
                String paternoUser = paterno.getText().toString().toUpperCase();
                String maternoUser = materno.getText().toString().toUpperCase();
                String celularUser = celular.getText().toString();
                String direccionUser = direccion.getText().toString().toUpperCase();
                String ciudadUser = ciudad.getText().toString().toUpperCase();
                String sexoUser = comboSexo.getSelectedItem().toString();
                String correoUser = correo.getText().toString();
                String passworUser = passwordUser.getEditText().getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"  );
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss" , Locale.getDefault());
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
                simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
                String currentDate= simpleDateFormat.format(new Date());
                String currentTime = simpleDateFormat2.format(new Date());

                //insertamos datos del usuario en la base de datos

                final AlertDialog esperarDialog = new SpotsDialog(MainActivity.this);
                esperarDialog.show();
                esperarDialog.setMessage("Por favor espere...");
                Log.e("VERIFICANDO", correoUser);

                Toast.makeText(MainActivity.this, "VERIFICANDO NULL "+correoUser, Toast.LENGTH_SHORT).show();
                //------------------------------------prueba---------------------------------------------------


                compositeDisposable.add(
                        mService.createUser(ciUser,expedidoUser,nombreUser,paternoUser,maternoUser,
                                correoUser,celularUser,direccionUser,sexoUser,ciudadUser)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(Users ->{

                                    esperarDialog.dismiss();
                                    Users users = Users;
                                    Toast.makeText(MainActivity.this, "ENTRA  "+correoUser, Toast.LENGTH_SHORT).show();
                                    
                                    if(TextUtils.isEmpty(users.getMessage()))
                                    {
                                       //------------------------registro al login-----------------------
                                        compositeDisposable.add(
                                                mService.createLogin(users.getIdusuario(), correoUser,passworUser,currentDate,currentTime)
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribeOn(Schedulers.io())
                                                        .subscribe(Login ->{

                                                            esperarDialog.dismiss();
                                                            Login login = Login;
                                                            if(TextUtils.isEmpty(login.getMessage()))
                                                            {
                                                                Toast.makeText(MainActivity.this, "Login guardado ", Toast.LENGTH_SHORT).show();

                                                                //cambiamos de actividad
                                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                                finish();
                                                            }

                                                        },throwable -> {Toast.makeText(MainActivity.this, "[USUARIO CREADO]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();}) );



                                    }

                                },throwable -> {Toast.makeText(MainActivity.this, "[USUARIO CREADO]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();}) );




               //------------------------------------fin prueba-------------------------------------------------------

           /*     mService.createUser(ciUser,expedidoUser,nombreUser,paternoUser,maternoUser,
                                    correoUser,celularUser,direccionUser,sexoUser,ciudadUser)
                                    .enqueue(new Callback<Users>() {
                                        @Override
                                        public void onResponse(Call<Users> call, Response<Users> response) {
                                            esperarDialog.dismiss();
                                            Users users = response.body();
                                            if(TextUtils.isEmpty(users.getMessage()))
                                            {
                                                Toast.makeText(MainActivity.this, "Registro satisfactorio", Toast.LENGTH_SHORT).show();

                                                //cambiamos a home activity
                                                //startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                //finish();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Users> call, Throwable t) {
                                            esperarDialog.dismiss();

                                        }
                                    });
                mService.createLogin(correoUser,passworUser,currentDate,currentTime)
                                    .enqueue(new Callback<Login>() {
                                        @Override
                                        public void onResponse(Call<Login> call, Response<Login> response) {
                                            esperarDialog.dismiss();
                                            Login login = response.body();
                                            if(TextUtils.isEmpty(login.getMessage()))
                                            {
                                                Toast.makeText(MainActivity.this, "Login guardado ", Toast.LENGTH_SHORT).show();

                                                //cambiamos de actividad
                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                finish();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Login> call, Throwable t) {
                                            esperarDialog.dismiss();

                                        }
                                    });*/




        });




        dialog.show();
    }

    //salir del la app cuando click en buton atras
    boolean isBackButtonClicked = false;
    //ctrl +o

    @Override
    public void onBackPressed() {
        if(isBackButtonClicked){
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked= true;
        Toast.makeText(this, "Por favor click en boton atras para salir", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        isBackButtonClicked = false;
        super.onResume();
    }

   /* @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }*/
}
