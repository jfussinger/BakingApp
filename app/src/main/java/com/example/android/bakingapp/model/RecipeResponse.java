package com.example.android.bakingapp.model;

//https://github.com/hajiyevelnur92/retrofit-with-food2fork.com/blob/master/app/src/main/java/codehive/foodrecept/models/view/ItemResponse.java

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeResponse implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("count")
    private int count;
    @SerializedName("recipes")
    private List<Recipe> recipeList;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Recipe> getRecipes() {
        return recipeList;
    }

    public void setRecipes(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.recipeList);
    }

    public RecipeResponse() {
    }

    protected RecipeResponse(Parcel in) {
        this.id = in.readInt();
        this.recipeList = in.createTypedArrayList(Recipe.CREATOR);
    }

    public static final Parcelable.Creator<RecipeResponse> CREATOR = new Parcelable.Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel source) {
            return new RecipeResponse(source);
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };
}