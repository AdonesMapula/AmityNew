package com.example.amity;

import com.google.gson.annotations.SerializedName;

public class PatientResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("patient")
    private Patient patient;

    public String getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public static class Patient {
        @SerializedName("name")
        private String name;

        // Add other fields as necessary

        public String getName() {
            return name;
        }
    }
}
