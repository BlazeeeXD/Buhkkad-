package com.example.bhukkad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    // MutableLiveData allows us to change the values inside the code
    private final MutableLiveData<String> revenue = new MutableLiveData<>("₹ 957.00");
    private final MutableLiveData<Integer> ordersToday = new MutableLiveData<>(2);
    private final MutableLiveData<Integer> pendingOrders = new MutableLiveData<>(0);

    // LiveData (read-only) for the Activity to observe
    public LiveData<String> getRevenue() { return revenue; }
    public LiveData<Integer> getOrdersToday() { return ordersToday; }
    public LiveData<Integer> getPendingOrders() { return pendingOrders; }

    // This is where you'd later add a method to "fetch" data from an API
    public void updateRevenue(String newAmount) {
        revenue.setValue(newAmount);
    }


    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Order>> getOrders() { return orders; }

    public void loadMockOrders() {
        List<Order> mockList = new ArrayList<>();
        mockList.add(new Order("#OR22637489839004", "Completed", "Guest", 3, "₹ 197"));
        mockList.add(new Order("#ORD1771873462396", "Completed", "Guest", 1, "₹ 450"));
        orders.setValue(mockList);
    }

}
