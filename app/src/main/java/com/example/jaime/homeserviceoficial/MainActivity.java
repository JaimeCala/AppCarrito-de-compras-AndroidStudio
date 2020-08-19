package com.example.jaime.homeserviceoficial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
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
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TextView registrarse ;
    private EditText usuario, contrasena;
    private Button login;

    private MaterialEditText ci,nombre,paterno,materno,celular,direccion,ciudad,correo,passwordUser;
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

        tokenManager = new TokenManager(getApplicationContext()); //inicializamos para mandar token guardado

        usuario = (EditText) findViewById(R.id.edtUsuario);
        contrasena = (EditText) findViewById(R.id.edtContrasena);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mService = Common.getAPI();

                final String usuarioVal = usuario.getText().toString();
                final String contrasenaVal = contrasena.getText().toString();

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


                                tokenManager.createSesion(usuarioVal, jwtToken.getToken().toString());  //para mandar token guardada

                                Toast.makeText(MainActivity.this, "el token es"+jwtToken.getToken(), Toast.LENGTH_SHORT).show();

                                mService.getUserDrawerIcon("Bearer "+jwtToken.getToken(),usuarioVal).enqueue(new Callback<Users>() {
                                    @Override
                                    public void onResponse(Call<Users> call, Response<Users> response) {


                                       Users users = response.body();
                                        Toast.makeText(MainActivity.this, "PRobando resul"+users.getNombre(), Toast.LENGTH_SHORT).show();
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
                                           Toast.makeText(MainActivity.this, "Grave error ", Toast.LENGTH_SHORT).show();
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

        registrarse = (TextView) findViewById(R.id.txtRegistrarse);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mostrarRegistroDialog();

            }
        });


    }

    private void mostrarRegistroDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTRO");



        LayoutInflater inflater = this.getLayoutInflater();
        View registrouser_layout = inflater.inflate(R.layout.registrouser_layout,null);


        expedido = (TextView) findViewById(R.id.txtExpedidoUser);
        comboExpedido = (Spinner) registrouser_layout.findViewById(R.id.spinExpedidoUser);
        sexo = (TextView) findViewById(R.id.txtSexoUser);
        comboSexo = (Spinner) registrouser_layout.findViewById(R.id.spinSexoUser);
        ArrayAdapter<CharSequence> adapterExpedido = ArrayAdapter.createFromResource(this,R.array.combo_expedido, android.R.layout.simple_spinner_item);
        comboExpedido.setAdapter(adapterExpedido);
        ArrayAdapter<CharSequence> adapterSexo = ArrayAdapter.createFromResource(this,R.array.combo_sexo, android.R.layout.simple_spinner_item);
        comboSexo.setAdapter(adapterSexo);


        ci = (MaterialEditText) registrouser_layout.findViewById(R.id.edtCedulaUser);
        nombre = (MaterialEditText) registrouser_layout.findViewById(R.id.edtNombreUser);
        paterno = (MaterialEditText) registrouser_layout.findViewById(R.id.edtPaternoUser);
        materno = (MaterialEditText) registrouser_layout.findViewById(R.id.edtMaternoUser);
        celular = (MaterialEditText) registrouser_layout.findViewById(R.id.edtCelularUser);
        direccion = (MaterialEditText) registrouser_layout.findViewById(R.id.edtDireccionUser);
        ciudad = (MaterialEditText) registrouser_layout.findViewById(R.id.edtCiudaduser);
        correo = (MaterialEditText) registrouser_layout.findViewById(R.id.edtCorreoUser);
        passwordUser = (MaterialEditText) registrouser_layout.findViewById(R.id.edtPasswordUser);
        registrar = (Button) registrouser_layout.findViewById(R.id.btnRegistrarUser);


        //cerrar dialogo
        builder.setView(registrouser_layout);
       final AlertDialog dialog = builder.create();

        //evento boton registrar
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                if(TextUtils.isEmpty(passwordUser.getText().toString() ))
                {
                    Toast.makeText(MainActivity.this, "Introduzca su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }


                String ciUser = ci.getText().toString();
                String expedidoUser = comboExpedido.getSelectedItem().toString();
                String nombreUser = nombre.getText().toString();
                String paternoUser = paterno.getText().toString();
                String maternoUser = materno.getText().toString();
                String celularUser = celular.getText().toString();
                String direccionUser = direccion.getText().toString();
                String ciudadUser = ciudad.getText().toString();
                String sexoUser = comboSexo.getSelectedItem().toString();
                String correoUser = correo.getText().toString();
                String passworUser = passwordUser.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String currentDate= simpleDateFormat.format(new Date());
                String currentTime = simpleDateFormat2.format(new Date());

                //insertamos datos del usuario en la base de datos

                final AlertDialog esperarDialog = new SpotsDialog(MainActivity.this);
                esperarDialog.show();
                esperarDialog.setMessage("Por favor espere...");

                mService.createUser(ciUser,expedidoUser,nombreUser,paternoUser,maternoUser,
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
                                    });



            }
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
}
