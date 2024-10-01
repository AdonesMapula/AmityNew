package com.example.amity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button logInBtn, signUpBtn;
    private ImageView imageView;
    private int[] images = {R.drawable.bgimage, R.drawable.bgimage2, R.drawable.bgimage3};
    private int currentIndex = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the layout
        setContentView(R.layout.activity_main);

        // Set up edge-to-edge window insets for immersive UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        logInBtn = findViewById(R.id.LogInBtn);
        signUpBtn = findViewById(R.id.SignUpBtn);
        imageView = findViewById(R.id.imageView);

        // Start the slideshow
        startSlideshow();

        // Set up login button click listener
        logInBtn.setOnClickListener(view ->{
                Intent loginIntent = new Intent(MainActivity.this, logInActivity.class);
                startActivity(loginIntent);
        });

        // Set up sign-up button click listener
        signUpBtn.setOnClickListener(view -> {
            Intent signupIntent = new Intent(MainActivity.this, signUpActivity.class);
            startActivity(signupIntent);
        });
    }

    // Method to start the background image slideshow
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks when activity is destroyed
        handler.removeCallbacksAndMessages(null);
    }
}
