package com.example.bhukkad.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.ui.account.ProfileActivity;
import com.example.bhukkad.ui.menu.MenuActivity;
import com.example.bhukkad.databinding.ActivityHomeBinding;
import com.example.bhukkad.ui.ordering.CartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        binding.tvGreeting.setText("Hello, " + name + "!");
                    }
                });




        // Category clicks → MenuActivity
        binding.ivCatBreakfast.setOnClickListener(v -> openMenu("Breakfast"));
        binding.ivCatLunch.setOnClickListener(v -> openMenu("Lunch"));
        binding.ivCatSnack.setOnClickListener(v -> openMenu("Snack"));
        binding.ivCatBeverage.setOnClickListener(v -> openMenu("Beverage"));
        binding.ivCatThali.setOnClickListener(v -> openMenu("Thali"));
        binding.ivCatDessert.setOnClickListener(v -> openMenu("Dessert"));

        // "See More" → MenuActivity (all items)
        binding.tvSeeMore.setOnClickListener(v -> openMenu("All"));

        binding.navCart.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, CartActivity.class))
        );

        binding.navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            intent.putExtra("open_search", true);
            startActivity(intent);
        });

        binding.ivAvatar.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
        );

        // Bottom nav
        // Add these after you've merged Sakshi's files
//        binding.navOrders.setOnClickListener(v ->
//                startActivity(new Intent(HomeActivity.this, OrdersActivity.class))
//        );
//        binding.navProfile.setOnClickListener(v ->
//                startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
//        );
    }

    private void openMenu(String category) {
        Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
