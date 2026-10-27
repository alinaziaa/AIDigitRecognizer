
package com.example.aidigitrecognizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends Activity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable openLoginScreen = () -> {
        Intent intent = new Intent(
                SplashActivity.this,
                MainActivity.class
        );
        startActivity(intent);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(openLoginScreen, 2500);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openLoginScreen);
        super.onDestroy();
    }
}
