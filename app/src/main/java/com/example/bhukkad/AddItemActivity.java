package com.example.bhukkad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.data.model.MenuItem;
import com.example.bhukkad.databinding.ActivityAddItemBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    private Uri selectedImageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    binding.ivPreview.setVisibility(View.VISIBLE);
                    binding.ivPreview.setImageURI(uri);
                    binding.ivUploadIcon.setVisibility(View.GONE);
                    binding.tvUploadLabel.setVisibility(View.GONE);

                    // Smart UX: If they pick an image, clear any URL they might have typed
                    binding.etImgUrl.setText("");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("menu_images");

        setupCategorySpinner();

        binding.btnBack.setOnClickListener(v -> finish());
        binding.cardImageUpload.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        binding.btnSaveItem.setOnClickListener(v -> validateAndSave());
    }

    private void setupCategorySpinner() {
        List<String> categories = Arrays.asList("Coffee", "Snacks", "Meals", "Desserts");
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void validateAndSave() {
        String name = binding.etItemName.getText().toString().trim();
        String priceStr = binding.etItemPrice.getText().toString().trim();
        String desc = binding.etDescription.getText().toString().trim();
        String category = binding.spinnerCategory.getSelectedItem().toString();

        // Grab the manually typed URL
        String imgUrlInput = binding.etImgUrl.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill all required text fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // THE 'OR' LOGIC: Fail only if BOTH the gallery image AND the URL field are empty
        if (selectedImageUri == null && imgUrlInput.isEmpty()) {
            Toast.makeText(this, "Please upload an image OR enter an image URL", Toast.LENGTH_LONG).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            binding.etItemPrice.setError("Invalid price");
            return;
        }

        // Lock UI while processing
        binding.btnSaveItem.setEnabled(false);

        // --- PATH A: User selected a local image from their gallery ---
        if (selectedImageUri != null) {
            binding.btnSaveItem.setText("Uploading Image...");

            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference fileRef = storageRef.child(fileName);

            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String finalImageUrl = uri.toString();
                            saveToFirestore(name, price, desc, category, finalImageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.btnSaveItem.setEnabled(true);
                        binding.btnSaveItem.setText("Add Item");
                    });

            // --- PATH B: User pasted a direct URL link ---
        } else {
            binding.btnSaveItem.setText("Saving Data...");
            // Skip storage, send the typed URL straight to the database
            saveToFirestore(name, price, desc, category, imgUrlInput);
        }
    }

    private void saveToFirestore(String name, int price, String desc, String category, String imageUrl) {
        boolean isVegetarian = binding.cbVegetarian.isChecked();
        boolean isBestseller = binding.cbBestseller.isChecked();
        boolean isAvailable = binding.cbAvailable.isChecked();

        MenuItem newItem = new MenuItem(
                name, desc, price, imageUrl, category, isAvailable, isVegetarian, isBestseller
        );

        db.collection("menu_items").add(newItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK); // Good practice to signal the previous screen to refresh
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save to database", Toast.LENGTH_SHORT).show();
                    binding.btnSaveItem.setEnabled(true);
                    binding.btnSaveItem.setText("Add Item");
                });
    }
}