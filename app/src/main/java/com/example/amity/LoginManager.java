package com.example.amity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginManager {

    private final ApiService apiService;
    private final Context context;

    public LoginManager(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public void loginUser(String email, String password) {
        apiService.loginUser(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                handleLoginResponse(response, email);
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginResponse(Response<LoginResponse> response, String email) {
        if (response.isSuccessful() && response.body() != null) {
            LoginResponse loginResponse = response.body();
            Toast.makeText(context, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

            if ("success".equals(loginResponse.getStatus())) {
                saveSession(email);
                navigateToActivity(homePage.class);
            }
        } else {
            Toast.makeText(context, "Error: " + getErrorMessage(response), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSession(String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("userEmail", email)
                .putLong("sessionStartTime", System.currentTimeMillis()).apply();
    }

    private String getErrorMessage(Response<LoginResponse> response) {
        try {
            return response.errorBody() != null ? response.errorBody().string() : "Unknown error";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown error";
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }
}
