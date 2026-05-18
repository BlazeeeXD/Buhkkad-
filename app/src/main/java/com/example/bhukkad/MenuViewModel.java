package com.example.bhukkad;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.bhukkad.data.model.MenuItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuViewModel extends ViewModel {

    private final MutableLiveData<List<MenuItem>> menuItems = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<MenuItem>> getMenuItems() {
        return menuItems;
    }

    public void loadMenu() {
        // Real-time listener for ALL items (staff needs to see everything to edit it)
        db.collection("menu_items")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("MenuViewModel", "Listen failed.", error);
                        return;
                    }

                    // Group items by category to create headers
                    Map<String, List<MenuItem>> groupedItems = new HashMap<>();

                    for (QueryDocumentSnapshot doc : value) {
                        MenuItem item = doc.toObject(MenuItem.class);
                        String category = item.getCategory() != null ? item.getCategory() : "Uncategorized";

                        if (!groupedItems.containsKey(category)) {
                            groupedItems.put(category, new ArrayList<>());
                        }
                        groupedItems.get(category).add(item);
                    }

                    // Rebuild the final list with headers inserted
                    List<MenuItem> finalDisplayList = new ArrayList<>();
                    for (Map.Entry<String, List<MenuItem>> entry : groupedItems.entrySet()) {
                        // Insert Header
                        finalDisplayList.add(new MenuItem(entry.getKey(), true));
                        // Insert the actual items under that header
                        finalDisplayList.addAll(entry.getValue());
                    }

                    menuItems.setValue(finalDisplayList);
                });
    }
}