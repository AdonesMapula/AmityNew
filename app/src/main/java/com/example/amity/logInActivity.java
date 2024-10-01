package com.example.amity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class logInActivity extends AppCompatActivity {

    private EditText logEmailInTxt, logPassInTxt;
    private Button logInBtn, signUpBtn;
    private ImageView imageView;
    private ImageButton backButton;
    private int[] images = {R.drawable.bgimage, R.drawable.bgimage2, R.drawable.bgimage3};
    private int currentIndex = 0;
    private Handler handler = new Handler();

    private ApiService apiService;  // Retrofit API service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if session exists and is valid
        if (isLoggedIn()) {
            Intent intent = new Intent(logInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_log_in);

            // Initialize Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://olive-lapwing-255852.hostingersite.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);

            // Initialize UI components
            logEmailInTxt = findViewById(R.id.LogEmailInTxt);
            logPassInTxt = findViewById(R.id.LogPassInTxt);
            imageView = findViewById(R.id.imageView);
            logInBtn = findViewById(R.id.LogInBtn);
            signUpBtn = findViewById(R.id.SignUpBtn);
            backButton = findViewById(R.id.backBtn);

            // Start the slideshow
            startSlideshow();

            // Set up click listener for Login button
            logInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(logInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Set up click listener for Sign Up button
            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectToSignUpActivity();
                }
            });
        }
    }

    private void startSlideshow() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(images[currentIndex]);
                currentIndex = (currentIndex + 1) % images.length;
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnable);
    }

    private void attemptLogin() {
        String email = logEmailInTxt.getText().toString().trim();
        String password = logPassInTxt.getText().toString().trim();

        // Simple validation
        if (email.isEmpty()) {
            logEmailInTxt.setError("Email is required");
            logEmailInTxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            logPassInTxt.setError("Password is required");
            logPassInTxt.requestFocus();
            return;
        }

        // Make the API call using Retrofit
        Call<LoginResponse> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getStatus().equals("success")) {
                        Toast.makeText(logInActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                        saveSession(email);  // Save session details

                        // Redirect to home screen
                        Intent intent = new Intent(logInActivity.this, homePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(logInActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(logInActivity.this, "Response error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(logInActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void redirectToSignUpActivity() {
        Intent intent = new Intent(logInActivity.this, signUpActivity.class);
        startActivity(intent);
        Toast.makeText(logInActivity.this, "Redirecting to Sign Up page", Toast.LENGTH_SHORT).show();
    }

    private void saveSession(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userEmail", email);
        editor.putLong("sessionStartTime", System.currentTimeMillis());
        editor.apply();
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);
        return userEmail != null && isSessionValid();
    }

    private boolean isSessionValid() {
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        long sessionStartTime = sharedPreferences.getLong("sessionStartTime", 0);
        long currentTime = System.currentTimeMillis();
        long sessionDuration = currentTime - sessionStartTime;

        // Set session timeout duration (e.g., 30 minutes = 1800000 milliseconds)
        long timeoutDuration = 1800000;

        return sessionDuration <= timeoutDuration;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);  // Stop the handler when activity is destroyed
    }
}
