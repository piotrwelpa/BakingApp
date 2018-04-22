package com.example.piotr.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredientList;
    private Context context;

    private int lastPosition = -1;

    @BindView(R.id.ingredient)
    TextView ingredient;
    @BindView(R.id.ingredient_measure)
    TextView ingredientMeasure;
    @BindView(R.id.ingredient_quantity)
    TextView ingredientQuantity;


    public IngredientListAdapter(ArrayList<Ingredient> ingredientList, Context context) {
        super(context, -1, ingredientList);
        this.ingredientList = ingredientList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );

        View rowView = inflater.inflate(R.layout.ingredient_item, parent, false);
        ButterKnife.bind(this, rowView);

        Ingredient ingredientItem = ingredientList.get(position);

        ingredient.setText(ingredientItem.getIngredient());
        ingredientMeasure.setText(ingredientItem.getMeasure());
        ingredientQuantity.setText(String.valueOf(ingredientItem.getQuantity()));

        return rowView;

    }
}
