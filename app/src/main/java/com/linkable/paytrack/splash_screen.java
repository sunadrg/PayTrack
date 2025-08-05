package com.linkable.paytrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;



public class splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000; // 2 seconds
    private static final int MY_REQUEST_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                Boolean check = sharedPreferences.getBoolean("flag",false);
                Log.i("tagconvertstr", "[" + check + "]");

                Intent iNext;
                if(check)
                {
                    Intent i = new Intent(splash_screen.this, MainActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
                else
                {
                    Intent i = new Intent(splash_screen.this, MainActivity.class);
                    startActivity(i);
                    finishAffinity();
                }

            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                finishAffinity();
                Log.w("dashboard", "Update flow failed! Result code: " + resultCode);
            }
        }
    }
}