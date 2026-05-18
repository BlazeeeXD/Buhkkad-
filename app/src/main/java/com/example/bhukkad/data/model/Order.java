package com.example.bhukkad.data.model;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    @DocumentId
    private String orderId;

    private String userId;
    private String guestName;
    private String status; // "Pending", "Preparing", "Ready", "Completed"
    private String pickupTime;
    private int itemCount;
    private double totalPrice;
    private long timestamp; // Helps sort orders by newest

    // A list of the specific items they bought
    private List<CartItem> items;

    // REQUIRED BY FIREBASE: Empty Constructor
    public Order() {}

    public Order(String userId, String guestName, String status, String pickupTime,
                 int itemCount, double totalPrice, long timestamp, List<CartItem> items) {
        this.userId = userId;
        this.guestName = guestName;
        this.status = status;
        this.pickupTime = pickupTime;
        this.itemCount = itemCount;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.items = items;
    }

    // --- GETTERS ---
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getGuestName() { return guestName; }
    public String getStatus() { return status; }
    public String getPickupTime() { return pickupTime; }
    public int getItemCount() { return itemCount; }
    public double getTotalPrice() { return totalPrice; }
    public long getTimestamp() { return timestamp; }
    public List<CartItem> getItems() { return items; }

    // --- SETTERS ---
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setStatus(String status) { this.status = status; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setItems(List<CartItem> items) { this.items = items; }

    // ==========================================
    // NESTED CLASS: Snapshot of the items bought
    // ==========================================
    public static class CartItem implements Serializable {
        private String itemName;
        private int quantity;
        private int priceAtPurchase;

        // REQUIRED BY FIREBASE: Empty Constructor
        public CartItem() {}

        public CartItem(String itemName, int quantity, int priceAtPurchase) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.priceAtPurchase = priceAtPurchase;
        }

        public String getItemName() { return itemName; }
        public int getQuantity() { return quantity; }
        public int getPriceAtPurchase() { return priceAtPurchase; }

        public void setItemName(String itemName) { this.itemName = itemName; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setPriceAtPurchase(int priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
    }
}
