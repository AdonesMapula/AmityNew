package com.example.amity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class staffPage extends AppCompatActivity {

    private Button addFiles2;
    private RecyclerView staffRecyclerView;
    private StaffAdapter staffAdapter;
    private List<String> staffList; // Replace String with your staff data model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_page); // Adjust this to your actual layout name

        addFiles2 = findViewById(R.id.addFiles2);
        staffRecyclerView = findViewById(R.id.staffRecyclerView);

        // Initialize staff list and adapter
        staffList = new ArrayList<>();
        staffAdapter = new StaffAdapter(staffList);
        staffRecyclerView.setAdapter(staffAdapter);
        staffRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add some sample data (you can replace this with actual data)
        loadStaffData();

        // Log out button click
        addFiles2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    // Method to log out the user
    private void logOut() {
        // Clear any user session data here if necessary
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to login page or main activity
        Intent intent = new Intent(staffPage.this, logInActivity.class); // Change LoginActivity to your actual login activity
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Method to load sample staff data
    private void loadStaffData() {
        // Add sample data to the staff list (replace with your data source)
        staffList.add("Dr. John Doe");
        staffList.add("Nurse Jane Smith");
        staffList.add("Dr. Emily Johnson");
        staffAdapter.notifyDataSetChanged();
    }
}
