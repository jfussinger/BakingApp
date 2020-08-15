package com.example.android.bakingapp.activity;

//https://developer.android.com/guide/topics/ui/layout/cardview
//https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
//http://jakewharton.github.io/butterknife/

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.MainAdapter;
import com.example.android.bakingapp.apiservice.APIService;
import com.example.android.bakingapp.apiservice.Retrofit2;
import com.example.android.bakingapp.idlingresource.SimpleIdlingResource;
import com.example.android.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//https://stackoverflow.com/questions/14184182/why-wont-fragment-retain-state-when-screen-is-rotated
//https://stackoverflow.com/questions/10126845/handle-screen-rotation-without-losing-data-android

public class MainActivity extends AppCompatActivity {

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    private static final String TAG = "mainactivity";

    private CardView recipesCardView;

    Recipe recipes;

    private List<Recipe> recipeList = new ArrayList<>();

    private MainAdapter recipesAdapter;

    private RecyclerView recipeRecyclerView;

    private Parcelable savedRecipeRecyclerLayoutState;
    private static final String BUNDLE_RECIPE_RECYCLER_VIEW = "bundleRecipeRecyclerView";

    public void onLoadRecipes() {

        //https://medium.com/@jacinth9/android-retrofit-2-0-tutorial-89de3c714c63
        //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit

        recipeRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recipeRecyclerView.setHasFixedSize(true);
        recipesAdapter = new MainAdapter(recipeList, getApplicationContext());

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recipeRecyclerView.setAdapter(recipesAdapter);

        //https://gist.github.com/7ares/d882e9236f3c8bd80fc86fbac53ee33c

        mIdlingResource.setIdleState(false);

        APIService apiService = Retrofit2.retrofit.create(APIService.class);

        //https://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array

        Call<List<Recipe>> callRecipe = apiService.getRecipes();
        callRecipe.enqueue(new Callback<List <Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                List<Recipe> recipeList = response.body();
                if (recipeList != null) {
                    recipesAdapter.notifyDataSetChanged();
                    recipeRecyclerView.setAdapter(new MainAdapter(recipeList, getApplicationContext()));
                }

                if (recipeList.size()>0) {Log.d(TAG, "Number of recipes received: " + recipeList.size());
                    Log.d(TAG, "recipeId:" + recipeList.get(0).getId());
                    Log.d(TAG, "recipeName:" + recipeList.get(0).getName());
                    Log.d(TAG, "recipeServings:" + recipeList.get(0).getServings());
                    Log.d(TAG, "recipeImage:" + recipeList.get(0).getImage());
                }
                else {
                    Toast.makeText(getApplicationContext(), "No recipes found", Toast.LENGTH_SHORT).show();
                }

                //https://gist.github.com/7ares/d882e9236f3c8bd80fc86fbac53ee33c

                mIdlingResource.setIdleState(true);
            }

            @Override
            public void onFailure(Call<List <Recipe>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());

                //https://gist.github.com/7ares/d882e9236f3c8bd80fc86fbac53ee33c

                //mIdlingResource.setIdleState(true);
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the IdlingResource instance
        getIdlingResource();

        recipeRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recipeRecyclerView.setHasFixedSize(true);
        recipesAdapter = new MainAdapter(recipeList, getApplicationContext());

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recipeRecyclerView.setAdapter(recipesAdapter);

        if(savedInstanceState != null) {
            recipes = savedInstanceState.getParcelable("recipes");
        }

        if (!isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {

            onLoadRecipes();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("recipes", recipes);
        savedInstanceState.putParcelable(BUNDLE_RECIPE_RECYCLER_VIEW, recipeRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

        if (savedInstanceState != null) {

            recipes = savedInstanceState.getParcelable("recipes");
            savedRecipeRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECIPE_RECYCLER_VIEW);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //https://stackoverflow.com/questions/9570237/android-check-internet-connection

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
