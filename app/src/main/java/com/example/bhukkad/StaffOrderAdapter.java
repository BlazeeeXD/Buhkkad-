package com.example.bhukkad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhukkad.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffOrderAdapter extends RecyclerView.Adapter<StaffOrderAdapter.ViewHolder> {

    private List<Order> orders;
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public StaffOrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    public void updateList(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText("Order ID: #" + order.getOrderId().substring(0, 8).toUpperCase());
        holder.tvGuestName.setText(order.getGuestName());
        holder.tvPickupTime.setText(order.getPickupTime());
        holder.tvTotalPrice.setText(String.format("₹%.2f", order.getTotalPrice()));

        // Format timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.tvOrderTime.setText("Ordered at " + sdf.format(new Date(order.getTimestamp())));

        // Build the dynamic items list
        StringBuilder itemsText = new StringBuilder();
        StringBuilder pricesText = new StringBuilder();
        if (order.getItems() != null) {
            for (Order.CartItem item : order.getItems()) {
                itemsText.append(item.getItemName()).append(" x ").append(item.getQuantity()).append("\n");
                pricesText.append("₹").append(item.getPriceAtPurchase() * item.getQuantity()).append("\n");
            }
        }
        holder.tvItemsList.setText(itemsText.toString().trim());
        holder.tvItemsPrices.setText(pricesText.toString().trim());

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvGuestName, tvPickupTime, tvItemsList, tvItemsPrices, tvOrderTime, tvTotalPrice;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvGuestName = itemView.findViewById(R.id.tvGuestName);
            tvPickupTime = itemView.findViewById(R.id.tvPickupTime);
            tvItemsList = itemView.findViewById(R.id.tvItemsList);
            tvItemsPrices = itemView.findViewById(R.id.tvItemsPrices);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}