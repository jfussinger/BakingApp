package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private static final String TAG = "stepsadapter";

    public List<Steps> stepsList;
    public List<Recipe> recipeList;

    public int rowLayout;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stepsId)
        TextView textViewStepsId;
        @BindView(R.id.stepsShortDescription)
        TextView textViewStepsShortDescription;
        @BindView(R.id.steps)
        LinearLayout linearLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

    public StepsAdapter(List<Steps> stepsList, Context context) {
        this.stepsList = stepsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public StepsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepsAdapter.MyViewHolder holder, final int position) {

        TextView textViewStepsId = holder.textViewStepsId;
        TextView textViewStepsShortDescription = holder.textViewStepsShortDescription;

        textViewStepsId.setText(String.valueOf(stepsList.get(position).getStepId()));
        textViewStepsShortDescription.setText(stepsList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        if (stepsList != null) {
            return stepsList.size();
        }
        else {
            return 0;
        }
    }
}