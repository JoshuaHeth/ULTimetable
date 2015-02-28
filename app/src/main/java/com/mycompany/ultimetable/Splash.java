package com.mycompany.ultimetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Loaner on 01/02/2015.
 */
public class Splash extends Activity{

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle splashScreen){
        super.onCreate(splashScreen);
        setContentView(R.layout.splash_screen);

        /* New Handler to start the Menu-Activity
        * and close this Splash-Screen after the time. */
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, Login.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
     }
}
