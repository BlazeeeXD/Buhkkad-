package com.example.bhukkad.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;
import com.example.bhukkad.data.model.Order;
import com.example.bhukkad.ui.home.HomeActivity;
import com.example.bhukkad.ui.menu.MenuActivity;
import com.example.bhukkad.ui.ordering.CartActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class OrderTrackingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListenerRegistration orderListener;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        orderId = getIntent().getStringExtra("order_id");
        Log.d("OrderTracking", "Received order ID: " + orderId);

        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Grab the ID from PaymentSuccessActivity
        orderId = getIntent().getStringExtra("order_id");

        if (orderId != null) {
            listenToOrderStatus();
        } else {
            Toast.makeText(this, "Error finding order", Toast.LENGTH_SHORT).show();
        }


        findViewById(R.id.nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.nav_search).setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("open_search", true);
            startActivity(intent);
        });

        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }


    private void listenToOrderStatus() {
        orderListener = db.collection("orders").document(orderId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w("OrderTracking", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Order currentOrder = snapshot.toObject(Order.class);
                        if (currentOrder != null) {
                            updateUIWithStatus(currentOrder.getStatus());
                        }
                    }
                });
    }

    private void updateUIWithStatus(String status) {
        // Find your text views or UI elements for the timeline here
        // e.g., TextView tvStatus = findViewById(R.id.tvCurrentStatus);
        // tvStatus.setText("Status: " + status);

        // For debugging/placeholder until you link your specific timeline XML IDs:
        Toast.makeText(this, "Order is now: " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Crucial: Detach the listener to save battery/money when user leaves screen
        if (orderListener != null) {
            orderListener.remove();
        }
    }
}