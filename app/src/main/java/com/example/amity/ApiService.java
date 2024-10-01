package com.example.amity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("register.php") // relative URL (base URL will be set in Retrofit instance)
    // Register user
    Call<RegisterResponse> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    // Login user
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    // Register a patient
    @FormUrlEncoded
    @POST("register_patient.php")
    Call<SimpleResponse> addPatient(
            @Field("patientName") String patientName,
            @Field("checkupDate") String checkupDate
    );

    // Search for a patient by name
    @GET("search_patient.php")
    Call<PatientResponse> searchPatient(@Query("patientName") String patientName);
}
