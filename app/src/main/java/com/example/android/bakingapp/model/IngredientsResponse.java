package com.example.android.bakingapp.model;

////https://github.com/hajiyevelnur92/retrofit-with-food2fork.com/blob/master/app/src/main/java/codehive/foodrecept/models/view/ItemResponse.java

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IngredientsResponse implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("count")
    private int count;
    @SerializedName("ingredients")
    private List<Ingredients> results;

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

    public List<Ingredients> getResults() {
        return results;
    }

    public void setResults(List<Ingredients> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.results);
    }

    public IngredientsResponse() {
    }

    protected IngredientsResponse(Parcel in) {
        this.id = in.readInt();
        this.results = in.createTypedArrayList(Ingredients.CREATOR);
    }

    public static final Parcelable.Creator<IngredientsResponse> CREATOR = new Parcelable.Creator<IngredientsResponse>() {
        @Override
        public IngredientsResponse createFromParcel(Parcel source) {
            return new IngredientsResponse(source);
        }

        @Override
        public IngredientsResponse[] newArray(int size) {
            return new IngredientsResponse[size];
        }
    };
}