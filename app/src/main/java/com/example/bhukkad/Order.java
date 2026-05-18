package com.example.bhukkad;

import com.google.firebase.firestore.DocumentId;
import java.util.List;

public class Order {

    @DocumentId
    private String orderId;

    private String userId; // To track which customer ordered this
    private String guestName;
    private String status; // "pending", "preparing", "ready", "completed"
    private String pickupTime;
    private double subtotal;
    private double tax;
    private double totalPrice;
    private long timestamp; // Helps sort orders by newest

    // Fields for mock/UI display
    private int itemCount;
    private String price;

    // A list of the specific items they bought
    private List<CartItem> items;

    // REQUIRED BY FIREBASE: Empty Constructor
    public Order() {}

    // Constructor for mocking/UI testing
    public Order(String orderId, String status, String guestName, int itemCount, String price) {
        this.orderId = orderId;
        this.status = status;
        this.guestName = guestName;
        this.itemCount = itemCount;
        this.price = price;
    }

    public Order(String userId, String guestName, String status, String pickupTime,
                 double subtotal, double tax, double totalPrice, long timestamp, List<CartItem> items) {
        this.userId = userId;
        this.guestName = guestName;
        this.status = status;
        this.pickupTime = pickupTime;
        this.subtotal = subtotal;
        this.tax = tax;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.items = items;
    }

    // Getters and Setters...
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public String getGuestName() { return guestName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPickupTime() { return pickupTime; }
    public double getSubtotal() { return subtotal; }
    public double getTax() { return tax; }
    public double getTotalPrice() { return totalPrice; }
    public long getTimestamp() { return timestamp; }
    public List<CartItem> getItems() { return items; }
    
    public int getItemCount() {
        if (items != null && !items.isEmpty()) {
            return items.size();
        }
        return itemCount;
    }
    
    public String getPrice() {
        if (price != null) {
            return price;
        }
        return "₹ " + totalPrice;
    }

    // Nested class to represent items inside an order
    public static class CartItem {
        private String itemName;
        private int quantity;
        private double priceAtPurchase;

        public CartItem() {} // Empty constructor for Firebase

        public CartItem(String itemName, int quantity, double priceAtPurchase) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.priceAtPurchase = priceAtPurchase;
        }

        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public double getPriceAtPurchase() { return priceAtPurchase; }
    }
}