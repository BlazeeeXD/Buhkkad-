package com.example.bhukkad.ui.menu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bhukkad.data.model.MenuItem;
import com.example.bhukkad.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> items;

    public MenuAdapter(List<MenuItem> items) {
        this.items = items;
    }

    public void updateItems(List<MenuItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_cards, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvDesc.setText(item.getDescription());
        holder.tvPrice.setText("₹" + item.getPrice()); // Price is now an int

        // Use Glide to load the image URL from Firebase
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.bg_chip_unselected) // Fallback grey box
                .into(holder.ivImage);

        holder.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_price", item.getPrice());
            intent.putExtra("item_description", item.getDescription());
            // You can also pass the image URL if you want the Detail screen to show it!
            intent.putExtra("item_image", item.getImageUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice, btnAdd;
        ImageView ivImage; // Added this for you!

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName  = itemView.findViewById(R.id.tv_food_name);
            tvDesc  = itemView.findViewById(R.id.tv_food_desc);
            tvPrice = itemView.findViewById(R.id.tv_food_price);
            btnAdd  = itemView.findViewById(R.id.btn_add);
            ivImage = itemView.findViewById(R.id.iv_food_image); // Ensure this matches your XML ID
        }
    }
}