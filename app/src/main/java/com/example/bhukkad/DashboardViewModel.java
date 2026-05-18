package com.example.bhukkad;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bhukkad.data.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> revenue = new MutableLiveData<>("₹ 0.00");
    private final MutableLiveData<Integer> ordersToday = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pendingOrders = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> preparingOrders = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> completedOrders = new MutableLiveData<>(0);
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>(new ArrayList<>());

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<String> getRevenue() { return revenue; }
    public LiveData<Integer> getOrdersToday() { return ordersToday; }
    public LiveData<Integer> getPendingOrders() { return pendingOrders; }
    public LiveData<Integer> getPreparingOrders() { return preparingOrders; }
    public LiveData<Integer> getCompletedOrders() { return completedOrders; }
    public LiveData<List<Order>> getOrders() { return orders; }

    public void listenToLiveOrders() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfDay = cal.getTimeInMillis();

        db.collection("orders")
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("DashboardViewModel", "Listen failed.", error);
                        return;
                    }

                    double totalRevenue = 0;
                    int pendingCount = 0;
                    int preparingCount = 0;
                    int completedCount = 0;
                    List<Order> activeOrdersList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : value) {
                        Order order = doc.toObject(Order.class);
                        String status = order.getStatus();

                        if ("Completed".equalsIgnoreCase(status)) {
                            totalRevenue += order.getTotalPrice();
                            completedCount++;
                        } else {
                            activeOrdersList.add(order);
                            if ("Pending".equalsIgnoreCase(status)) pendingCount++;
                            if ("Preparing".equalsIgnoreCase(status)) preparingCount++;
                        }
                    }

                    ordersToday.setValue(value.size());
                    pendingOrders.setValue(pendingCount);
                    preparingOrders.setValue(preparingCount);
                    completedOrders.setValue(completedCount);
                    revenue.setValue(String.format("₹ %.2f", totalRevenue));
                    orders.setValue(activeOrdersList);
                });
    }
}