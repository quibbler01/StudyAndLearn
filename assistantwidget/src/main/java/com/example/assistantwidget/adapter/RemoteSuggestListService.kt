package com.example.assistantwidget.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.assistantwidget.AppSuggestWidget.Companion.CLICK_ACTION
import com.example.assistantwidget.AppSuggestWidget.Companion.PACKAGE_NAME
import com.example.assistantwidget.R

class RemoteSuggestListService : RemoteViewsService() {

    companion object {
        const val TAG = "TAG_RemoteSuggestListService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        RemoteSuggestItemViewFactory(intent, packageName)

    class RemoteSuggestItemViewFactory(val intent: Intent?, val pkg: String) : RemoteViewsFactory {

        private val appList = mutableListOf<String>()

        override fun onCreate() {
            appList.add("com.android.settings")
            appList.add("com.android.settings")
            appList.add("com.android.settings")
            appList.add("com.android.settings")
            appList.add("com.android.settings")
        }

        override fun onDataSetChanged() {
        }

        override fun onDestroy() {
            appList.clear()
        }

        override fun getCount(): Int = appList.size

        override fun getViewAt(position: Int): RemoteViews {
            val remoteViews = RemoteViews(pkg, R.layout.app_suggest_list_item)
            remoteViews.setImageViewResource(
                R.id.suggest_app_icon,
                R.drawable.example_appwidget_preview
            )

            val bundle = Bundle()
            bundle.putString(PACKAGE_NAME,appList[position])
            bundle.putInt("position",position)
            val fillIntent = Intent()
            fillIntent.putExtras(bundle)
            remoteViews.setOnClickFillInIntent(R.id.suggest_app_icon, fillIntent)

            return remoteViews
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = false

    }
}