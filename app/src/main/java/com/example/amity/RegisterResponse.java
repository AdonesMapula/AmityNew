package com.example.amity;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public boolean isSuccess() {
        return data != null && data.success;
    }

    public String getMessage() {
        return data != null ? data.message : "Unknown error";
    }

    public class Data {
        boolean success;
        String message;
    }
}
