package com.swapnil.mvvmstarterkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.swapnil.mvvmstarterkit.ui.dashboard.DashboardFragment;


public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance())
                    .commitNow();
        }
    }
}