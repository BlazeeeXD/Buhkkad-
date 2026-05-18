package com.example.bhukkad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.data.model.MenuItem;
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffScreen9Activity extends AppCompatActivity {

    private MenuItem itemToEdit;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen9);

        db = FirebaseFirestore.getInstance();

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        EditText etName = findViewById(R.id.etItemName);
        EditText etPrice = findViewById(R.id.etPrice);

        if (getIntent() != null && getIntent().hasExtra("menu_item")) {
            itemToEdit = (MenuItem) getIntent().getSerializableExtra("menu_item");
            if (itemToEdit != null) {
                if (etName != null) etName.setText(itemToEdit.getName());
                if (etPrice != null) etPrice.setText(String.valueOf(itemToEdit.getPrice()));
            }
        }

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        if (btnCancel != null) btnCancel.setOnClickListener(v -> finish());

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                if (itemToEdit == null || itemToEdit.getId() == null) {
                    Toast.makeText(this, "Error: Cannot update item", Toast.LENGTH_SHORT).show();
                    return;
                }

                String newName = etName.getText().toString().trim();
                String priceStr = etPrice.getText().toString().trim();

                if (newName.isEmpty() || priceStr.isEmpty()) return;

                int newPrice = Integer.parseInt(priceStr);

                btnSave.setEnabled(false);
                btnSave.setText("Saving...");

                // Update specific fields in Firestore
                db.collection("menu_items").document(itemToEdit.getId())
                        .update("name", newName, "price", newPrice)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
                            btnSave.setEnabled(true);
                            btnSave.setText("Save changes");
                        });
            });
        }
    }
}