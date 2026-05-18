package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bhukkad.data.model.MenuItem;
import com.example.bhukkad.databinding.ActivityMenuManagementBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuManagementActivity extends AppCompatActivity {

    private ActivityMenuManagementBinding binding;
    private MenuViewModel viewModel;
    private MenuAdapter adapter;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<Intent> editItemLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    viewModel.loadMenu();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        setupRecyclerView();
        setupClickListeners();

        viewModel.loadMenu();
    }

    private void setupRecyclerView() {
        binding.rvMenuItems.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getMenuItems().observe(this, items -> {

            // Implement the new interface with BOTH methods
            adapter = new MenuAdapter(items, new MenuAdapter.MenuInteractionListener() {

                @Override
                public void onEditClick(MenuItem item, int position) {
                    Intent intent = new Intent(MenuManagementActivity.this, StaffScreen9Activity.class);
                    intent.putExtra("menu_item", item);
                    editItemLauncher.launch(intent);
                }

                @Override
                public void onAvailabilityToggled(MenuItem item, boolean isAvailable) {
                    // Push the toggle directly to Firestore!
                    // Ensure your MenuItem class has a getId() method that returns the Firestore Document ID
                    if (item.getId() != null) {
                        db.collection("menu_items").document(item.getId())
                                .update("available", isAvailable)
                                .addOnSuccessListener(aVoid -> {
                                    // Optional: Show a tiny toast, or just let it be silent and seamless
                                    // Toast.makeText(MenuManagementActivity.this, "Status updated", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MenuManagementActivity.this, "Failed to sync with cloud", Toast.LENGTH_SHORT).show();
                                    // If it fails, reload the menu to snap the switch back to its real state
                                    viewModel.loadMenu();
                                });
                    }
                }
            });

            binding.rvMenuItems.setAdapter(adapter);
        });
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MenuManagementActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadMenu();
        }
    }
}