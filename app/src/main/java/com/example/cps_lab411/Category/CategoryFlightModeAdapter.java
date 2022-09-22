package com.example.cps_lab411.Category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cps_lab411.R;

import java.util.List;

public class CategoryFlightModeAdapter extends ArrayAdapter<CategoryMode> {
    public CategoryFlightModeAdapter(@NonNull Context context, int resource, @NonNull List<CategoryMode> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mode_selected, parent, false);
        TextView tvSelectedMode = convertView.findViewById(R.id.tv_mod_selected);

        CategoryMode category = this.getItem(position);
        if(category != null) {
            //tvSelectedMode.setText(category.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mode_category, parent, false);
        TextView tvCategoryMode = convertView.findViewById(R.id.tv_mode_category);

        CategoryMode category = this.getItem(position);
        if(category != null) {
            tvCategoryMode.setText(category.getName());
        }
        return convertView;
    }
}
