package com.example.assistantwidget.utis

import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "TAG_PACKAGE_UTILS"

fun openApp(context: Context?, pkg: String) {
    context?.let {
        runCatching {
            val intent = context.packageManager.getLaunchIntentForPackage(pkg)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }.onFailure {
            Log.e(TAG, "start:$pkg failed", it)
        }.onSuccess {
            Log.d(TAG, "start:$pkg success")
        }
    }
}