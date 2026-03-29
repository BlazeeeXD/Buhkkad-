package com.example.bhukkad;

import android.content.Intent; // IMPORTANT: Add this import
import android.os.Bundle;
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

        binding = ActivityMenuManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        setupRecyclerView();
        setupClickListeners();

        viewModel.loadMenu();
    }

    private void setupRecyclerView() {
        binding.rvMenuItems.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getMenuItems().observe(this, items -> {
            adapter = new MenuAdapter(items);
            binding.rvMenuItems.setAdapter(adapter);
        });
    }

    private void setupClickListeners() {
        // Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        // UPDATE: This now actually opens the AddItemActivity
        binding.btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MenuManagementActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    // PRO-TIP: Refresh the list when coming back from the Add Item screen
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadMenu();
        }
    }
}