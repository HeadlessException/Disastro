package hacknsit16.disastro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by naman_000 on 09-04-16.
 */
public class Locations {

    LatLng loc;

    public Locations(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
        Location location = null;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if(location==null) {
            loc = new LatLng(0F, 0F);
        }
        else {
            loc = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    public LatLng getLocation() {
        return loc;
    }

}
