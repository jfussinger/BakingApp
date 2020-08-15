package com.example.android.bakingapp.roomdatabase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class IngredientRepository {

    private IngredientDao mIngredientDao;
    private LiveData<List<IngredientEntry>> mAllIngredients;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    IngredientRepository(Application application) {
        IngredientRoomDatabase db = IngredientRoomDatabase.getDatabase(application);
        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllIngredients();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<IngredientEntry>> getAllIngredients() {
        return mAllIngredients;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (IngredientEntry ingredient) {
        new insertAsyncTask(mIngredientDao).execute(ingredient);
    }

    private static class insertAsyncTask extends AsyncTask<IngredientEntry, Void, Void> {

        private IngredientDao mAsyncTaskDao;

        insertAsyncTask(IngredientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final IngredientEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}

