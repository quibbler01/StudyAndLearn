package com.example.assistantwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.example.assistantwidget.adapter.RemoteSuggestListService
import com.example.assistantwidget.utis.openApp

/**
 * Implementation of App Widget functionality.
 */
class AppSuggestWidget : AppWidgetProvider() {

    companion object {
        const val TAG = "TAG_AppSuggestWidget"

        const val CLICK_ACTION = "com.quibbler.click"

        const val ACTION_OPEN_APP = "com.quibbler.click.openapp"

        const val ACTION_UPDATE = "com.quibbler.click.update"

        const val PACKAGE_NAME = "PACKAGE_NAME"

        const val APPWIDGET_ID = "app_widget_id"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        Log.d(TAG, "action:$action")

        super.onReceive(context, intent)

        when (action) {
            ACTION_OPEN_APP -> {
                val extras = intent?.extras
                val pkg = extras?.getString(PACKAGE_NAME)
                val pos = extras?.getInt("position", -1)
                Log.d(TAG, "$context , open app :$pkg , pos:$pos")
                pkg?.let {
                    openApp(context, pkg)
                }
            }

            ACTION_UPDATE -> {
                val appWidgetId = intent?.extras?.getInt(APPWIDGET_ID)
                Log.d(TAG, "$context , update widget :$appWidgetId")
                val views = RemoteViews(context.packageName, R.layout.app_suggest_widget)
                views.setTextViewText(R.id.suggest_text, "widgetText ${System.currentTimeMillis()}")
                appWidgetId?.let {
                    AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
                }
            }

            else -> {

            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.app_suggest_widget)
        views.setTextViewText(R.id.suggest_text, widgetText)

        //click button to update
        val clickIntent = Intent(context, AppSuggestWidget::class.java)
        clickIntent.action = ACTION_UPDATE
        //clickIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        clickIntent.putExtra(APPWIDGET_ID, appWidgetId)
        val clickPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            clickIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.suggest_text, clickPendingIntent)

        //set ListView item click
        val templateIntent = Intent(context, AppSuggestWidget::class.java)
        templateIntent.action = ACTION_OPEN_APP
        val templatePendingIntent = PendingIntent.getBroadcast(
            context,
            200,
            templateIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setPendingIntentTemplate(R.id.suggest_list, templatePendingIntent)

        //set remote ListView data
        val dataIntent = Intent(context, RemoteSuggestListService::class.java)
        views.setRemoteAdapter(R.id.suggest_list, dataIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}
