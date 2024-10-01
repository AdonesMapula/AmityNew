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

        // Initialize views
        homeBtn = findViewById(R.id.homeBtn);
        fileBtn = findViewById(R.id.fileBtn);
        staffBtn = findViewById(R.id.staffBtn);
        addPatientBtn = findViewById(R.id.addPatient);
        addFilesBtn = findViewById(R.id.addFiles);
        ptntsNameTxt = findViewById(R.id.ptntsNameTxt);
        dateCheckTxt = findViewById(R.id.dateCheckTxt);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://olive-lapwing-255852.hostingersite.com/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Add patient button click
        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = ptntsNameTxt.getText().toString();
                String checkupDate = dateCheckTxt.getText().toString();

                if (patientName.isEmpty() || checkupDate.isEmpty()) {
                    Toast.makeText(homePage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    addPatientToDatabase(patientName, checkupDate);
                }
            }
        });

        // Search patient button click
        addFilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = ptntsNameTxt.getText().toString();

                if (patientName.isEmpty()) {
                    Toast.makeText(homePage.this, "Please enter the patient's name", Toast.LENGTH_SHORT).show();
                } else {
                    searchPatient(patientName);
                }
            }
        });
    }

    // Add patient to database using Retrofit
    private void addPatientToDatabase(final String patientName, final String checkupDate) {
        Call<SimpleResponse> call = apiService.addPatient(patientName, checkupDate);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SimpleResponse simpleResponse = response.body();
                    if (simpleResponse.getStatus().equals("success")) {
                        Toast.makeText(homePage.this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(homePage.this, "Error: " + simpleResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(homePage.this, "Failed to add patient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(homePage.this, "Failed to connect: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Search patient using Retrofit
    private void searchPatient(final String patientName) {
        Call<PatientResponse> call = apiService.searchPatient(patientName);
        call.enqueue(new Callback<PatientResponse>() {
            @Override
            public void onResponse(Call<PatientResponse> call, Response<PatientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PatientResponse patientResponse = response.body();
                    if (patientResponse.getStatus().equals("success")) {
                        Toast.makeText(homePage.this, "Patient found: " + patientResponse.getPatient().getName(), Toast.LENGTH_SHORT).show();
                        openCamera();
                    } else {
                        Toast.makeText(homePage.this, "Patient not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(homePage.this, "Failed to find patient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PatientResponse> call, Throwable t) {
                Toast.makeText(homePage.this, "Failed to connect: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open camera to capture image
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Handle the captured image here, e.g., save it or upload it
            Toast.makeText(this, "Image captured successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
