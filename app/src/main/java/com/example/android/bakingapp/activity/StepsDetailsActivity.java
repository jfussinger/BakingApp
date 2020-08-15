package com.example.android.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.StepsDetailsFragment;
import com.example.android.bakingapp.idlingresource.SimpleIdlingResource;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsDetailsActivity extends AppCompatActivity {

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

    @BindView(R.id.previousStep)
    TextView textViewPreviousStep;
    @BindView(R.id.nextStep)
    TextView textViewNextStep;

    private static final String TAG = "stepsdetailsactivity";

    private int currentPosition;
    private int position;
    private String stepsShortDescription;
    private String stepsDescription;
    private String stepsVideoURL;
    private String stepsThumbnailURL;

    public List<Recipe> recipeList = new ArrayList<>();
    public ArrayList<Steps> stepsList = new ArrayList<>();
    //public List<Steps> stepsList = new ArrayList<>();
    Steps steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepsdetails);

        //mIdlingResource.setIdleState(true);

        ButterKnife.bind(this);

        if (findViewById(R.id.item_video_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }


        //https://developer.android.com/reference/android/app/Fragment

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            stepsList = bundle.getParcelableArrayList("stepsList");
            stepsShortDescription = bundle.getString(String.valueOf(R.id.stepsShortDescription), stepsList.get(position).getShortDescription());
            stepsDescription = bundle.getString(String.valueOf(R.id.stepsDescription));
            stepsVideoURL = bundle.getString(String.valueOf(R.id.stepsVideoURL), stepsList.get(position).getVideoURL());
            stepsThumbnailURL = bundle.getString(String.valueOf(R.id.stepsThumbnailURL));
            position = bundle.getInt("position", 0);

            StepsDetailsFragment fragment = StepsDetailsFragment.newInstance(
                    stepsList,
                    stepsShortDescription,
                    stepsDescription,
                    stepsVideoURL,
                    stepsThumbnailURL,
                    position);

            Bundle arguments = new Bundle();

            arguments.putParcelableArrayList("stepsList", stepsList);
            arguments.putString("stepsShortDescription", stepsShortDescription);
            arguments.putString("stepsDescription", stepsDescription);
            arguments.putString("stepsVideoURL", stepsVideoURL);
            arguments.putString("stepsThumbnailURL", stepsThumbnailURL);
            arguments.putInt("position", position);

            fragment.setArguments(arguments);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    //.addToBackStack(null)
                    .replace(R.id.flContent, fragment)
                    .commit();

            updateFragmentPosition(position);

        } else {
            stepsList = savedInstanceState.getParcelableArrayList("stepsList");
            stepsShortDescription = savedInstanceState.getString("stepsShortDescription");
            stepsDescription = savedInstanceState.getString("stepsDescription");
            stepsVideoURL = savedInstanceState.getString("stepsVideoURL");
            stepsThumbnailURL = savedInstanceState.getString("stepsThumbnailURL");
            position = savedInstanceState.getInt("position");
        }

        textViewPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = stepsList.get(position).getStepId();
                position = currentPosition - 1;

                StepsDetailsFragment fragment = StepsDetailsFragment.newInstance(
                        stepsList,
                        stepsShortDescription,
                        stepsDescription,
                        stepsVideoURL,
                        stepsThumbnailURL,
                        position);

                Bundle arguments = new Bundle();

                arguments.putParcelableArrayList("stepsList", stepsList);
                arguments.putString("stepsShortDescription", stepsShortDescription);
                arguments.putString("stepsDescription", stepsDescription);
                arguments.putString("stepsVideoURL", stepsVideoURL);
                arguments.putString("stepsThumbnailURL", stepsThumbnailURL);
                arguments.putInt("position", position);

                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                stepsShortDescription = stepsList.get(position).getShortDescription();
                textViewPreviousStep.setText(stepsShortDescription);
                //textViewNextStep.setText(stepsShortDescription);

                updateFragmentPosition(position);

                Log.d(TAG, "Previous step" + " " + stepsList.get(position).getStepId() + " "
                        + stepsList.get(position).getShortDescription() + " " + "clicked");
            }
        });

        textViewNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = stepsList.get(position).getStepId();
                position = currentPosition + 1;

                StepsDetailsFragment fragment = StepsDetailsFragment.newInstance(
                        stepsList,
                        stepsShortDescription,
                        stepsDescription,
                        stepsVideoURL,
                        stepsThumbnailURL,
                        position);

                Bundle arguments = new Bundle();

                arguments.putParcelableArrayList("stepsList", stepsList);
                arguments.putString("stepsShortDescription", stepsShortDescription);
                arguments.putString("stepsDescription", stepsDescription);
                arguments.putString("stepsVideoURL", stepsVideoURL);
                arguments.putString("stepsThumbnailURL", stepsThumbnailURL);
                arguments.putInt("position", position);

                fragment.setArguments(arguments);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                stepsShortDescription = stepsList.get(position).getShortDescription();
                textViewNextStep.setText(stepsShortDescription);

                updateFragmentPosition(position);

                Log.d(TAG, "Next step" + " " + stepsList.get(position).getStepId() + " "
                        + stepsList.get(position).getShortDescription() + " " + "clicked");
            }
        });

    }

    public void updateFragmentPosition (int position) {

        //https://stackoverflow.com/questions/48774933/exoplayer-not-restoring-from-the-position-it-has-already-played-when-rotated

        if (mTwoPane) {
            textViewPreviousStep.setVisibility(View.GONE);
            textViewNextStep.setVisibility(View.GONE);
        } else {

            if (stepsList.get(position).getStepId() == 0) {
                textViewPreviousStep.setVisibility(View.GONE);
                textViewNextStep.setVisibility(View.VISIBLE);
                textViewNextStep.setText(stepsList.get(position + 1).getShortDescription());
            } else if (stepsList.get(position).getStepId() == stepsList.size()) {
                textViewPreviousStep.setVisibility(View.VISIBLE);
                textViewNextStep.setVisibility(View.GONE);
                textViewPreviousStep.setText(stepsList.get(position - 1).getShortDescription());
            } else if (stepsList.get(position).getStepId() == stepsList.size() - 1) {
                textViewPreviousStep.setVisibility(View.VISIBLE);
                textViewNextStep.setVisibility(View.GONE);
                textViewPreviousStep.setText(stepsList.get(position - 1).getShortDescription());
            } else {
                textViewPreviousStep.setVisibility(View.VISIBLE);
                textViewNextStep.setVisibility(View.VISIBLE);
                textViewNextStep.setText(stepsList.get(position + 1).getShortDescription());
                textViewPreviousStep.setText(stepsList.get(position - 1).getShortDescription());
            }

        }

    }

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList("stepsList", stepsList);
        savedInstanceState.putString("stepsShortDescription", stepsShortDescription);
        savedInstanceState.putString("stepsDescription", stepsDescription);
        savedInstanceState.putString("stepsVideoURL", stepsVideoURL);
        savedInstanceState.putString("stepsThumbnailURL", stepsThumbnailURL);
        savedInstanceState.putInt("position", position);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state

        if (savedInstanceState != null) {

            stepsList = savedInstanceState.getParcelableArrayList("stepsList");
            stepsShortDescription = savedInstanceState.getString("stepsShortDescription");
            stepsDescription = savedInstanceState.getString("stepsDescription");
            stepsVideoURL = savedInstanceState.getString("stepsVideoURL");
            stepsThumbnailURL = savedInstanceState.getString("stepsThumbnailURL");
            position = savedInstanceState.getInt("position");;

        }
    }

}