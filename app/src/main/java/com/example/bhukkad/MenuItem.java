package com.example.bhukkad;

import java.io.Serializable;

public class MenuItem implements Serializable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOD_ITEM = 1;

    private String name;
    private String price;
    private String description;
    private String imageUri; // Store gallery URI or URL
    private int imageResId;  // For your local mock images
    private String category;
    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isBestseller;
    private int viewType;

    // Constructor for Header
    public MenuItem(String name) {
        this.name = name;
        this.viewType = TYPE_HEADER;
    }

    // Constructor for Mock data used in MenuViewModel
    public MenuItem(String name, String price, int imageResId, boolean isAvailable, String category) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.isAvailable = isAvailable;
        this.category = category;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // Constructor for the Form/Database
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

    // Getters for all fields
    public String getDescription() { return description; }
    public String getImageUri() { return imageUri; }
    public boolean isVegetarian() { return isVegetarian; }
    public boolean isBestseller() { return isBestseller; }
    public int getViewType() { return viewType; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }

    // Setters
    public void setAvailable(boolean available) { isAvailable = available; }
}
