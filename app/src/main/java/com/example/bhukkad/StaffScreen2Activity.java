package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhukkad.data.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffScreen2Activity extends AppCompatActivity {

    private TextView tabPending, tabPreparing, tabReady;
    private String currentStatus = "Pending";

    private RecyclerView rvOrders;
    private StaffOrderAdapter adapter;
    private List<Order> allActiveOrders = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen2);

        db = FirebaseFirestore.getInstance();

        ImageButton btnBack = findViewById(R.id.btnBack);
        tabPending = findViewById(R.id.tabPending);
        tabPreparing = findViewById(R.id.tabPreparing);
        tabReady = findViewById(R.id.tabReady);
        rvOrders = findViewById(R.id.rvOrders);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Catch the specific tab request if it came from a dashboard card
        if (getIntent() != null && getIntent().hasExtra("target_tab")) {
            currentStatus = getIntent().getStringExtra("target_tab");
        }

        setupTabs();
        setupRecyclerView();
        listenToActiveOrders();
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StaffOrderAdapter(new ArrayList<>(), order -> {
            Intent intent = new Intent(StaffScreen2Activity.this, StaffScreen3Activity.class);
            intent.putExtra("status", order.getStatus());
            intent.putExtra("orderId", order.getOrderId());
            startActivity(intent); // No need for result anymore, Screen 3 listens dynamically!
        });
        rvOrders.setAdapter(adapter);
    }

    private void listenToActiveOrders() {
        db.collection("orders")
                .whereIn("status", Arrays.asList("Pending", "Preparing", "Ready"))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("StaffScreen2", "Listen failed.", error);
                        return;
                    }
                    allActiveOrders.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        allActiveOrders.add(doc.toObject(Order.class));
                    }
                    // Sort descending by timestamp
                    allActiveOrders.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
                    filterAndDisplay();
                });
    }

    private void setupTabs() {
        tabPending.setOnClickListener(v -> selectTab("Pending"));
        tabPreparing.setOnClickListener(v -> selectTab("Preparing"));
        tabReady.setOnClickListener(v -> selectTab("Ready"));
        selectTab(currentStatus); // Initial highlight
    }

    private void selectTab(String status) {
        currentStatus = status;
        resetTab(tabPending);
        resetTab(tabPreparing);
        resetTab(tabReady);

        if (status.equalsIgnoreCase("Pending")) highlightTab(tabPending);
        else if (status.equalsIgnoreCase("Preparing")) highlightTab(tabPreparing);
        else if (status.equalsIgnoreCase("Ready")) highlightTab(tabReady);

        filterAndDisplay();
    }

    private void filterAndDisplay() {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : allActiveOrders) {
            if (order.getStatus().equalsIgnoreCase(currentStatus)) {
                filteredList.add(order);
            }
        }
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }

    private void resetTab(TextView tab) {
        tab.setBackground(null);
        tab.setTextColor(ContextCompat.getColor(this, R.color.text_light));
    }

    private void highlightTab(TextView tab) {
        tab.setBackgroundResource(R.drawable.tab_selected);
        tab.setTextColor(ContextCompat.getColor(this, R.color.primary_brown));
    }
}