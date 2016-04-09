package hacknsit16.disastro;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by naman_000 on 09-04-16.
 */
public class FirebaseService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Firebase fbref = new Firebase("https://glowing-inferno-197.firebaseio.com/");
        Firebase disasterRef = fbref.child("Disaster");

        disasterRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prev) {
                String desc = dataSnapshot.getKey();
                Log.d("HELLO", desc);
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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
