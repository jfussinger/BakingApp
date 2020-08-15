package com.example.android.bakingapp.roomdatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.List;

import butterknife.BindView;

public class IngredientRoomListAdapter extends RecyclerView.Adapter<IngredientRoomListAdapter.IngredientViewHolder> {

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipeId)
        TextView textViewRecipeId;
        @BindView(R.id.ingredientsQuantity)
        TextView textViewIngredientsQuantity;
        @BindView(R.id.ingredientsMeasure)
        TextView textViewIngredientsMeasure;
        @BindView(R.id.ingredientsIngredient)
        TextView textViewIngredientsIngredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);

        }
    }

    public final LayoutInflater mInflater;
    public List<IngredientEntry> mIngredients; // Cached copy of ingredients

    public IngredientRoomListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.widget_layout, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        IngredientEntry current = mIngredients.get(position);

        TextView textViewRecipeId = holder.textViewRecipeId;
        TextView textViewIngredientsQuantity = holder.textViewIngredientsQuantity;
        TextView textViewIngredientsMeasure = holder.textViewIngredientsMeasure;
        TextView textViewIngredientsIngredient = holder.textViewIngredientsIngredient;

        textViewRecipeId.setText(String.valueOf(current.getId()));
        textViewIngredientsQuantity.setText(String.valueOf(current.getQuantity()));
        textViewIngredientsMeasure.setText(current.getMeasure());
        textViewIngredientsIngredient.setText(current.getIngredient());


    }

    public void setIngredients(List<IngredientEntry> ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mIngredients != null)
            return mIngredients.size();
        else return 0;
    }
}


