package com.example.android.bakingapp.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsDetailsFragment extends Fragment {

    private boolean mTwoPane;

    @BindView(R.id.stepsVideoURL)
    TextView textViewStepsVideoURL;
    @BindView(R.id.stepsThumbnailURL)
    ImageView imageViewStepsThumbnailURL;
    @BindView(R.id.stepsId)
    TextView textViewStepsId;
    @BindView(R.id.stepsShortDescription)
    TextView textViewStepsShortDescription;
    @BindView(R.id.stepsDescription)
    TextView textViewStepsDescription;
    @BindView(R.id.video_view) PlayerView playerView;

    SimpleExoPlayer player;

    int step_id;

    String stepsVideoURL;
    String stepsThumbnailURL;
    String stepsId;
    String stepsShortDescription;
    String stepsDescription;

    private int position;

    private static final String TAG = "stepdetailsfragment";

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    //private long CurrentPosition = 0;
    private long CurrentPosition;

    private long playbackPosition;
    private int currentWindow;

    public ArrayList<Recipe> recipeList = new ArrayList<>();
    public ArrayList<Steps> stepsList = new ArrayList<>();
    //public List<Steps> stepsList = new ArrayList<>();

    public StepsDetailsFragment() {
        // Required empty public constructor
    }

    public static StepsDetailsFragment newInstance(
            ArrayList<Steps> stepsList,
            String stepsShortDescription,
            String stepsDescription,
            String stepsVideoURL,
            String stepsThumbnailURL,
            int position) {

        StepsDetailsFragment fragment = new StepsDetailsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("stepsList", stepsList);
        bundle.putString("stepsShortDescription", stepsShortDescription);
        bundle.putString("stepsDescription", stepsDescription);
        bundle.putString("stepsVideoURL", stepsVideoURL);
        bundle.putString("stepsThumbnailURL", stepsThumbnailURL);
        bundle.putInt("position", position);

        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //https://stackoverflow.com/questions/28332605/unreachable-statement-after-using-getactivity-in-a-fragment

        final View view = inflater.inflate(R.layout.fragment_stepdetails, container, false);

        ButterKnife.bind(this, view);

        if (getActivity().findViewById(R.id.item_video_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        if (getArguments() != null) {

            stepsList = getArguments().getParcelableArrayList("stepsList");
            stepsShortDescription = getArguments().getString("stepsShortDescription");
            stepsDescription = getArguments().getString("stepsDescription");
            stepsVideoURL = getArguments().getString("stepsVideoURL");
            stepsThumbnailURL = getArguments().getString("stepsThumbnailURL");
            position = getArguments().getInt("position");

            stepsId = Integer.toString(stepsList.get(position).getStepId());
            stepsShortDescription = stepsList.get(position).getShortDescription();
            stepsDescription = stepsList.get(position).getDescription();

            Log.d(TAG, "stepId:" + stepsList.get(position).getStepId());
            Log.d(TAG, "stepShortDescription:" + stepsList.get(position).getShortDescription());
            Log.d(TAG, "stepDescription:" + stepsList.get(position).getDescription());
            Log.d(TAG, "stepVideoURL:" + stepsList.get(position).getVideoURL());
            Log.d(TAG, "stepThumbnailURL:" + stepsList.get(position).getThumbnailURL());

            //https://stackoverflow.com/questions/36461022/settitle-doesnt-work-in-fragment

            getActivity().setTitle(stepsShortDescription);

            step_id = stepsList.get(position).getStepId();

            if (stepsList.get(position).getThumbnailURL() != null ||
                    stepsList.get(position).getThumbnailURL().equalsIgnoreCase("")) {

                stepsThumbnailURL = stepsList.get(position).getThumbnailURL();

                //https://stackoverflow.com/questions/46349657/difference-diskcachestrategy-in-glide-v4
                //https://futurestud.io/tutorials/glide-caching-basics

                Glide.with(this)
                        .load(stepsThumbnailURL)
                        //.asBitmap()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewStepsThumbnailURL);
                imageViewStepsThumbnailURL.setVisibility(View.VISIBLE);

            }
            if (stepsList.get(position).getVideoURL() != null
                    || stepsList.get(position).getVideoURL().equalsIgnoreCase("")) {

                stepsVideoURL = stepsList.get(position).getVideoURL();

                initializePlayer();

            }

            textViewStepsId.setText(String.valueOf(stepsList.get(position).getStepId()));
            textViewStepsShortDescription.setText(stepsList.get(position).getShortDescription());
            textViewStepsDescription.setText(stepsList.get(position).getDescription());
            textViewStepsVideoURL.setText(stepsList.get(position).getVideoURL());

            }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();

        window = new Timeline.Window();

        //setRetainInstance(true);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("stepsList", stepsList);
        savedInstanceState.putString("stepsShortDescription" , stepsShortDescription);
        savedInstanceState.putString("stepsDescription", stepsDescription);
        savedInstanceState.putString("stepsVideoURL", stepsVideoURL);
        savedInstanceState.putString("stepsThumbnailURL", stepsThumbnailURL);
        savedInstanceState.putInt("position", position);

        savedInstanceState.putLong("playbackPosition", playbackPosition);

        if (stepsVideoURL.equals("")){
            playerView.setVisibility(View.GONE);
        } else {
            CurrentPosition = player.getCurrentPosition();
        }
        //if (player != null) {
            //CurrentPosition = player.getCurrentPosition();
        //}
        //CurrentPosition = player.getCurrentPosition();
        savedInstanceState.putLong("currentPosition", CurrentPosition);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            stepsList = savedInstanceState.getParcelableArrayList("stepsList");
            stepsShortDescription = savedInstanceState.getString("stepsShortDescription");
            stepsDescription = savedInstanceState.getString("stepsDescription");
            stepsVideoURL = savedInstanceState.getString("stepsVideoURL");
            stepsThumbnailURL = savedInstanceState.getString("stepsThumbnailURL");
            position = savedInstanceState.getInt("position");

            playbackPosition = savedInstanceState.getLong("playbackPosition");
            CurrentPosition = savedInstanceState.getLong("currentPosition");

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initializePlayer() {

        if (stepsVideoURL.equals("")){
            playerView.setVisibility(View.GONE);
        } else {
            if (player == null )

            playerView.requestFocus();

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            playerView.setPlayer(player);

            //https://stackoverflow.com/questions/50980672/exoplayer-mediasource-created-video-player-not-playing-the-video-extracted-from

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), null);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(stepsVideoURL));

            player.prepare(mediaSource, true, false);

            player.seekTo(currentWindow, playbackPosition);

            player.setPlayWhenReady(shouldAutoPlay);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();

            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If is tablet
            if (!mTwoPane) {

                //https://stackoverflow.com/questions/10045360/getting-classcastexception-when-trying-to-insert-relativelayout-dyanmically/10046252#10046252

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);
                hideSystemUi();

                textViewStepsVideoURL.setVisibility(View.GONE);
                imageViewStepsThumbnailURL.setVisibility(View.GONE);
                textViewStepsId.setVisibility(View.VISIBLE);
                textViewStepsShortDescription.setVisibility(View.VISIBLE);
                textViewStepsDescription.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (player != null) {
                CurrentPosition = player.getCurrentPosition();
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    //https://stackoverflow.com/questions/46713761/how-to-play-video-full-screen-in-landscape-using-exoplayer

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerView.setLayoutParams(params);

            hideSystemUi();

            textViewStepsVideoURL.setVisibility(View.GONE);
            imageViewStepsThumbnailURL.setVisibility(View.GONE);
            textViewStepsId.setVisibility(View.VISIBLE);
            textViewStepsShortDescription.setVisibility(View.VISIBLE);
            textViewStepsDescription.setVisibility(View.VISIBLE);

            //https://stackoverflow.com/questions/18320713/getsupportactionbar-from-inside-of-fragment-actionbarcompat

            //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.WRAP_CONTENT;
            //params.height=600;
            playerView.setLayoutParams(params);

            //((AppCompatActivity)getActivity()).getSupportActionBar().show();

            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}