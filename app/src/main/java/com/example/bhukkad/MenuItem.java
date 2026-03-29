package com.example.bhukkad;

public class MenuItem {
    // View Type Constants
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOD_ITEM = 1;

    private String name;
    private String price;
    private int imageResId;
    private boolean isAvailable;
    private String category;
    private int viewType;

    // Constructor for Food Items
    public MenuItem(String name, String price, int imageResId, boolean isAvailable, String category) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.isAvailable = isAvailable;
        this.category = category;
        this.viewType = TYPE_FOOD_ITEM;
    }

    // Constructor for Headers
    public MenuItem(String categoryName) {
        this.name = categoryName;
        this.viewType = TYPE_HEADER;
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public boolean isAvailable() { return isAvailable; }
    public String getCategory() { return category; }
    public int getViewType() { return viewType; }

    // Setter for availability (for the Switch logic)
    public void setAvailable(boolean available) { isAvailable = available; }
}