package com.example.muhammedfayed.publishandsell.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.UI.MainActivity;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class Widget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            Intent Intent = new Intent(context, Wservice.class);
            Intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            Intent.setData(Uri.parse(Intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.listview, Intent);

            Intent clickIntent = new Intent(context, MainActivity.class);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.listview, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.listview);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

}
