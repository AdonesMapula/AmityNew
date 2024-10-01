package com.example.amity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        // Add logic to send a request to add a patient
        // Call the API and handle the response
        // Return true if successful, false otherwise
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
            // Handle the image capture result
            // Get image data from intent
        }
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
