package com.example.bhukkad.ui.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;
import com.example.bhukkad.data.model.Order;
import com.example.bhukkad.ui.account.PaymentSuccessActivity;
import com.example.bhukkad.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.List;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String pendingFirestoreOrderId = null;
    private double finalTotalAmount = 0;
    private com.google.android.material.button.MaterialButton btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // We ONLY need the pickup time from the Intent now. Everything else comes from the CartManager.
        String pickupTime = getIntent().getStringExtra("pickup_time");

        TextView tvItems = findViewById(R.id.tvItems);
        TextView tvBreakdown = findViewById(R.id.tvBreakdown);
        TextView tvSummaryTotalPrice = findViewById(R.id.tvSummaryTotalPrice);
        btnPay = findViewById(R.id.btnPay);

        // 1. Fetch everything from the central CartManager vault
        List<Order.CartItem> cartItems = CartManager.getInstance().getCartItems();
        int totalItems = CartManager.getInstance().getTotalItems();
        double subtotal = CartManager.getInstance().getSubtotal();

        // 2. Build the receipt text dynamically based on whatever is in the cart
        StringBuilder itemsText = new StringBuilder();
        for (Order.CartItem item : cartItems) {
            double cost = item.getQuantity() * item.getPriceAtPurchase();
            itemsText.append(String.format("%s x %d - ₹%.2f\n", item.getItemName(), item.getQuantity(), cost));
        }

        double taxRate = 0.05;
        double tax = subtotal * taxRate;
        finalTotalAmount = subtotal + tax;

        // 3. Update the UI
        tvItems.setText(itemsText.toString().trim());
        tvBreakdown.setText(String.format("Subtotal - ₹%.2f\nTax (5%%) - ₹%.2f\nTotal - ₹%.2f", subtotal, tax, finalTotalAmount));
        tvSummaryTotalPrice.setText(String.format("₹%.2f", finalTotalAmount));
        btnPay.setText(String.format("Pay ₹%.2f", finalTotalAmount));

        // 4. Handle the Pay Button Click
        btnPay.setOnClickListener(v -> {
            if (totalItems == 0) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            btnPay.setEnabled(false);
            btnPay.setText("Initializing Secure Payment...");
            createPendingOrderAndPay(pickupTime, totalItems, cartItems);
        });
    }

    private void createPendingOrderAndPay(String pickupTime, int totalItems, List<Order.CartItem> cartItems) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown";
        String userName = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "Guest";

        Order newOrder = new Order(
                userId,
                userName,
                "payment_pending",
                pickupTime != null ? pickupTime : "ASAP",
                totalItems,
                finalTotalAmount,
                System.currentTimeMillis(),
                cartItems
        );

        db.collection("orders").add(newOrder)
                .addOnSuccessListener(documentReference -> {
                    pendingFirestoreOrderId = documentReference.getId();
                    startRazorpayCheckout();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Network error. Try again.", Toast.LENGTH_SHORT).show();
                    btnPay.setEnabled(true);
                    btnPay.setText(String.format("Pay ₹%.2f", finalTotalAmount));
                });
    }

    private void startRazorpayCheckout() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Sj1Q4YA0CdAQpp");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Bhukkad Cafe");
            options.put("description", "Order ID: " + pendingFirestoreOrderId.substring(0, 8).toUpperCase());
            options.put("theme.color", "#6A4E42");
            options.put("currency", "INR");

            int amountInPaise = (int) Math.round(finalTotalAmount * 100);
            options.put("amount", amountInPaise);

            JSONObject prefill = new JSONObject();
            prefill.put("email", mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "student@bmu.edu.in");
            prefill.put("contact", "9999999999"); // Razorpay test number
            options.put("prefill", prefill);

            checkout.open(PaymentActivity.this, options);

        } catch (Exception e) {
            Log.e("Razorpay", "Error in starting Razorpay Checkout", e);
            Toast.makeText(this, "Payment UI failed to load.", Toast.LENGTH_SHORT).show();
            btnPay.setEnabled(true);
            btnPay.setText(String.format("Pay ₹%.2f", finalTotalAmount));
        }
    }

    // --- RAZORPAY CALLBACKS ---

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        Log.d("PAYMENT_SYSTEM", "===============================");
        Log.d("PAYMENT_SYSTEM", "SUCCESS: Razorpay ID -> " + razorpayPaymentID);
        Log.d("PAYMENT_SYSTEM", "===============================");

        // IMPORTANT: Clear the cart after a successful payment!
        CartManager.getInstance().clearCart();

        if (pendingFirestoreOrderId != null) {
            // STORE IT IN FIREBASE
            db.collection("orders").document(pendingFirestoreOrderId)
                    .update(
                            "status", "Pending",
                            "razorpay_payment_id", razorpayPaymentID // The persistent proof
                    )
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                        intent.putExtra("order_id", pendingFirestoreOrderId);
                        intent.putExtra("payment_id", razorpayPaymentID); // Pass to visual screen
                        startActivity(intent);
                        finish();
                    });
        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        Log.e("PAYMENT_SYSTEM", "FAILED: " + response);
        Toast.makeText(this, "Payment Failed/Cancelled.", Toast.LENGTH_LONG).show();
        btnPay.setEnabled(true);
        btnPay.setText(String.format("Pay ₹%.2f", finalTotalAmount));

        if (pendingFirestoreOrderId != null) {
            db.collection("orders").document(pendingFirestoreOrderId).update("status", "cancelled");
        }
    }
}