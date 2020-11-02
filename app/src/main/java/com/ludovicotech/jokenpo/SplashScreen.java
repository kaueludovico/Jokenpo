package com.ludovicotech.jokenpo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ludovicotech.jokenpo.controller.MainActivity;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, 3000);
    }

    private void showMainActivity() {
        Intent intent = new Intent(
                SplashScreen.this, MainActivity.class
        );
        startActivity(intent);
        finish();
    }
}
