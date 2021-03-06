package com.example.jaime.homeserviceoficial.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Favorite")
public class Favorite {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "imgnombre")
    public String imgnombre;

    @ColumnInfo(name = "precio")
    public double precio;

    @ColumnInfo(name = "categoriaId")
    public int categoriaId;
}
