package com.example.amity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private List<String> patientList;
    private List<String> patientListFull;

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public PatientViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1); // Use a simple TextView layout
        }
    }

    public PatientAdapter(List<String> patientList) {
        this.patientList = patientList;
        this.patientListFull = new ArrayList<>(patientList); // Store full list for search
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PatientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        String currentItem = patientList.get(position);
        holder.textView.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    // Method to filter the list
    public void filter(String text) {
        patientList.clear();
        if (text.isEmpty()) {
            patientList.addAll(patientListFull);
        } else {
            for (String patient : patientListFull) {
                if (patient.toLowerCase().contains(text.toLowerCase())) {
                    patientList.add(patient);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Method to update the patient list
    public void updatePatientList(List<String> newPatientList) {
        this.patientList.clear();
        this.patientListFull.clear();
        this.patientList.addAll(newPatientList);
        this.patientListFull.addAll(newPatientList);
        notifyDataSetChanged();
    }
}
