package com.example.bhukkad.ui.ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhukkad.R;
import com.example.bhukkad.data.model.Order.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items;
    private final CartListener listener;

    // Interface to let the Activity know when + or - is clicked
    public interface CartListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
    }

    public CartAdapter(List<CartItem> items, CartListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateCart(List<CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // NOTE: Make sure you have an XML layout file named item_cart_row.xml for this!
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_row, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvItemName.setText(item.getItemName());
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        // Calculate the total price for this specific row (Price * Qty)
        int rowTotal = item.getPriceAtPurchase() * item.getQuantity();
        holder.tvItemPrice.setText("₹" + rowTotal);

        holder.btnPlus.setOnClickListener(v -> listener.onIncrease(item));
        holder.btnMinus.setOnClickListener(v -> listener.onDecrease(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemPrice, tvQty;
        ImageView btnPlus, btnMinus; // Change to ImageButton or View depending on your XML

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Replace these IDs with the actual IDs from your item_cart_row.xml
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvQty = itemView.findViewById(R.id.tvQty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}