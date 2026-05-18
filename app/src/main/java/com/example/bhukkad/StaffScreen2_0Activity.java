package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class StaffScreen2_0Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen2_0);

        MaterialCardView cardOrdersToday = findViewById(R.id.cardOrdersToday);
        MaterialCardView cardIncomingOrders = findViewById(R.id.cardIncomingOrders);
        MaterialCardView cardMenuManagement = findViewById(R.id.cardMenuManagement);
        MaterialCardView cardRecentOrder = findViewById(R.id.cardRecentOrder);

        if (cardOrdersToday != null) {
            cardOrdersToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StaffScreen2_0Activity.this, StaffScreen2Activity.class);
                    startActivity(intent);
                }
            });
        }

        if (cardIncomingOrders != null) {
            cardIncomingOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StaffScreen2_0Activity.this, StaffScreen2Activity.class);
                    startActivity(intent);
                }
            });
        }

        if (cardMenuManagement != null) {
            cardMenuManagement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StaffScreen2_0Activity.this, StaffScreen9Activity.class);
                    startActivity(intent);
                }
            });
        }

        if (cardRecentOrder != null) {
            cardRecentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to Order Details (Screen 3)
                    Intent intent = new Intent(StaffScreen2_0Activity.this, StaffScreen3Activity.class);
                    startActivity(intent);
                }
            });
        }
    }
}