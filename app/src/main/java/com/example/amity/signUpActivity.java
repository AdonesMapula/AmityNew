package com.example.amity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class signUpActivity extends AppCompatActivity {

    private EditText usernameReg, emailReg, passwordReg;
    private Button signUpButton;
    private ImageButton backButton;
    private ImageView imageView;

    private int[] images = {R.drawable.bgimage, R.drawable.bgimage2, R.drawable.bgimage3};
    private int currentIndex = 0;
    private android.os.Handler handler = new android.os.Handler();

    private static final String TAG = "signUpActivity"; // Log tag
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI components
        imageView = findViewById(R.id.backgroundImageView);
        usernameReg = findViewById(R.id.UserNameReg);
        emailReg = findViewById(R.id.EmailAddReg);
        passwordReg = findViewById(R.id.UserPassReg);
        signUpButton = findViewById(R.id.SignUpSubmitBtn);
        backButton = findViewById(R.id.backBtn);

        // Start the slideshow
        startSlideshow();

        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getInstance();


        // Sign Up button listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

    private void attemptRegistration() {
        final String username = usernameReg.getText().toString().trim();
        final String email = emailReg.getText().toString().trim();
        final String password = passwordReg.getText().toString().trim();

        // Input validation
        if (username.isEmpty()) {
            usernameReg.setError("Username is required");
            usernameReg.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailReg.setError("Email is required");
            emailReg.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            emailReg.setError("Enter a valid email");
            emailReg.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordReg.setError("Password is required");
            passwordReg.requestFocus();
            return;
        }

        Call<RegisterResponse> call = apiService.registerUser(username, email, password);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log the server response
                    Log.d(TAG, "Server Response: " + response.body().toString());

                    RegisterResponse registerResponse = response.body();

                    if (registerResponse.isSuccess()) {
                        Toast.makeText(signUpActivity.this, registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Handle logical failure in the response
                        Toast.makeText(signUpActivity.this, "Registration failed: " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Registration failure: " + registerResponse.getMessage());
                    }
                } else {
                    try {
                        // Log the raw error response body for debugging
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Response Error Body: " + errorBody);
                        Toast.makeText(signUpActivity.this, "Registration failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Log the failure message
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(signUpActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailPattern, email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop the handler when activity is destroyed to prevent memory leaks
    }
}