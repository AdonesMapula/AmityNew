package com.example.amity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("register.php")
    // Register user
    Call<RegisterResponse> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
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

    @Multipart
    @POST("upload/image")//change if naa nay endpoint
    Call<ImageUploadResponse> uploadImage(@Part("image") String image);
}
