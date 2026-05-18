package com.example.bhukkad.ui.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;

import java.util.ArrayList;
import java.util.List;

public class PickupTimeActivity extends AppCompatActivity {

    private final List<TextView> slots = new ArrayList<>();
    private TextView selectedSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_time);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        int[] slotIds = {R.id.slot1, R.id.slot2, R.id.slot3, R.id.slot4, R.id.slot5, R.id.slot6, R.id.slot7, R.id.slot8};

        for (int id : slotIds) {
            TextView slot = findViewById(id);
            if (slot != null) {
                slots.add(slot);
                slot.setOnClickListener(v -> selectSlot((TextView) v));
            }
        }

        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            if (selectedSlot != null) {
                Intent intent = new Intent(this, PaymentActivity.class);
                // We ONLY pass the pickup time now. CartManager has everything else!
                intent.putExtra("pickup_time", selectedSlot.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a pickup time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectSlot(TextView slot) {
        if (selectedSlot != null) {
            selectedSlot.setBackgroundResource(R.drawable.bg_time_slot);
            selectedSlot.setTextColor(getResources().getColor(R.color.black));
        }

        selectedSlot = slot;
        selectedSlot.setBackgroundResource(R.drawable.bg_time_slot_selected);
        selectedSlot.setTextColor(getResources().getColor(R.color.white));
    }
}