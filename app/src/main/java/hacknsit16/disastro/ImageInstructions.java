package hacknsit16.disastro;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by naman_000 on 10-04-16.
 */
public class ImageInstructions extends AppCompatActivity {

    TextToSpeech talkback;
    String instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_instructions);
        Intent intent=getIntent();
        String type=intent.getStringExtra("disaster");
        Log.d("HELLO2", type);
        if(type.contains("Earthquake"))
            instructions = "1.Stay away from glass, windows, outside doors and walls, and anything that could fall\n"+"2.DROP to the ground; take COVER by getting under a sturdy table or other piece of furniture";
        else if(type.contains("Cyclone"))
            instructions = "1.Clear your property of loose material that could blow about\n"+"2.Prepare an emergency kit containing";
        else if(type.contains("Avalanche"))
            instructions = "1.Don't hesitate: move as quickly as possible to the side of the avalanche slope.\n"+"2.If you're unable to escape the avalanche, try to grab on to a boulder or sturdy tree.";
        else if(type.contains("Flood"))
            instructions = "1.go to higher ground and avoid areas subject to flooding.\n"+"2.Turn off all utilities at the main power switch and close the main gas";
        else if(type.contains("Landslide"))
            instructions = "1.Take refuge in the part of the building opposite the landslide and take shelter under a solid piece of furniture\n";
        else if(type.contains("Drought"))
            instructions = "Hello how are you feeling today?";
        else if(type.contains("CloudBurst"))
            instructions = "Hello how are you feeling today?";
        else if(type.contains("Tsunami"))
            instructions = "Hello how are you feeling today?";
        else if(type.contains("HeatWave"))
            instructions = "Hello how are you feeling today?";


        ((TextView) findViewById(R.id.instructions)).setText(instructions);
        talkback = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS) {
                    talkback.setLanguage(Locale.ENGLISH);
                    speak();
                }
                else {
                    Log.d("TTSErr", Integer.toString(status));
                }
            }
        });
        /*
        ImageView img = (ImageView) findViewById(R.id.image);
        if(type.contains("Earthquake"))
            img.setImageResource(R.drawable.earthquake);
        else if(type.contains("Cyclone"))
            img.setImageResource(R.drawable.cyclone);
        else if(type.contains("Avalanche"))
            img.setImageResource(R.drawable.avalanche);
        else if(type.contains("Flood"))
            img.setImageResource(R.drawable.flood);
        else if(type.contains("Landslide"))
            img.setImageResource(R.drawable.landslide);
        else if(type.contains("Drought"))
            img.setImageResource(R.drawable.drought);
        else if(type.contains("CloudBurst"))
            img.setImageResource(R.drawable.cloudburst);
        else if(type.contains("Tsunami"))
            img.setImageResource(R.drawable.tsunami);
        else if(type.contains("HeatWave"))
            img.setImageResource(R.drawable.heatwave);
            */
    }

    private void speak() {
        if(talkback!=null){
            talkback.speak(instructions, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onPause() {

        if(talkback!=null){
            talkback.stop();
            talkback.shutdown();
        }

        super.onPause();
    }
}
