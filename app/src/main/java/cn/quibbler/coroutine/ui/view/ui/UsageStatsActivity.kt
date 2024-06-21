package cn.quibbler.coroutine.ui.view.ui

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.query
import cn.quibbler.coroutine.App
import cn.quibbler.coroutine.R


class UsageStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usage_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (checkUsageAccessPermission()) {
            aqueryUsage()
        } else {
            val intent: Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }


        window.decorView.viewTreeObserver.isAlive
    }

    fun aqueryUsage() {
        val usageStateManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        usageStateManager.apply {
            val usage = queryUsageStats()
//            queryAndAggregateUsageStats()

//            queryEvents()
//            queryEventStats()
//            queryEventsForSelf()

//            queryConfigurations()
        }
    }

    //https://www.cnblogs.com/baiqiantao/p/12758115.html
    fun checkUsageAccessPermission(): Boolean {
        val appOpsManager: AppOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return when (mode) {
            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_DEFAULT -> checkCallingPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
            else -> false
        }
    }

}