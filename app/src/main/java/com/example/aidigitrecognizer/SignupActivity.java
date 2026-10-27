
package com.example.aidigitrecognizer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

    private EditText nameInput, emailInput, passwordInput;
    private Button btnSignup, btnBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        btnSignup = findViewById(R.id.btnSignup);
        btnBackLogin = findViewById(R.id.btnBackLogin);

        btnSignup.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (name.isEmpty()) {
                nameInput.setError("Enter your full name");
            } else if (email.isEmpty()) {
                emailInput.setError("Enter your email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email");
            } else if (password.length() < 6) {
                passwordInput.setError(
                        "Password must be at least 6 characters");
            } else {
                Toast.makeText(this,
                        "Demo registration successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        SignupActivity.this,
                        DashboardActivity.class
                );
                startActivity(intent);
                finish();
            }
        });

        btnBackLogin.setOnClickListener(v -> finish());
    }
}
