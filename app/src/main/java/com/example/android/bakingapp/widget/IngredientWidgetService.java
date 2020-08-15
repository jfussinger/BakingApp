package com.example.android.bakingapp.widget;

//https://developer.android.com/guide/topics/appwidgets/
//https://developer.android.com/guide/topics/appwidgets/overview
//https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/content/unit-1-expand-the-user-experience/lesson-2-app-widgets/2-1-p-app-widgets/2-1-p-app-widgets.html#alreadyknow
//https://developer.android.com/guide/topics/appwidgets/index.html
//https://developer.android.com/guide/topics/appwidgets/
//https://android.googlesource.com/platform/development/+/master/samples/StackWidget

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

    class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Gson gson;
        public List<Ingredients> ingredientsList;
        private Context mContext;
        private int mAppWidgetId;
        public IngredientRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
            // for example downloading or creating content etc, should be deferred to onDataSetChanged()
            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

            // We sleep for 3 seconds here to show how the empty view appears in the interim.
            // The empty view is set in the StackWidgetProvider and should be a sibling of the
            // collection view.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onDestroy() {
            // In onDestroy() you should tear down anything that was setup for your data source,
            // eg. cursors, connections, etc.

            ingredientsList.clear();

        }

        @Override
        public int getCount() {

            if (ingredientsList == null) return 0;
            else return ingredientsList.size();

        }

        @Override
        public RemoteViews getViewAt(int i) {

            // position will always range from 0 to getCount() - 1.
            // We construct a remote views item based on our widget item xml file, and set the
            // text based on the position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

            rv.setTextViewText(R.id.widgetIngredientsQuantity, String.valueOf(ingredientsList.get(i).getQuantity()));
            rv.setTextViewText(R.id.widgetIngredientsMeasure, ingredientsList.get(i).getMeasure());
            rv.setTextViewText(R.id.widgetIngredientsIngredient, ingredientsList.get(i).getIngredient());

            // Next, we set a fill-intent which will be used to fill-in the pending intent template
            // which is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(IngredientWidgetProvider.EXTRA_ITEM, i);

            // You can do heaving lifting in here, synchronously. For example, if you need to
            // process an image, fetch something from the network, etc., it is ok to do it here,
            // synchronously. A loading view will show up in lieu of the actual contents in the
            // interim.
            try {
                System.out.println("Loading view " + i);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Return the remote views object.
            return rv;

        }

        public RemoteViews getLoadingView() {
            // You can create a custom loading view (for instance when getViewAt() is slow.) If you
            // return null here, you will get the default loading view.
            return null;
        }
        public int getViewTypeCount() {
            return 1;
        }
        public long getItemId(int i) {
            return 0;
        }
        public boolean hasStableIds() {
            return true;
        }
        public void onDataSetChanged() {
            // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
            // on the collection view corresponding to this factory. You can do heaving lifting in
            // here, synchronously. For example, if you need to process an image, fetch something
            // from the network, etc., it is ok to do it here, synchronously. The widget will remain
            // in its current state while work is being done here, so you don't need to worry about
            // locking up the widget.

            //https://codinginflow.com/tutorials/android/save-arraylist-to-sharedpreferences-with-gson
            //https://www.youtube.com/watch?v=jcliHGR3CHo&feature=youtu.be
            //https://developer.android.com/training/data-storage/shared-preferences#java

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("ingredientsList", "");
            Type type = new TypeToken<List<Ingredients>>() {}.getType();
            ingredientsList = gson.fromJson(json, type);

            if (ingredientsList == null) {
                ingredientsList = new ArrayList<>();
            }

        }
}

