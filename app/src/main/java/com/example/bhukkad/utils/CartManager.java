package com.example.bhukkad.utils;

import com.example.bhukkad.data.model.Order.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private final List<CartItem> cartItems;

    // Private constructor so nobody else can create a new instance
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // This is how every activity gets the ONE shared cart
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add item or increase quantity if it already exists
    public void addItem(String name, int price, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getItemName().equals(name)) {
                item.setQuantity(item.getQuantity() + quantity);
                return; // Stop looking, we found it
            }
        }
        // If we get here, it's a new item. Add it to the list.
        cartItems.add(new CartItem(name, quantity, price));
    }

    // Decrease quantity, and completely remove if it hits 0
    public void decrementItem(String name) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getItemName().equals(name)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    cartItems.remove(i); // Remove from list completely
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += (item.getPriceAtPurchase() * item.getQuantity());
        }
        return subtotal;
    }
}