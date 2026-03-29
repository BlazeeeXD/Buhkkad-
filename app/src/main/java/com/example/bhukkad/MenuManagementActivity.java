package com.example.bhukkad;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bhukkad.databinding.ActivityMenuManagementBinding;

public class MenuManagementActivity extends AppCompatActivity {

    private ActivityMenuManagementBinding binding;
    private MenuViewModel viewModel;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Initialize View Binding
        binding = ActivityMenuManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // 3. Setup UI components
        setupRecyclerView();
        setupClickListeners();

        // 4. Load initial data
        viewModel.loadMenu();
    }

    private void setupRecyclerView() {
        binding.rvMenuItems.setLayoutManager(new LinearLayoutManager(this));

        // Observe the LiveData from ViewModel
        viewModel.getMenuItems().observe(this, items -> {
            adapter = new MenuAdapter(items);
            binding.rvMenuItems.setAdapter(adapter);
        });
    }

    private void setupClickListeners() {
        // Back Button Logic
        binding.btnBack.setOnClickListener(v -> {
            // Closes this activity and returns to the previous one (Dashboard)
            finish();
        });

        // Add New Item Button Logic
        binding.btnAddItem.setOnClickListener(v -> {
            Toast.makeText(this, "Opening 'Add New Item' Screen...", Toast.LENGTH_SHORT).show();
        });
    }
}