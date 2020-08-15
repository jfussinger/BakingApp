package com.example.android.bakingapp.roomdatabase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
//http://thetechnocafe.com/how-to-use-room-in-android-all-you-need-to-know-to-get-started/

@Entity(tableName = "IngredientTable")
public class IngredientEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public Double quantity;
    public String measure;
    public String ingredient;

    public IngredientEntry(int id, Double quantity, String measure, String ingredient) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

}


