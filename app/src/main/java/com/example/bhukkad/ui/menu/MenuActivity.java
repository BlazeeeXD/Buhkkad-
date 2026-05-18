package com.example.bhukkad.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bhukkad.data.model.MenuItem;
import com.example.bhukkad.R;
import com.example.bhukkad.databinding.ActivityMenuBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    private MenuAdapter adapter;
    private List<MenuItem> allItems = new ArrayList<>();
    private String activeChip = "All";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        EditText etSearch = findViewById(R.id.etSearch);

// If launched from search button, show the search bar and focus it
        if (getIntent().getBooleanExtra("open_search", false)) {
            etSearch.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
        }

// Filter list as user types
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });





        adapter = new MenuAdapter(allItems);
        binding.rvMenuItems.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvMenuItems.setAdapter(adapter);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnCart.setOnClickListener(v ->
                startActivity(new Intent(MenuActivity.this, com.example.bhukkad.ui.ordering.CartActivity.class))
        );

        binding.chipAll.setOnClickListener(v -> applyFilter("All"));
        binding.chipRecommended.setOnClickListener(v -> applyFilter("Recommended"));
        binding.chipVeg.setOnClickListener(v -> applyFilter("Veg"));

        String category = getIntent().getStringExtra("category");
        if (category != null && !category.equals("All")) {
            activeChip = category;
        }

        // Fetch Live Data from Firestore
        fetchLiveMenu();
    }



    private void fetchLiveMenu() {
        // Real-time listener: Only get items that are available
        db.collection("menu_items")
                .whereEqualTo("available", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("MenuActivity", "Listen failed.", error);
                        return;
                    }

                    allItems.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        MenuItem item = doc.toObject(MenuItem.class);
                        allItems.add(item);
                    }
                    // Re-apply the current filter whenever data updates
                    applyFilter(activeChip);
                });
    }

    private void applyFilter(String filter) {
        activeChip = filter;
        resetChips();

        switch (filter) {
            case "All": setChipSelected(binding.chipAll); break;
            case "Recommended": setChipSelected(binding.chipRecommended); break;
            case "Veg": setChipSelected(binding.chipVeg); break;
            default: break; // If it's a specific category from the Home screen
        }

        List<MenuItem> filtered = new ArrayList<>();
        for (MenuItem item : allItems) {
            if (filter.equals("All")) {
                filtered.add(item);
            } else if (filter.equals("Recommended")) {
                if (item.isBestseller()) filtered.add(item);
            } else if (filter.equals("Veg")) {
                if (item.isVegetarian()) filtered.add(item);
            } else {
                if (item.getCategory() != null && item.getCategory().equalsIgnoreCase(filter)) {
                    filtered.add(item);
                }
            }
        }
        adapter.updateItems(filtered);
    }

    private void filterBySearch(String query) {
        if (query.isEmpty()) {
            adapter.updateItems(allItems);
            return;
        }
        List<MenuItem> filtered = new ArrayList<>();
        for (MenuItem item : allItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateItems(filtered);
    }

    private void resetChips() {
        binding.chipAll.setBackgroundResource(R.drawable.bg_chip_unselected);
        binding.chipAll.setTextColor(0xFF5D4037);
        binding.chipRecommended.setBackgroundResource(R.drawable.bg_chip_unselected);
        binding.chipRecommended.setTextColor(0xFF5D4037);
        binding.chipVeg.setBackgroundResource(R.drawable.bg_chip_unselected);
        binding.chipVeg.setTextColor(0xFF5D4037);
    }

    private void setChipSelected(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_chip_selected);
        chip.setTextColor(0xFFFFFFFF);
    }
}