package com.example.amity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private List<String> staffList;

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public StaffViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1); // Simple TextView
        }
    }

    public StaffAdapter(List<String> staffList) {
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new StaffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        String currentItem = staffList.get(position);
        holder.textView.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }
}
