package com.example.bhukkad;

public class Order {
    private String orderId;
    private String status;
    private String guestName;
    private int itemCount;
    private String price;

    public Order(String orderId, String status, String guestName, int itemCount, String price) {
        this.orderId = orderId;
        this.status = status;
        this.guestName = guestName;
        this.itemCount = itemCount;
        this.price = price;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getGuestName() { return guestName; }
    public int getItemCount() { return itemCount; }
    public String getPrice() { return price; }
}
