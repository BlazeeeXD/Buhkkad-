package com.example.bhukkad;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bhukkad.data.model.Order;
import com.example.bhukkad.databinding.ItemOrderRowBinding;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private final OnOrderClickListener listener; // New: listener variable

    // New: Interface to handle clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order, int position);
    }

    // Updated Constructor to include the listener
    public OrderAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderRowBinding binding = ItemOrderRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.binding.orderId.setText("Order ID: " + order.getOrderId());
        holder.binding.statusChip.setText(order.getStatus());
        holder.binding.tvGuestLabel.setText(order.getGuestName());
        holder.binding.itemCount.setText(order.getItemCount() + " items");
        holder.binding.price.setText("₹ " + order.getTotalPrice());

        // New: Set the click listener on the entire card/row
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // New: Method to update just one item when it returns from Screen 3
    public void updateItemStatus(int position, String newStatus) {
        if (position >= 0 && position < orderList.size()) {
            orderList.get(position).setStatus(newStatus);
            notifyItemChanged(position); // This refreshes only that specific card
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderRowBinding binding;
        public OrderViewHolder(ItemOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }
}