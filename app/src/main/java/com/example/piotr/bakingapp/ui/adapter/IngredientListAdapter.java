package com.example.piotr.bakingapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.ui.CakeDetailsActivity;
import com.example.piotr.bakingapp.utils.UiHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientListAdapter extends
        RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredientList;

    @BindView(R.id.ingredient)
    TextView ingredient;
    @BindView(R.id.ingredient_measure)
    TextView ingredientMeasure;
    @BindView(R.id.ingredient_quantity)
    TextView ingredientQuantity;


    public IngredientListAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE
//        );
//
//        View rowView = inflater.inflate(R.layout.ingredient_item, parent, false);
//        ButterKnife.bind(this, rowView);
//
//        Ingredient ingredientItem = ingredientList.get(position);
//
//        ingredient.setText(ingredientItem.getIngredient());
//        ingredientMeasure.setText(ingredientItem.getMeasure());
//        ingredientQuantity.setText(String.valueOf(ingredientItem.getQuantity()));
//
//        return rowView;
//
//    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(v, ingredientList);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        initIngredient(holder, position);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    private void initIngredient(IngredientViewHolder holder, int position){
        holder.ingredient.setText(ingredientList.get(position).getIngredient());
        holder.ingredientMeasure.setText(ingredientList.get(position).getMeasure());
        holder.ingredientQuantity.setText(String.valueOf(
                ingredientList.get(position).getQuantity()));
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_quantity)
        TextView ingredientQuantity;
        @BindView(R.id.ingredient_measure)
        TextView ingredientMeasure;
        @BindView(R.id.ingredient)
        TextView ingredient;

        final List<Ingredient> ingredientList;

        public IngredientViewHolder(View itemView, List<Ingredient> ingredients) {
            super(itemView);
            this.ingredientList = ingredients;
            ButterKnife.bind(this, itemView);
        }
    }
}
