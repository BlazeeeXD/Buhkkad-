package com.example.bhukkad.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.widget.TextView;

import com.example.bhukkad.ui.home.HomeActivity;

import com.example.bhukkad.R;

public class PaymentSuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // 4. VISUAL CONFIRMATION SCREEN
        String orderId = getIntent().getStringExtra("order_id");
        String paymentId = getIntent().getStringExtra("payment_id");

        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvPaymentId = findViewById(R.id.tvPaymentId); // Make sure to add this ID to your XML!

        if (orderId != null) {
            // Format to look like a clean receipt number
            tvOrderId.setText("Order ID: #" + orderId.substring(0, 8).toUpperCase());
        }

        if (paymentId != null && tvPaymentId != null) {
            // This is the undeniable proof the user sees
            tvPaymentId.setText("Payment Ref: " + paymentId);
        }

      ;


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, OrderTrackingActivity.class);
            intent.putExtra("order_id", getIntent().getStringExtra("order_id"));
            intent.putExtra("total_price", getIntent().getDoubleExtra("total_price", 0));
            startActivity(intent);
            finish();
        }, 3000); // 3 seconds


    }
}