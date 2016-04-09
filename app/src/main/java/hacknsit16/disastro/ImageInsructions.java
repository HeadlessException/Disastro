package hacknsit16.disastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImageInsructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_insructions);
        Intent intent=getIntent();
        String type=intent.getStringExtra("Disaster");
        String myImage=null;
        if(type=="Earthquake")
            myImage="Earthquake";
        if(type=="Cyclone")
            myImage="Cyclone";
        if(type=="Avalanche")
            myImage="Avalanche";
        if(type=="Flood")
            myImage="Flood";
        if(type=="Landslide")
            myImage="Landslide";
        if(type=="Drought")
            myImage="Draught";
        if(type=="CloudBurst")
            myImage="CloudBurst";
        if(type=="Tsunami")
            myImage="Tsunami";
        if(type=="HeatWave")
            myImage="HeatWave";
        int id = getResources().getIdentifier("hacknsit16.disastro:drawable/" + myImage, null, null);
        ImageView img= (ImageView)findViewById(R.id.image);
        img.setImageResource(id);
    }

}
