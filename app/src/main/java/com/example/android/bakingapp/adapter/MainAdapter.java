package com.example.android.bakingapp.adapter;

//http://jakewharton.github.io/butterknife/

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.IngredientsStepsActivity;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private static final String TAG = "mainadapter";

    Recipe recipe;

    public List<Recipe> recipeList;
    public List<Ingredients> ingredientsList;
    public List<Steps> stepsList;
    public int rowLayout;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image) ImageView image;
        @BindView(R.id.recipeId) TextView textViewRecipeId;
        @BindView(R.id.recipeName) TextView textViewRecipeName;
        @BindView(R.id.recipeServings) TextView textViewRecipeServings;
        @BindView(R.id.recipes) CardView cardView;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

    public MainAdapter(List<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final String image = recipeList.get(position).getSteps().get(recipeList.get(position).getSteps().size() - 1).getVideoURL();

        Log.d(TAG, "recipeVideoURL:" + recipeList.get(position).getSteps().get(recipeList.get(position).getSteps().size() - 1).getVideoURL());

        //https://stackoverflow.com/questions/45938741/android-glide-in-the-recyclerview
        //https://stackoverflow.com/questions/46349657/difference-diskcachestrategy-in-glide-v4
        //https://futurestud.io/tutorials/glide-caching-basics
        //https://bumptech.github.io/glide/doc/caching.html
        //https://bumptech.github.io/glide/javadocs/400/com/bumptech/glide/request/RequestOptions.html

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(Uri.parse(image))
                .into(holder.image);

        TextView textViewRecipeId = holder.textViewRecipeId;
        TextView textViewRecipeName = holder.textViewRecipeName;
        TextView textViewRecipeServings = holder.textViewRecipeServings;

        //https://stackoverflow.com/questions/8327235/how-to-set-text-an-integer-and-get-int-without-getting-error

        textViewRecipeId.setText(String.valueOf(recipeList.get(position).getId()));

        textViewRecipeName.setText(recipeList.get(position).getName());

        //https://stackoverflow.com/questions/15191092/android-content-res-resourcesnotfoundexception-string-resource-id-fatal-except

        textViewRecipeServings.setText(String.valueOf(recipeList.get(position).getServings()));

        //https://stackoverflow.com/questions/27081787/onclicklistener-for-cardview

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, IngredientsStepsActivity.class);

                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("recipe", recipe);
                intent.putExtra("recipeName", recipeList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("recipeId", recipeList.get(holder.getAdapterPosition()).getId());
                intent.putParcelableArrayListExtra("recipeList", new ArrayList<Parcelable>(recipeList.get(holder.getAdapterPosition()).getId()));
                intent.putParcelableArrayListExtra("ingredientsList", new ArrayList<Parcelable>(recipeList.get(holder.getAdapterPosition()).getIngredients()));
                intent.putParcelableArrayListExtra("stepsList", new ArrayList<Parcelable>(recipeList.get(holder.getAdapterPosition()).getSteps()));

                context.startActivity(intent);

                Log.d(TAG, "recipeId:" + recipeList.get(position).getId());
                Log.d(TAG, "recipeName:" + recipeList.get(position).getName());
                Log.d(TAG, "recipeServings:" + recipeList.get(position).getServings());
                Log.d(TAG, "recipeImage:" + recipeList.get(position).getImage());

                Toast.makeText(holder.context, "Recipe " + recipeList.get(position).getName() + " " + "selected",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipeList != null) {
            return recipeList.size();
        }
        else {
            return 0;
        }
    }
}
