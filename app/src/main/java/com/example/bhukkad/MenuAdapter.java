package com.example.bhukkad;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bhukkad.data.model.MenuItem;
import com.example.bhukkad.databinding.ItemMenuCardBinding;
import com.example.bhukkad.databinding.ItemMenuHeaderBinding;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MenuItem> items;
    private final MenuInteractionListener listener;

    // Upgraded Interface to handle both Edits and Toggles
    public interface MenuInteractionListener {
        void onEditClick(MenuItem item, int position);
        void onAvailabilityToggled(MenuItem item, boolean isAvailable);
    }

    public MenuAdapter(List<MenuItem> items, MenuInteractionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MenuItem.TYPE_HEADER) {
            ItemMenuHeaderBinding headerBinding = ItemMenuHeaderBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new HeaderViewHolder(headerBinding);
        } else {
            ItemMenuCardBinding cardBinding = ItemMenuCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new FoodViewHolder(cardBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).binding.tvHeaderTitle.setText(item.getName());

        } else if (holder instanceof FoodViewHolder) {
            FoodViewHolder foodHolder = (FoodViewHolder) holder;

            foodHolder.binding.tvFoodName.setText(item.getName());
            foodHolder.binding.tvFoodPrice.setText("₹" + item.getPrice());

            Glide.with(foodHolder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.bg_chip_unselected)
                    .into(foodHolder.binding.ivFoodImage);

            updateUIState(foodHolder, item.isAvailable());

            // THE FIX: Detach the listener BEFORE setting the state so scrolling doesn't cause ghost clicks
            foodHolder.binding.switchAvailable.setOnCheckedChangeListener(null);

            // Set the actual state from the database
            foodHolder.binding.switchAvailable.setChecked(item.isAvailable());

            // Reattach the listener to catch REAL human clicks
            foodHolder.binding.switchAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (item.isAvailable() != isChecked) { // Only act if it actually changed
                    item.setAvailable(isChecked);
                    updateUIState(foodHolder, isChecked);

                    // Tell the Activity to push this to Firebase
                    if (listener != null) {
                        listener.onAvailabilityToggled(item, isChecked);
                    }
                }
            });

            foodHolder.binding.ivEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(item, position);
                }
            });
        }
    }

    private void updateUIState(FoodViewHolder holder, boolean isAvailable) {
        if (isAvailable) {
            holder.binding.tvAvailability.setText("Available");
            holder.binding.tvAvailability.setTextColor(Color.parseColor("#666666"));
            holder.binding.layoutContainer.setAlpha(1.0f);
        } else {
            holder.binding.tvAvailability.setText("Out of stock");
            holder.binding.tvAvailability.setTextColor(Color.RED);
            holder.binding.layoutContainer.setAlpha(0.6f);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ItemMenuHeaderBinding binding;
        HeaderViewHolder(ItemMenuHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ItemMenuCardBinding binding;
        FoodViewHolder(ItemMenuCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}