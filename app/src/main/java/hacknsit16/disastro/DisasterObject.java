package hacknsit16.disastro;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by naman_000 on 10-04-16.
 */
public class DisasterObject {

    String disaster;
    LatLng location;
    Date time;

    public DisasterObject(String disaster, String latitude, String longitude, long time) {
        this.disaster = disaster;
        this.location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        this.time = new Date(time);
    }

    public String getDisaster() {
        return disaster;
    }

    public LatLng getLocation() {
        return location;
    }

    public Date getTime() {
        return time;
    }
}
