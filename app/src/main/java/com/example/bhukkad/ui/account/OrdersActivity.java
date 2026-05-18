package com.example.bhukkad.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhukkad.R;
import com.example.bhukkad.data.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_more).setOnClickListener(v -> {
            Intent intent = new Intent(OrdersActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        fetchUserOrders();
    }

    private void fetchUserOrders() {
        if (mAuth.getCurrentUser() == null) return;

        String currentUserId = mAuth.getCurrentUser().getUid();

        // Get orders for THIS user only, newest first
        db.collection("orders")
                .whereEqualTo("userId", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Order> myOrders = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            myOrders.add(document.toObject(Order.class));
                        }

                        // TODO: Pass 'myOrders' into your RecyclerView Adapter here
                        // e.g., myAdapter.updateData(myOrders);
                        Log.d("OrdersActivity", "Fetched " + myOrders.size() + " orders.");

                    } else {
                        Toast.makeText(this, "Failed to load history.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}