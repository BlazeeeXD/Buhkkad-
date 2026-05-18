package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bhukkad.databinding.ActivityStaffDashboardBinding;
import com.example.bhukkad.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class StaffDashboardActivity extends AppCompatActivity {

    private ActivityStaffDashboardBinding binding;
    private OrderAdapter adapter;
    private DashboardViewModel viewModel;
    private int lastClickedPosition = -1;

    private final ActivityResultLauncher<Intent> orderDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newStatus = result.getData().getStringExtra("newStatus");
                    if (newStatus != null && lastClickedPosition != -1) {
                        adapter.updateItemStatus(lastClickedPosition, newStatus);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        observeViewModel();
        setupRecyclerView();
        setupClickListeners();
    }

    private void observeViewModel() {
        viewModel.getRevenue().observe(this, amount -> binding.revenueTextId.setText(amount));
        viewModel.getOrdersToday().observe(this, count -> binding.orderCountId.setText(String.valueOf(count)));

        // Dynamically update the specific state cards
        viewModel.getPendingOrders().observe(this, count -> binding.pendingCountId.setText(String.valueOf(count)));
        viewModel.getPreparingOrders().observe(this, count -> binding.preparingCountId.setText(String.valueOf(count)));
        viewModel.getCompletedOrders().observe(this, count -> binding.completedCountId.setText(String.valueOf(count)));
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(new ArrayList<>(), (order, position) -> {
            lastClickedPosition = position;
            Intent intent = new Intent(StaffDashboardActivity.this, StaffScreen3Activity.class);
            intent.putExtra("status", order.getStatus());
            intent.putExtra("orderId", order.getOrderId());
            orderDetailLauncher.launch(intent);
        });

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrders.setAdapter(adapter);

        viewModel.getOrders().observe(this, newOrders -> adapter.updateList(newOrders));
        viewModel.listenToLiveOrders();
    }

    private void setupClickListeners() {
        // Main button
        binding.btnIncoming.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, StaffScreen2Activity.class);
            startActivity(intent);
        });

        // The specific Pending Card route
        binding.cardPending.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, StaffScreen2Activity.class);
            intent.putExtra("target_tab", "Pending");
            startActivity(intent);
        });

        // The specific Preparing Card route
        binding.cardPreparing.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, StaffScreen2Activity.class);
            intent.putExtra("target_tab", "Preparing");
            startActivity(intent);
        });

        binding.btnMenuManagement.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, MenuManagementActivity.class);
            startActivity(intent);
        });

        binding.imgLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out securely", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StaffDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}