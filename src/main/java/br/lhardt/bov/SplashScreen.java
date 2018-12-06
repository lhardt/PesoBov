package br.lhardt.bov;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler hnd = new Handler();
        /* Stays 2.5 seconds, then finishes the activity */
        hnd.postDelayed(new Runnable(){
            @Override
            public void run(){
                finish();
            }}, 3000
        );
    }
}
