package app.micromarket.marza.homeserviceoficial;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class Localizacion  implements LocationListener{

   CartActivity cartActivity;

    public CartActivity getCartActivity() {
        return cartActivity;
    }

    public void setCartActivity(CartActivity cartActivity) {
        this.cartActivity = cartActivity;
    }

    @Override
    public void onLocationChanged(Location location) {

       // latitud = String.valueOf(location.getLatitude()) ;
       // longitud = String.valueOf(location.getLongitude()) ;
        location.getLatitude();
        location.getLongitude();
        this.cartActivity.enviarOrden();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
