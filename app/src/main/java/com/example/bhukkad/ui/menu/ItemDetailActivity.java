package com.example.bhukkad.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bhukkad.R;
import com.example.bhukkad.ui.ordering.CartActivity;

public class ItemDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private String itemName;
    private int itemPrice;
    private String itemDescription;
    private String itemImageUrl;

    private TextView tvQuantity;
    private com.google.android.material.button.MaterialButton btnAddToCart;

    private void updateUI() {
        tvQuantity.setText(String.valueOf(quantity));
        btnAddToCart.setText("Add to cart • ₹" + (quantity * itemPrice));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        if (getIntent().hasExtra("item_name")) {
            itemName = getIntent().getStringExtra("item_name");
            itemPrice = getIntent().getIntExtra("item_price", 0);
            itemDescription = getIntent().getStringExtra("item_description");
            itemImageUrl = getIntent().getStringExtra("item_image"); // Grab the URL
        } else {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.tvProductName);
        TextView tvDesc = findViewById(R.id.tvProductDescription);
        TextView tvPrice = findViewById(R.id.tvPrice);
        ImageView ivProductImage = findViewById(R.id.ivProductImage); // Ensure this matches your XML
        tvQuantity = findViewById(R.id.tvQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        tvName.setText(itemName);
        tvDesc.setText(itemDescription);
        tvPrice.setText("₹" + itemPrice);

        // Load the image using Glide
        if (itemImageUrl != null && !itemImageUrl.isEmpty() && ivProductImage != null) {
            Glide.with(this)
                    .load(itemImageUrl)
                    .placeholder(R.drawable.bg_chip_unselected)
                    .into(ivProductImage);
        }

        updateUI();

        findViewById(R.id.btnPlus).setOnClickListener(v -> {
            quantity++;
            updateUI();
        });

        findViewById(R.id.btnMinus).setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateUI();
            }
        });


        findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
            // Save it to the permanent memory vault!
            com.example.bhukkad.utils.CartManager.getInstance()
                    .addItem(itemName, itemPrice, quantity);

            Toast.makeText(this, quantity + "x " + itemName + " added to cart", Toast.LENGTH_SHORT).show();

            // Go to cart
            startActivity(new Intent(ItemDetailActivity.this, CartActivity.class));
            finish(); // Optional: closes detail screen so back button goes to Menu
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}