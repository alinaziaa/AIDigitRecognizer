
package com.example.aidigitrecognizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button btnLogin;
    private Button btnSignup;
    private TextView btnTogglePassword;
    private TextView txtForgotPassword;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect XML views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        // Show / Hide Password
        btnTogglePassword.setOnClickListener(v -> {

            int cursorPosition = passwordInput.getSelectionStart();

            if (passwordVisible) {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                btnTogglePassword.setText("SHOW");
                passwordVisible = false;

            } else {
                passwordInput.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                btnTogglePassword.setText("HIDE");
                passwordVisible = true;
            }

            passwordInput.setTypeface(
                    android.graphics.Typeface.DEFAULT
            );

            if (cursorPosition >= 0) {
                passwordInput.setSelection(
                        Math.min(
                                cursorPosition,
                                passwordInput.getText().length()
                        )
                );
            }
        });

        // Forgot Password
        txtForgotPassword.setOnClickListener(v -> {

            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Forgot Password")
                        .setMessage("Please enter your email address first.")
                        .setPositiveButton("OK", null)
                        .show();

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Invalid Email")
                        .setMessage("Please enter a valid email address.")
                        .setPositiveButton("OK", null)
                        .show();

            } else {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Password Reset")
                        .setMessage(
                                "Email: " + email
                                        + "\n\nPassword reset is not configured yet."
                                        + "\nNo reset email has been sent."
                        )
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        // Login Button
        btnLogin.setOnClickListener(v -> {

            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty()) {
                emailInput.setError("Enter your email");
                emailInput.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email");
                emailInput.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordInput.setError("Enter your password");
                passwordInput.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordInput.setError(
                        "Password must contain at least 6 characters"
                );
                passwordInput.requestFocus();
                return;
            }

            // Hide keyboard
            View currentView = getCurrentFocus();

            if (currentView != null) {
                InputMethodManager manager =
                        (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );

                if (manager != null) {
                    manager.hideSoftInputFromWindow(
                            currentView.getWindowToken(),
                            0
                    );
                }
            }

            // Demo login only; credentials are not verified
            Toast.makeText(
                    MainActivity.this,
                    "Demo login successful",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    MainActivity.this,
                    DashboardActivity.class
            );

            startActivity(intent);
        });

        // Signup Button
        btnSignup.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SignupActivity.class
            );

            startActivity(intent);
        });
    }
}
