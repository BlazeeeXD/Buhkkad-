package com.example.bhukkad;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.data.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StaffScreen3Activity extends AppCompatActivity {

    private String currentStatus = "Pending";
    private String orderId = null;

    private Button btnAction;
    private TextView tvBadge, tvOrderId, tvCustomerName, tvStudentId, tvOrderTime, tvPickupTime, tvItemsList, tvItemsPrices, tvTotalPrice;

    private FirebaseFirestore db;
    private ListenerRegistration orderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen3);

        db = FirebaseFirestore.getInstance();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnAction = findViewById(R.id.btnAction);
        tvBadge = findViewById(R.id.tvBadge);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        tvItemsList = findViewById(R.id.tvItemsList);
        tvItemsPrices = findViewById(R.id.tvItemsPrices);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            if (orderId != null) {
                listenToOrderDetails();
            } else {
                Toast.makeText(this, "Order ID missing", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        if (btnAction != null) btnAction.setOnClickListener(v -> handleAction());
    }

    private void listenToOrderDetails() {
        orderListener = db.collection("orders").document(orderId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e("StaffScreen3", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Order order = snapshot.toObject(Order.class);
                        if (order != null) {
                            populateUI(order);
                        }
                    }
                });
    }

    private void populateUI(Order order) {
        currentStatus = order.getStatus();

        tvOrderId.setText("Order ID: #" + order.getOrderId().substring(0, 8).toUpperCase());
        tvBadge.setText(currentStatus);

        tvCustomerName.setText(order.getGuestName());
        // Simple Student ID fallback using Firebase ID
        tvStudentId.setText(order.getUserId().substring(0, 8).toUpperCase());

        tvPickupTime.setText(order.getPickupTime());
        tvTotalPrice.setText(String.format("₹%.2f", order.getTotalPrice()));

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        tvOrderTime.setText(sdf.format(new Date(order.getTimestamp())));

        StringBuilder itemsText = new StringBuilder();
        StringBuilder pricesText = new StringBuilder();
        if (order.getItems() != null) {
            for (Order.CartItem item : order.getItems()) {
                itemsText.append("• ").append(item.getItemName()).append("\n  Qty: ").append(item.getQuantity()).append("\n");
                pricesText.append(String.format("₹%d\n\n", item.getPriceAtPurchase() * item.getQuantity()));
            }
        }
        tvItemsList.setText(itemsText.toString().trim());
        tvItemsPrices.setText(pricesText.toString().trim());

        updateButtonState();
    }

    private void updateButtonState() {
        if (currentStatus.equalsIgnoreCase("Pending")) {
            btnAction.setText("Mark as Preparing");
            btnAction.setEnabled(true);
        } else if (currentStatus.equalsIgnoreCase("Preparing")) {
            btnAction.setText("Mark as Ready");
            btnAction.setEnabled(true);
        } else if (currentStatus.equalsIgnoreCase("Ready")) {
            btnAction.setText("Mark as Complete");
            btnAction.setEnabled(true);
        } else {
            // Completed or Cancelled
            btnAction.setText("Order " + currentStatus);
            btnAction.setEnabled(false);
        }
    }

    private void handleAction() {
        String nextStatus;
        if (currentStatus.equalsIgnoreCase("Pending")) nextStatus = "Preparing";
        else if (currentStatus.equalsIgnoreCase("Preparing")) nextStatus = "Ready";
        else nextStatus = "Completed";

        btnAction.setEnabled(false);
        btnAction.setText("Updating...");

        db.collection("orders").document(orderId)
                .update("status", nextStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order updated to " + nextStatus, Toast.LENGTH_SHORT).show();
                    if (nextStatus.equals("Completed")) {
                        finish(); // Auto-close if finished
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    updateButtonState();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderListener != null) {
            orderListener.remove(); // Save memory/battery when closing
        }
    }
}