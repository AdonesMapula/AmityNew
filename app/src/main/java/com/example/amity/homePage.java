package com.example.amity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homePage extends AppCompatActivity {

    private ImageButton homeBtn, fileBtn, staffBtn;
    private Button addPatientBtn, addFilesBtn;
    private EditText ptntsNameTxt, dateCheckTxt;
    private ApiService apiService;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeViews();
        setupRetrofit();
        setupButtonListeners();
    }

    private void initializeViews() {
        homeBtn = findViewById(R.id.homeBtn);
        fileBtn = findViewById(R.id.fileBtn);
        staffBtn = findViewById(R.id.staffBtn);
        addPatientBtn = findViewById(R.id.addPatient);
        addFilesBtn = findViewById(R.id.addFiles);
        ptntsNameTxt = findViewById(R.id.ptntsNameTxt);
        dateCheckTxt = findViewById(R.id.dateCheckTxt);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://olive-lapwing-255852.hostingersite.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void setupButtonListeners() {
        addPatientBtn.setOnClickListener(v -> {
            String patientName = ptntsNameTxt.getText().toString();
            String checkupDate = dateCheckTxt.getText().toString();
            if (addPatient(patientName, checkupDate)) {
                showToast("Patient added successfully.");
            } else {
                showToast("Failed to add patient.");
            }
        });

        addFilesBtn.setOnClickListener(v -> captureImage());

        homeBtn.setOnClickListener(v -> navigateToHome());
        fileBtn.setOnClickListener(v -> navigateToFiles());
        staffBtn.setOnClickListener(v -> navigateToStaff());
    }

    private boolean addPatient(String name, String   date) {
        return true; // Placeholder
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image captured by the camera
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uploadImage(imageBitmap);
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {
        // Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Call<ImageUploadResponse> call = apiService.uploadImage(encodedImage);
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("Image uploaded successfully: " + response.body().getMessage());
                } else {
                    showToast("Upload failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                showToast("Upload error: " + t.getMessage());
                Log.e("ImageUpload", "Error uploading image: ", t);
            }
        });
    }



    private void navigateToHome() {
        // Logic to navigate to home
    }

    private void navigateToFiles() {
        Intent intent = new Intent(homePage.this, filePage.class);
        startActivity(intent);
    }

    private void navigateToStaff() {
        Intent intent = new Intent(homePage.this, staffPage.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(homePage.this, message, Toast.LENGTH_SHORT).show();
    }
}
