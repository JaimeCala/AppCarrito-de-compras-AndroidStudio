package app.micromarket.marza.homeserviceoficial.Database.ModelDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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
