package com.example.amity;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class filePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<String> patientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_file_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PatientAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch patients from database
        fetchPatientsFromDatabase();

        // Setup SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    // Method to fetch patient names from the database
    private void fetchPatientsFromDatabase() {
        // Example: Using Retrofit to fetch data from your Hostinger API
        // Replace this with your actual API call
        // This is just a mock implementation
        // You would typically use Retrofit to make a network call here
        String[] mockPatients = {"Alice Johnson", "Bob Smith", "Cathy Brown", "David Wilson", "Ella Martinez"};

        // Clear the existing list and add fetched names
        patientList.clear();
        Collections.addAll(patientList, mockPatients);

        // Sort patient names in alphabetical order
        Collections.sort(patientList);

        // Update the adapter with sorted patient names
        adapter.updatePatientList(patientList);
    }
}
