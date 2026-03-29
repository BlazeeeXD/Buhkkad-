package com.example.bhukkad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {

    private final MutableLiveData<List<MenuItem>> menuItems = new MutableLiveData<>();

    public LiveData<List<MenuItem>> getMenuItems() {
        return menuItems;
    }

    public void loadMenu() {
        List<MenuItem> list = new ArrayList<>();

        // Coffee Section
        list.add(new MenuItem("Coffee")); // Header
        list.add(new MenuItem("Espresso", "₹80", R.drawable.espresso, true, "Coffee"));
        list.add(new MenuItem("Cappucino", "₹120", R.drawable.cappucino, true, "Coffee"));
        list.add(new MenuItem("Cold Brew", "₹140", R.drawable.cold_brew, false, "Coffee"));

        // Snacks Section
        list.add(new MenuItem("Snacks")); // Header
        list.add(new MenuItem("Paneer Sandwich", "₹90", R.drawable.paneer_sandwich, true, "Snacks"));
        list.add(new MenuItem("Samosa", "₹30", R.drawable.samosa, true, "Snacks"));

        // Meals Section
        list.add(new MenuItem("Meals")); // Header
        list.add(new MenuItem("Penne Arrabbiata", "₹150", R.drawable.penne_arrabbiata, true, "Meals"));

        menuItems.setValue(list);
    }


}