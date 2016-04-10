package hacknsit16.disastro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    final private int DISASTER = 1;
    final private int RELIEF = 2;
    private int mode = DISASTER;

    private GoogleMap map;
    private TileOverlay disasterOverlay, reliefOverlay;
    final private Context context = this;
    ActionBar abar;

    ArrayList<Marker> markers = new ArrayList<>();
    ArrayList<Circle> circles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        abar = getSupportActionBar();

        abar.setTitle("Disaster Map");

        final Firebase fbref = new Firebase("https://glowing-inferno-197.firebaseio.com/");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeMode(1);
                }
            });
        }

        Firebase disasterRef = fbref.child("Disaster");

        disasterRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prev) {
                String disaster = (String) dataSnapshot.child("Disaster").getValue();
                String latitude = (String) dataSnapshot.child("Latitude").getValue();
                String longitude = (String) dataSnapshot.child("Longitude").getValue();
                String radius = (String) dataSnapshot.child("Radius").getValue();
                long time = (long) dataSnapshot.child("Time").getValue();
                DisasterObject obj;
                try {
                    obj = new DisasterObject(disaster, latitude, longitude, radius, time);
                } catch (Exception e) {
                    return;
                }
                Locations l = new Locations(MainActivity.this);
                LatLng location = l.getLocation();
                Location l1, l2;
                l1 = new Location("A");
                l2 = new Location("B");
                l1.setLatitude(location.latitude);
                l1.setLongitude(location.longitude);
                l2.setLatitude(obj.getLocation().latitude);
                l2.setLongitude(obj.getLocation().longitude);
                float distance = l1.distanceTo(l2);
                Log.d("HELLO", Float.toString(distance));
                distance /= 1000;
                if(((long) distance) < obj.getRadius())
                    notifyDisaster(obj);
                Log.d("HELLO", obj.getDisaster());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prev) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("HELLO", firebaseError.getMessage());
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        // Add a marker in Delhi and move the camera
        LatLng delhi = new LatLng(28.6139, 77.2090);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(delhi, 5.0F));

        changeMode(0);
    }

    public void notifyDisaster(DisasterObject obj) {
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this);
        notif.setSmallIcon(R.drawable.ic_error);
        notif.setContentTitle("Danger!");
        notif.setContentText(obj.getDisaster() + " Alert!");
        notif.setPriority(2);
        notif.setAutoCancel(true);


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle("Disaster Alert!");
        String contentText = "A " + obj.getDisaster().toLowerCase() + " is about to strike/has struck your area! Please take measures to protect yourself";
        style.bigText(contentText);
        notif.setStyle(style);

        Intent intent = new Intent(this, ImageInstructions.class);
        intent.putExtra("disaster", obj.getDisaster());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ImageInstructions.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.setContentIntent(pendingIntent);

        Notification notification = notif.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(999, notification);
    }

    private void changeMode(int x) {
        if(x==1) {
            if(mode == DISASTER) {
                mode = RELIEF;
                abar.setTitle("Relief Shelters");
            }
            else {
                mode = DISASTER;
                abar.setTitle("Disaster Map");
            }
        }

        for(Marker marker: markers) {
            marker.remove();
        }

        markers.clear();

        for(Circle circle: circles) {
            circle.remove();
        }

        circles.clear();

        Firebase fbref = new Firebase("https://glowing-inferno-197.firebaseio.com/");
        if (mode == DISASTER) {
            fbref = fbref.child("Disaster");


            Query query = fbref.orderByKey();

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String disaster = (String) dataSnapshot.child("Disaster").getValue();
                    String latitude = (String) dataSnapshot.child("Latitude").getValue();
                    String longitude = (String) dataSnapshot.child("Longitude").getValue();
                    String radius = (String) dataSnapshot.child("Radius").getValue();
                    long time = (long) dataSnapshot.child("Time").getValue();
                    DisasterObject obj = new DisasterObject(disaster, latitude, longitude, radius, time);

                    if (map != null) {
                        Marker m = map.addMarker(new MarkerOptions().position(obj.getLocation()));
                        m.setTitle(obj.getDisaster());
                        if (mode == DISASTER) {
                            CircleOptions circle = new CircleOptions()
                                    .center(obj.getLocation())
                                    .radius(obj.getRadius() * 1000)
                                    .strokeColor(0xff2929)
                                    .fillColor(0x99ff4747);
                            circles.add(map.addCircle(circle));
                        }
                        markers.add(m);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        else {

            LatLng location = new LatLng(27F,78F);
            Marker m = map.addMarker(new MarkerOptions().position(location));
            m.setTitle("Relief Camp");
            markers.add(m);
            location = new LatLng(26F,79F);
            m = map.addMarker(new MarkerOptions().position(location));
            m.setTitle("Relief Camp");
            markers.add(m);
            location = new LatLng(20F,74F);
            m = map.addMarker(new MarkerOptions().position(location));
            m.setTitle("Relief Camp");
            markers.add(m);
            location = new LatLng(30F,83F);
            m = map.addMarker(new MarkerOptions().position(location));
            m.setTitle("Relief Camp");
            markers.add(m);
            location = new LatLng(23F,88F);
            m = map.addMarker(new MarkerOptions().position(location));
            m.setTitle("Relief Camp");
            markers.add(m);

        }

    }
}
