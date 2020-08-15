package com.example.android.bakingapp.roomdatabase;

//https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0
//https://github.com/googlecodelabs/android-room-with-a-view
//https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("select * from IngredientTable")
    LiveData<List<IngredientEntry>> getAllIngredients();

    @Query("select * from IngredientTable where id = :id")
    IngredientEntry getItembyId(Integer id);

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //void addIngredient(IngredientEntry ingredient);

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(IngredientEntry ingredient);

    @Delete
    void deleteIngredient(IngredientEntry ingredient);

    @Query("DELETE FROM IngredientTable")
    void deleteAll();

}

