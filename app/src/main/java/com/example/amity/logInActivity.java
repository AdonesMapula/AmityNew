package com.example.amity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLoggedIn()) {
            navigateToHome();
        } else {
            setContentView(R.layout.activity_log_in);
            initializeUI();
            setupRetrofit();
            startSlideshow();
            setupButtonListeners();
        }
    }

    private void initializeUI() {
        logEmailInTxt = findViewById(R.id.LogEmailInTxt);
        logPassInTxt = findViewById(R.id.LogPassInTxt);
        imageView = findViewById(R.id.imageView);
        logInBtn = findViewById(R.id.LogInBtn);
        signUpBtn = findViewById(R.id.SignUpBtn);
        backButton = findViewById(R.id.backBtn);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://olive-lapwing-255852.hostingersite.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void setupButtonListeners() {
        logInBtn.setOnClickListener(view -> attemptLogin());
        backButton.setOnClickListener(view -> navigateToMainActivity());
        signUpBtn.setOnClickListener(view -> navigateToSignUp());
    }

    private void startSlideshow() {
        final Runnable slideshowRunnable = new Runnable() {
            @Override
            public void run() {
                // Create a fade-out animation
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
                fadeOut.setDuration(1000); // Duration of fade out

                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Change the image after the fade out ends
                        imageView.setImageResource(images[currentIndex]);

                        // Create a fade-in animation
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
                        fadeIn.setDuration(1000); // Duration of fade in
                        fadeIn.start(); // Start fade in
                    }
                });

                fadeOut.start(); // Start fade out

                currentIndex = (currentIndex + 1) % images.length; // Update index
                handler.postDelayed(this, 5000); // Schedule the next transition
            }
        };
        handler.post(slideshowRunnable);
    }


    private void attemptLogin() {
        String email = logEmailInTxt.getText().toString().trim();
        String password = logPassInTxt.getText().toString().trim();

        if (!validateCredentials(email, password)) return;

        Call<LoginResponse> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showToast("Login failed: " + t.getMessage());
            }
        });
    }

    private boolean validateCredentials(String email, String password) {
        if (email.isEmpty()) {
            logEmailInTxt.setError("Email is required");
            logEmailInTxt.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            logPassInTxt.setError("Password is required");
            logPassInTxt.requestFocus();
            return false;
        }
        return true;
    }

    private void handleLoginResponse(Response<LoginResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            LoginResponse loginResponse = response.body();
            if ("success".equals(loginResponse.getStatus())) {
                showToast(loginResponse.getMessage());
                saveSession(logEmailInTxt.getText().toString());
                navigateToHome();
            } else {
                showToast(loginResponse.getMessage());
            }
        } else {
            showToast("Response error: " + response.message());
        }
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

        long timeoutDuration = 1800000;  // 30 minutes
        return sessionDuration <= timeoutDuration;
    }

    private void navigateToHome() {
        Intent intent = new Intent(logInActivity.this, homePage.class);
        intent.putExtra("userEmail", logEmailInTxt.getText().toString());
        startActivity(intent);
        finish();

    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(logInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(logInActivity.this, signUpActivity.class);
        startActivity(intent);
        showToast("Redirecting to Sign Up page");
    }

    private void showToast(String message) {
        Toast.makeText(logInActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);  // Stop slideshow handler when activity is destroyed
    }
}