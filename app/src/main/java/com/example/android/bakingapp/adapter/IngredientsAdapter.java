package com.example.android.bakingapp.adapter;

//http://jakewharton.github.io/butterknife/

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    public List<Ingredients> ingredientsList;

    public int rowLayout;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientsQuantity)
        TextView textViewIngredientsQuantity;
        @BindView(R.id.ingredientsMeasure)
        TextView textViewIngredientsMeasure;
        @BindView(R.id.ingredientsIngredient)
        TextView textViewIngredientsIngredient;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

    public IngredientsAdapter(List<Ingredients> ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public IngredientsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.MyViewHolder holder, final int position) {

        TextView textViewIngredientsQuantity = holder.textViewIngredientsQuantity;
        TextView textViewIngredientsMeasure = holder.textViewIngredientsMeasure;
        TextView textViewIngredientsIngredient = holder.textViewIngredientsIngredient;

        textViewIngredientsQuantity.setText(String.valueOf(ingredientsList.get(position).getQuantity()));
        textViewIngredientsMeasure.setText(ingredientsList.get(position).getMeasure());
        textViewIngredientsIngredient.setText(ingredientsList.get(position).getIngredient());

    }

    @Override
    public int getItemCount() {
        if (ingredientsList != null) {
            return ingredientsList.size();
        }
        else {
            return 0;
        }
    }
}
