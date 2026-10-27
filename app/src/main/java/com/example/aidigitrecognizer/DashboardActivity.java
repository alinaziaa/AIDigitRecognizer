
package com.example.aidigitrecognizer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    private Button btnStartRecognition, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnStartRecognition =
                findViewById(R.id.btnStartRecognition);

        btnLogout = findViewById(R.id.btnLogout);

        btnStartRecognition.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    DigitRecognitionActivity.class
            );
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK
            );

            startActivity(intent);
            finish();
        });
    }
}
