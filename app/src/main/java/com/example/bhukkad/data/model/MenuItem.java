package com.example.bhukkad.data.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class MenuItem implements Serializable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOD_ITEM = 1;

    @DocumentId
    private String id; // Firebase will automatically inject the document ID here

    private String name;
    private String description;
    private int price;
    private String imageUrl; // Replacing the local imageResId
    private String category;
    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isBestseller;

    // We don't want to save UI states to the database
    @Exclude
    private int viewType;

    // 1. REQUIRED BY FIREBASE: Empty Constructor
    public MenuItem() {
        this.viewType = TYPE_FOOD_ITEM; // Default
    }

    // 2. Local Constructor for Headers (Categories)
    public MenuItem(String name, boolean isHeader) {
        this.name = name;
        this.viewType = isHeader ? TYPE_HEADER : TYPE_FOOD_ITEM;
    }

    // Constructor for local seeding/testing (matches the usage in MenuActivity)
    public MenuItem(String name, String description, int price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = true;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // 3. Full Constructor for creating items in the database
    public MenuItem(String name, String description, int price, String imageUrl,
                    String category, boolean isAvailable, boolean isVegetarian, boolean isBestseller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isAvailable = isAvailable;
        this.isVegetarian = isVegetarian;
        this.isBestseller = isBestseller;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isVegetarian() { return isVegetarian; }
    public boolean isBestseller() { return isBestseller; }

    @Exclude
    public int getViewType() { return viewType; }

    // --- SETTERS ---
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(int price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setVegetarian(boolean vegetarian) { this.isVegetarian = vegetarian; }
    public void setBestseller(boolean bestseller) { this.isBestseller = bestseller; }

    @Exclude
    public void setViewType(int viewType) { this.viewType = viewType; }
}