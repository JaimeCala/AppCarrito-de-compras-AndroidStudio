package com.example.jaime.homeserviceoficial.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Cart")
public class Cart {
    @NonNull
    @PrimaryKey(autoGenerate = true)


    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "nombre")
    public  String nombre;

    @ColumnInfo(name = "imgnombre")
    public  String imgnombre;

    @ColumnInfo(name = "precio")
    public  double precio;       //modificar a double



    @ColumnInfo(name = "amount")
    public  int amount;

    @ColumnInfo(name = "cantidad")
    public  int cantidad;

    @ColumnInfo(name = "producto")
    public  int producto;

    /*@ColumnInfo(name = "pedido")
    public  int pedido;*/






}
