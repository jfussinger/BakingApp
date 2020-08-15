package com.example.android.bakingapp.model;

//https://github.com/hajiyevelnur92/retrofit-with-food2fork.com/blob/master/app/src/main/java/codehive/foodrecept/models/view/ItemResponse.java

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StepsResponse implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("count")
    private int count;
    @SerializedName("steps")
    private List<Steps> results;

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

    public List<Steps> getResults() {
        return results;
    }

    public void setResults(List<Steps> results) {
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

    public StepsResponse() {
    }

    protected StepsResponse(Parcel in) {
        this.id = in.readInt();
        this.results = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Parcelable.Creator<StepsResponse> CREATOR = new Parcelable.Creator<StepsResponse>() {
        @Override
        public StepsResponse createFromParcel(Parcel source) {
            return new StepsResponse(source);
        }

        @Override
        public StepsResponse[] newArray(int size) {
            return new StepsResponse[size];
        }
    };
}

