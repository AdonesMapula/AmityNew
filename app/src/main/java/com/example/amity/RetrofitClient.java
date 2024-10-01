package com.example.amity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://olive-lapwing-255852.hostingersite.com/api/";
    private static Retrofit retrofitInstance;

    // Create a Gson object with lenient parsing for slightly malformed JSON
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    // Private constructor to prevent instantiation
    private RetrofitClient() {
        // Empty constructor to prevent instantiation
    }

    // Get the Retrofit instance (Singleton pattern)
    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) {  // Thread-safe singleton
                if (retrofitInstance == null) {   // Double-check locking
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }
        return retrofitInstance;
    }
}
