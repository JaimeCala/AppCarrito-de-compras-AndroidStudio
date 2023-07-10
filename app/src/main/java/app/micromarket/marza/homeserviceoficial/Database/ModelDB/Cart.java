package app.micromarket.marza.homeserviceoficial.Database.ModelDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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

    @ColumnInfo(name = "precio_uni")
    public  double precio_uni;

    @ColumnInfo(name = "precio_total")
    public  double precio_total;

    @ColumnInfo(name = "oferta")
    public  String oferta;

    @ColumnInfo(name = "porcentaje_des")
    public  double porcentaje_des;

    /*@ColumnInfo(name = "pedido")
    public  int pedido;*/






}
