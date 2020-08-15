package com.example.android.bakingapp.widget;

//https://developer.android.com/guide/topics/appwidgets/
//https://developer.android.com/guide/topics/appwidgets/overview
//https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/content/unit-1-expand-the-user-experience/lesson-2-app-widgets/2-1-p-app-widgets/2-1-p-app-widgets.html#alreadyknow
//https://developer.android.com/guide/topics/appwidgets/index.html
//https://developer.android.com/guide/topics/appwidgets/
//https://android.googlesource.com/platform/development/+/master/samples/StackWidget

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.IngredientsStepsActivity;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */

public class IngredientWidgetProvider extends AppWidgetProvider {

    private AppWidgetTarget appWidgetTarget;

    public int recipeId;

    public static final String TOAST_ACTION = "com.example.android.bakingapp.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.bakingapp.EXTRA_ITEM";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    //https://developer.android.com/guide/topics/appwidgets

    //@Override
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String recipeName = sharedPreferences.getString(IngredientsStepsActivity.RECIPENAME, "");
        int recipeId = sharedPreferences.getInt(IngredientsStepsActivity.RECIPEID, 0);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rv.setTextViewText(R.id.widgetRecipeName, recipeName);
        rv.setRemoteAdapter(appWidgetId, R.id.list_view, intent);
        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        rv.setEmptyView(R.id.list_view, R.id.empty_view);
        // Here we setup the a pending intent template. Individuals items of a collection
        // cannot setup their own pending intents, instead, the collection as a whole can
        // setup a pending intent template, and the individual items can set a fillInIntent
        // to create unique before on an item to item basis.
        Intent toastIntent = new Intent(context, IngredientWidgetProvider.class);
        toastIntent.setAction(IngredientWidgetProvider.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);

    }

    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientWidgetProvider.class));
        //Now update all widgets
        for (int appWidgetId : appWidgetIds) {
            IngredientWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

