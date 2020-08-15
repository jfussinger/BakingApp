package com.example.android.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.IngredientsAdapter;
import com.example.android.bakingapp.adapter.StepsAdapter;
import com.example.android.bakingapp.fragments.StepsDetailsFragment;
import com.example.android.bakingapp.idlingresource.SimpleIdlingResource;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;
import com.example.android.bakingapp.roomdatabase.IngredientViewModel;
import com.example.android.bakingapp.widget.IngredientWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class IngredientsStepsActivity extends AppCompatActivity {

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

    private boolean mTwoPane;
    private IngredientsStepsActivity mParentActivity;

    private static final String LOG_TAG = IngredientsStepsActivity.class.getSimpleName();

    private static final String TAG = "ingredients_steps";

    private Gson gson;

    private int position;
    private int recipeId;
    private String recipeName;
    private String stepsId;
    private String stepsShortDescription;
    private String stepsDescription;
    private String stepsVideoURL;
    private String stepsThumbnailURL;

    public ArrayList<Recipe> recipeList = new ArrayList<>();
    public Recipe recipe;
    public Ingredients ingredients;
    public Steps steps;
    //public List<Ingredients> ingredientsList;
    public ArrayList<Ingredients> ingredientsList;
    private ArrayList<Steps> stepsList;
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private RecyclerView ingredientsRecyclerView;
    private RecyclerView stepsRecyclerView;

    private LinearLayoutManager linearLayoutManager;

    private Parcelable savedIngredientsRecyclerLayoutState;
    private static final String BUNDLE_INGREDIENTS_RECYCLER_VIEW = "bundleIngredientsRecyclerView";
    private Parcelable savedStepsRecyclerLayoutState;
    private static final String BUNDLE_STEPS_RECYCLER_VIEW = "bundleStepsRecyclerView";

    public static final String RECIPEID = "recipeId";
    public static final String RECIPENAME = "recipeName";

    private IngredientViewModel mIngredientViewModel;

    private StepsDetailsFragment stepsDetailsFragment;

    //https://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Log.d(TAG, "onCreate Lifecycle invoked");

        setContentView(R.layout.activity_ingredients_steps);

        final Intent intent = getIntent();

        //https://github.com/udacity/Android_Me/blob/TFragments.07-Solution-TwoPaneUI/app/src/main/java/com/example/android/android_me/ui/MainActivity.java
        //https://developer.android.com/training/multiscreen/screensizes

        if (findViewById(R.id.item_video_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        if (intent != null && intent.hasExtra("ingredientsList") && intent.hasExtra("position")) {

            position = intent.getIntExtra("position", 0);
            recipe = getIntent().getParcelableExtra("recipe");
            recipeName = getIntent().getStringExtra("recipeName");
            recipeId = intent.getIntExtra("recipeId", position);
            recipeList = getIntent().getParcelableArrayListExtra("recipeList");
            ingredientsList = getIntent().getParcelableArrayListExtra("ingredientsList");

            //https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar/8852749

            this.setTitle(recipeName);

        } else {
            Toast.makeText(this, "Ingredients not found", Toast.LENGTH_SHORT).show();
        }

        if (intent != null && intent.hasExtra("stepsList") && intent.hasExtra("position")) {

            position = intent.getIntExtra("position", 0);
            recipe = getIntent().getParcelableExtra("recipe");
            recipeName = getIntent().getStringExtra("recipeName");
            recipeId = intent.getIntExtra("recipeId", position);
            recipeList = getIntent().getParcelableArrayListExtra("recipeList");
            stepsList = getIntent().getParcelableArrayListExtra("stepsList");

        } else {
            Toast.makeText(this, "Steps not found", Toast.LENGTH_SHORT).show();
        }

        ingredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsAdapter = new IngredientsAdapter(ingredientsList, getApplicationContext());

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        ingredientsRecyclerView.setNestedScrollingEnabled(false);

        stepsRecyclerView = (RecyclerView) findViewById(R.id.steps_recycler_view);
        stepsRecyclerView.setHasFixedSize(true);
        stepsAdapter = new StepsAdapter(stepsList, getApplicationContext());

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stepsRecyclerView.setAdapter(stepsAdapter);

        stepsRecyclerView.setNestedScrollingEnabled(false);

        if (savedInstanceState != null) {

            recipeName = savedInstanceState.getString("recipeName");
            recipeId = savedInstanceState.getInt("recipeId", position);
            recipeList = savedInstanceState.getParcelableArrayList("recipeList");
            //ingredientsList = recipe.getIngredients();
            stepsList = savedInstanceState.getParcelableArrayList("stepsList");
            position = savedInstanceState.getInt("position");
            savedIngredientsRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_INGREDIENTS_RECYCLER_VIEW);
            savedStepsRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_STEPS_RECYCLER_VIEW);

        } else {

            onLoadIngredients();
            onLoadSteps();

            //if (mTwoPane) {

            //stepsDetailsFragment = StepsDetailsFragment.newInstance(stepsList,
            //steps,
            //stepsShortDescription,
            //stepsDescription,
            //stepsVideoURL,
            //stepsThumbnailURL,
            //0);
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().add(R.id.item_video_container,
            //stepsDetailsFragment).commit();

            //}

        }

        //if (mTwoPane) {

                //stepsDetailsFragment = StepsDetailsFragment.newInstance(stepsList,
                        //steps,
                        //stepsShortDescription,
                        //stepsDescription,
                        //stepsVideoURL,
                        //stepsThumbnailURL,
                        //0);
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.beginTransaction().add(R.id.item_video_container,
                        //stepsDetailsFragment).commit();

        //}

    }

    //https://developer.android.com/guide/topics/ui/menus#java

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_widget) {

            saveIngredients();

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void saveIngredients() {

        Toast.makeText(IngredientsStepsActivity.this, getString(R.string.recipe_added_to_widget), Toast.LENGTH_LONG).show();

        //https://developer.android.com/guide/topics/appwidgets/overview
        //https://developer.android.com/guide/topics/appwidgets
        //https://developer.android.com/training/data-storage/shared-preferences
        //https://medium.com/@evancheese1/shared-preferences-saving-arraylists-and-more-with-json-and-gson-java-5d899c8b0235

        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(ingredientsList);

        editor.putString(RECIPENAME, recipeName);
        editor.putInt(RECIPEID, recipeId);
        editor.putString("ingredientsList", json);
        editor.apply();

        IngredientWidgetProvider.updateWidget(this);

        Log.d(TAG, "RecipeList" + recipeList);
        Log.d(TAG, "Widget Name:" + " " + recipeName);
        Log.d(TAG, "Widget Id:" + " " + recipeId);
        Log.d(TAG, "IngredientsList" + ingredientsList);
        Log.d(TAG, "Widget Ingredients:" + " " +  (String.valueOf(ingredientsList.get(0).getQuantity())) + " " +
                ingredientsList.get(0).getMeasure() + " "+
                ingredientsList.get(0).getIngredient());

    }

    //https://developer.android.com/guide/components/activities/activity-lifecycle

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Lifecycle invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Lifecycle invoked");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Lifecycle invoked");
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        Log.d(TAG, "onStop Lifecycle invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy Lifecycle invoked");
    }

    public void onLoadIngredients (){

        ingredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsAdapter = new IngredientsAdapter(ingredientsList, getApplicationContext());

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        ingredientsRecyclerView.setNestedScrollingEnabled(false);

        //mIdlingResource.setIdleState(true);

    }

    public void onLoadSteps (){

        stepsRecyclerView = (RecyclerView) findViewById(R.id.steps_recycler_view);
        stepsRecyclerView.setHasFixedSize(true);
        stepsAdapter = new StepsAdapter(stepsList, getApplicationContext());

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stepsRecyclerView.setAdapter(stepsAdapter);

        stepsRecyclerView.setNestedScrollingEnabled(false);

        stepsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                stepsRecyclerView, new RecyclerTouchListener.ClickListener()
        {

            @Override
            public void onClick(View view, int position) {

                stepsId = String.valueOf(stepsList.get(position).getStepId());
                stepsShortDescription = String.valueOf(stepsList.get(position).getShortDescription());
                stepsDescription = String.valueOf(stepsList.get(position).getDescription());
                stepsVideoURL = String.valueOf(stepsList.get(position).getVideoURL());
                stepsThumbnailURL = String.valueOf(stepsList.get(position).getThumbnailURL());

                if (mTwoPane) {

                    stepsDetailsFragment = StepsDetailsFragment.newInstance(stepsList,
                            stepsShortDescription,
                            stepsDescription,
                            stepsVideoURL,
                            stepsThumbnailURL,
                            position);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.item_video_container,
                            stepsDetailsFragment).commit();

                    Log.d(TAG, "Is Two Pane:");
                }

                else {

                    Intent intent = new Intent(IngredientsStepsActivity.this, StepsDetailsActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("stepsList", stepsList);
                    bundle.putString("stepsShortDescription", stepsShortDescription);
                    bundle.putString("stepsDescription", stepsDescription);
                    bundle.putString("stepsVideoURL", stepsVideoURL);
                    bundle.putString("stepsThumbnailURL", stepsThumbnailURL);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.d(TAG, "StepsDetailsActivity started:");

                }

                Log.d(TAG, "stepId:" + stepsList.get(position).getStepId());
                Log.d(TAG, "stepShortDescription:" + stepsList.get(position).getShortDescription());
                Log.d(TAG, "stepDescription:" + stepsList.get(position).getDescription());
                Log.d(TAG, "stepVideoURL:" + stepsList.get(position).getVideoURL());
                Log.d(TAG, "stepThumbnailURL:" + stepsList.get(position).getThumbnailURL());

                Toast.makeText(getApplicationContext(), "Step " + stepsList.get(position).getStepId() + " " + stepsList.get(position).getShortDescription() + " selected",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //mIdlingResource.setIdleState(true);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("recipeName", recipeName);
        savedInstanceState.putInt("recipeId", position);
        savedInstanceState.putParcelableArrayList("recipeList", recipeList);
        savedInstanceState.putParcelableArrayList("ingredientsList", ingredientsList);
        savedInstanceState.putParcelableArrayList("stepsList", stepsList);
        savedInstanceState.putInt("position", position);

        savedInstanceState.putParcelable(BUNDLE_INGREDIENTS_RECYCLER_VIEW, ingredientsRecyclerView.getLayoutManager().onSaveInstanceState());
        savedInstanceState.putParcelable(BUNDLE_STEPS_RECYCLER_VIEW, stepsRecyclerView.getLayoutManager().onSaveInstanceState());
        //outState.putIntArray("ARTICLE_SCROLL_POSITION",
        //new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

        if (savedInstanceState != null) {
            recipeName = savedInstanceState.getString("recipeName");
            recipeId = savedInstanceState.getInt("recipeId", position);
            recipeList = savedInstanceState.getParcelableArrayList("recipeList");
            //ingredientsList = recipeList.get(0).getIngredients();
            ingredientsList = savedInstanceState.getParcelableArrayList("ingredientsList");
            stepsList = savedInstanceState.getParcelableArrayList("stepsList");
            position = savedInstanceState.getInt("position", 0);

            savedIngredientsRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_INGREDIENTS_RECYCLER_VIEW);
            savedStepsRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_STEPS_RECYCLER_VIEW);

        }
    }
}
