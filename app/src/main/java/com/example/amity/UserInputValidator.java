package com.example.amity;

import android.widget.EditText;

public class UserInputValidator {

    public static boolean validateCredentials(EditText emailField, EditText passwordField) {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Email is required");
            emailField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return false;
        }

        // Optionally add more validation (e.g., regex for email format)
        return true;
    }
}
