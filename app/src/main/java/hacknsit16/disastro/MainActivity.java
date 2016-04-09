package hacknsit16.disastro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private TileOverlay disasterOverlay, reliefOverlay;
    final private Context context = this;
    ActionBar abar;


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
                    fbref.child("testMessage").setValue("Test data:" + new SimpleDateFormat("MMM dd ss").format(Calendar.getInstance().getTime()));
                    Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
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
                long time = (long) dataSnapshot.child("Time").getValue();
                DisasterObject obj = new DisasterObject(disaster, latitude, longitude, time);
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
        map.addMarker(new MarkerOptions().position(delhi));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(delhi, 7.0F));
    }

    public void notifyDisaster(DisasterObject obj) {
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this);
        notif.setSmallIcon(R.drawable.ic_error);
        notif.setContentTitle("Danger!");
        notif.setContentText(obj.getDisaster() + " Alert!");
        notif.setPriority(2);


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle("Disaster Alert!");
        String contentText = "A " + obj.getDisaster().toLowerCase() + " is about to strike/has struck your area! Please take measures to protect yourself";
        style.bigText(contentText);
        notif.setStyle(style);

        Intent intent = new Intent(this, ImageInsructions.class);
        intent.putExtra("disaster", obj.getDisaster());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ImageInsructions.class);
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
}
