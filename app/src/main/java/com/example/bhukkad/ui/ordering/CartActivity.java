package com.example.bhukkad.ui.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhukkad.R;
import com.example.bhukkad.data.model.Order.CartItem;
import com.example.bhukkad.utils.CartManager;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartListener {

    private final double taxRate = 0.05;
    private CartAdapter adapter;
    private TextView tvSubtotal, tvTaxes, tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTaxes = findViewById(R.id.tvTaxes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView rvCartItems = findViewById(R.id.rvCartItems); // Ensure this is in your XML!
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        // Pass the list from the Singleton vault to the Adapter
        adapter = new CartAdapter(CartManager.getInstance().getCartItems(), this);
        rvCartItems.setAdapter(adapter);

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (CartManager.getInstance().getTotalItems() == 0) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                // Notice we don't pass quantities via Intent anymore! The vault handles it.
                Intent intent = new Intent(this, PickupTimeActivity.class);
                startActivity(intent);
            }
        });

        updateTotals();
    }

    // --- Interface methods from the Adapter ---
    @Override
    public void onIncrease(CartItem item) {
        CartManager.getInstance().addItem(item.getItemName(), item.getPriceAtPurchase(), 1);
        adapter.notifyDataSetChanged();
        updateTotals();
    }

    @Override
    public void onDecrease(CartItem item) {
        CartManager.getInstance().decrementItem(item.getItemName());
        adapter.notifyDataSetChanged();
        updateTotals();
    }

    private void updateTotals() {
        double subtotal = CartManager.getInstance().getSubtotal();
        double taxes = subtotal * taxRate;
        double total = subtotal + taxes;

        tvSubtotal.setText(String.format("₹%.2f", subtotal));
        tvTaxes.setText(String.format("₹%.2f", taxes));
        tvTotalPrice.setText(String.format("₹%.2f", total));
    }
}