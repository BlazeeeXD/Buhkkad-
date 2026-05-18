package com.example.bhukkad;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class MenuItem implements Serializable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOD_ITEM = 1;

    @DocumentId // Tells Firestore to inject the document ID here
    private String id;

    private String name;
    private String price;
    private String description;
    private String imageUri;
    private int imageResId;
    private String category;
    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isBestseller;
    private int viewType;

    // 1. REQUIRED BY FIREBASE: Empty Constructor
    public MenuItem() {}

    // Constructor for Header
    public MenuItem(String name) {
        this.name = name;
        this.viewType = TYPE_HEADER;
    }

    // Constructor for the Form/Database (Staff Side)
    public MenuItem(String name, String price, String description, String imageUri,
                    String category, boolean isAvailable, boolean isVegetarian, boolean isBestseller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUri = imageUri;
        this.category = category;
        this.isAvailable = isAvailable;
        this.isVegetarian = isVegetarian;
        this.isBestseller = isBestseller;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // Constructor for Mock data (You can phase this out later)
    public MenuItem(String name, String price, int imageResId, boolean isAvailable, String category) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.isAvailable = isAvailable;
        this.category = category;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUri() { return imageUri; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isVegetarian() { return isVegetarian; }
    public boolean isBestseller() { return isBestseller; }
    public int getViewType() { return viewType; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setViewType(int viewType) { this.viewType = viewType; }
}