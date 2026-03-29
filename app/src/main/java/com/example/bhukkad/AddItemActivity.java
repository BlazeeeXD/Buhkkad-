package com.example.bhukkad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bhukkad.databinding.ActivityAddItemBinding;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    private Uri selectedImageUri;

    // 1. Setup the Gallery Launcher
    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    // Show preview and hide upload icons
                    binding.ivPreview.setVisibility(View.VISIBLE);
                    binding.ivPreview.setImageURI(uri);
                    binding.ivUploadIcon.setVisibility(View.GONE);
                    binding.tvUploadLabel.setVisibility(View.GONE);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupCategorySpinner();

        // Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        // Open Gallery on Card Click
        binding.cardImageUpload.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        // Save Button with Validation
        binding.btnSaveItem.setOnClickListener(v -> validateAndSave());
    }

    private void setupCategorySpinner() {
        java.util.List<String> categories = java.util.Arrays.asList("Coffee", "Snacks", "Meals", "Desserts");

        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void validateAndSave() {
        String name = binding.etItemName.getText().toString().trim();
        String price = binding.etItemPrice.getText().toString().trim();
        String desc = binding.etDescription.getText().toString().trim();
        String category = binding.spinnerCategory.getSelectedItem().toString();

        // Check for empty fields
        if (name.isEmpty()) {
            binding.etItemName.setError("Item name is required");
            return;
        }
        if (price.isEmpty()) {
            binding.etItemPrice.setError("Price is required");
            return;
        }
        if (desc.isEmpty()) {
            binding.etDescription.setError("Description is required");
            return;
        }

        // If we get here, data is valid
        saveToData(name, price, desc, category);
    }

    private void saveToData(String name, String price, String desc, String category) {
        // Here is where you'd eventually write to SQLite/Firebase
        // For now, we just simulate success

        Toast.makeText(this, "Item added successfully!", Toast.LENGTH_LONG).show();

        // Go back to Menu Management
        finish();
    }
}