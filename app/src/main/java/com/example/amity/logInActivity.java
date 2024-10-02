package com.example.amity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class logInActivity extends AppCompatActivity {

    private EditText logEmailInTxt, logPassInTxt;
    private ImageView imageView;
    private final int[] images = {R.drawable.bgimage, R.drawable.bgimage2, R.drawable.bgimage3};
    private int currentIndex = 0;
    private final Handler handler = new Handler();
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLoggedIn()) {
            navigateToActivity(homePage.class);
            return;
        }
        setContentView(R.layout.activity_log_in);
        initializeUI();
        setupRetrofit();
    }

    private void initializeUI() {
        logEmailInTxt = findViewById(R.id.LogEmailInTxt);
        logPassInTxt = findViewById(R.id.LogPassInTxt);
        imageView = findViewById(R.id.imageView);
        Button logInBtn = findViewById(R.id.LogInBtn);
        ImageButton backButton = findViewById(R.id.backBtn);
        Button signUpBtn = findViewById(R.id.SignUpBtn);

        logInBtn.setOnClickListener(view -> attemptLogin());
        backButton.setOnClickListener(view -> navigateToActivity(MainActivity.class, "Redirecting to Landing Page"));
        signUpBtn.setOnClickListener(view -> navigateToActivity(signUpActivity.class, "Redirecting to Sign Up page"));

        startSlideshow();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://olive-lapwing-255852.hostingersite.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        loginManager = new LoginManager(apiService, this);
    }

    private void startSlideshow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeImage();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void fadeImage() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setImageResource(images[currentIndex]);
                currentIndex = (currentIndex + 1) % images.length;
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
                fadeIn.setDuration(1000);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    private void attemptLogin() {
        if (UserInputValidator.validateCredentials(logEmailInTxt, logPassInTxt)) {
            String email = logEmailInTxt.getText().toString().trim();
            String password = logPassInTxt.getText().toString().trim();
            loginManager.loginUser(email, password);
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        return sharedPreferences.getString("userEmail", null) != null && isSessionValid();
    }

    private boolean isSessionValid() {
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        return System.currentTimeMillis() - sharedPreferences.getLong("sessionStartTime", 0) <= 1800000; // 30 minutes
    }

    private void navigateToActivity(Class<?> activityClass, String... message) {
        if (message.length > 0) showToast(message[0]);
        startActivity(new Intent(logInActivity.this, activityClass));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(logInActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
