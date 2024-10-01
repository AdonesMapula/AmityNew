package com.example.amity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class GraphPagerAdapter extends FragmentStateAdapter {

    public GraphPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DailyGraphFragment();
            case 1:
                return new WeeklyGraphFragment();
            case 2:
                return new MonthlyGraphFragment();
            default:
                return new DailyGraphFragment(); // Default fragment
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Daily, Weekly, Monthly
    }
}
