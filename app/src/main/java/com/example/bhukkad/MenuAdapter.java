package com.example.bhukkad;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bhukkad.databinding.ItemMenuCardBinding;
import com.example.bhukkad.databinding.ItemMenuHeaderBinding;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MenuItem> items;

    public MenuAdapter(List<MenuItem> items) {
        this.items = items;
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

            // Set Data
            foodHolder.binding.tvFoodName.setText(item.getName());
            foodHolder.binding.tvFoodPrice.setText(item.getPrice());
            foodHolder.binding.ivFoodImage.setImageResource(item.getImageResId());

            // Set Initial Switch State
            updateUIState(foodHolder, item.isAvailable());
            foodHolder.binding.switchAvailable.setChecked(item.isAvailable());

            // Switch Listener
            foodHolder.binding.switchAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setAvailable(isChecked);
                updateUIState(foodHolder, isChecked);
            });

            // Edit Icon Listener
            foodHolder.binding.ivEdit.setOnClickListener(v ->
                    Toast.makeText(v.getContext(), "Edit " + item.getName(), Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void updateUIState(FoodViewHolder holder, boolean isAvailable) {
        if (isAvailable) {
            holder.binding.tvAvailability.setText("Available");
            holder.binding.tvAvailability.setTextColor(Color.parseColor("#666666"));
            holder.binding.layoutContainer.setAlpha(1.0f); // Fully visible
        } else {
            holder.binding.tvAvailability.setText("Out of stock");
            holder.binding.tvAvailability.setTextColor(Color.RED);
            holder.binding.layoutContainer.setAlpha(0.6f); // Dim the card
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder for Category Headers
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ItemMenuHeaderBinding binding;
        HeaderViewHolder(ItemMenuHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // ViewHolder for Food Cards
    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ItemMenuCardBinding binding;
        FoodViewHolder(ItemMenuCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}