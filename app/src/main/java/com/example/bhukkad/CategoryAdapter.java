package com.example.bhukkad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> {

    public CategoryAdapter(@NonNull Context context, List<String> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // This is the view shown when the spinner is closed
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // This is the view shown in the dropdown list
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner_category, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tv_spinner_text);
        String category = getItem(position);

        if (category != null) {
            textView.setText(category);
        }

        return convertView;
    }
}