package hacknsit16.disastro;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by naman_000 on 09-04-16.
 */
public class Disastro extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
