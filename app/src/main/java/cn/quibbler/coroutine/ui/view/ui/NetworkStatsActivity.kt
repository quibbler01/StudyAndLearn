package cn.quibbler.coroutine.ui.view.ui

import android.app.AppOpsManager
import android.app.usage.NetworkStatsManager
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.UserHandle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.quibbler.coroutine.databinding.ActivityNetworkStatsBinding

class NetworkStatsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_NetworkStatsActivity"
    }

    private lateinit var binding: ActivityNetworkStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (hasPermissionToReadNetworkStats()) {
            query()
        } else {
            requestReadNetworkStats()
        }

        val userHandle: UserHandle = android.os.Process.myUserHandle()

        val pid = android.os.Process.myPid()

        val uid = android.os.Process.myUid()
    }

    fun hasPermissionToReadNetworkStats(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true

        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // 打开“有权查看使用情况的应用”页面
    fun requestReadNetworkStats() {
        val intent: Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }

    private fun query() {
        val networkStatsManager: NetworkStatsManager = getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager
        val bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis())
        Log.i(TAG, "Total: " + (bucket.rxBytes + bucket.txBytes) / 1024 / 1024 / 1024 + "Gb")

        networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, null, 0, System.currentTimeMillis(), uid)

    }

}