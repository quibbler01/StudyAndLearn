package cn.quibbler.coroutine.ui.view.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.storage.StorageManager
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityStorageStatsManagerBinding

@RequiresApi(Build.VERSION_CODES.S)
class StorageStatsManagerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "QUIBBLER_StorageStatsManagerActivity"
    }

    private lateinit var binding: ActivityStorageStatsManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStorageStatsManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        kotlin.runCatching {
            init()
        }.getOrElse { e ->
            Log.e(TAG, "get failed", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val screenCaptureCallback = object : Activity.ScreenCaptureCallback {
        override fun onScreenCaptured() {

        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onResume() {
        super.onResume()
        registerScreenCaptureCallback(mainExecutor, screenCaptureCallback)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onPause() {
        super.onPause()
        unregisterScreenCaptureCallback(screenCaptureCallback)
    }

    private fun init() {
        val userHandler = Process.myUserHandle()

        //Class requires API level 26 (current min is 24)
        val storageStatsManager: StorageStatsManager = getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager


        val storageManager = getSystemService(Context.STORAGE_SERVICE) as StorageManager

        val sv = storageManager.storageVolumes

        storageManager.storageVolumes.apply {
            Log.d(TAG, "storageVolumes ${this.size}")
        }.forEach { storageVolume ->
            storageVolume.storageUuid
            val uuid = storageVolume.storageUuid!!

            Log.d(TAG, "$uuid getAllocatableBytes(uuid) ${storageManager.getAllocatableBytes(uuid).toMb()}")

            //
            val total = storageStatsManager.getTotalBytes(uuid)

            val storageStats = storageStatsManager.queryStatsForPackage(uuid, "com.larus.nova", userHandler)
            //应用程序的大小。这包括 APK 文件、优化的编译器输出和解压的原生库
            storageStats.appBytes

            //应用程序所有数据的大小。这包括存储在 Context.getDataDir()、Context.getCacheDir()、Context.getCodeCacheDir() 下的文件
            storageStats.dataBytes

            //应用程序缓存数据的大小。这包括存储在 Context.getCacheDir() 和 Context.getCodeCacheDir() 下的文件
            storageStats.cacheBytes

            //主外部共享存储中所有缓存数据的大小。这包括存储在 Context.getExternalCacheDir() 下的文件
            storageStats.externalCacheBytes

            Log.d(
                TAG,
                "total:${total} appBytes:${storageStats.appBytes} dataBytes:${storageStats.dataBytes} cacheBytes:${storageStats.cacheBytes} externalCacheBytes:${storageStats.externalCacheBytes} "
            )


            Log.d(
                TAG,
                "total:${total.toMb()} appBytes:${storageStats.appBytes.toMb()} dataBytes:${storageStats.dataBytes.toMb()} cacheBytes:${storageStats.cacheBytes.toMb()} externalCacheBytes:${storageStats.externalCacheBytes.toMb()} "
            )
        }

    }

}

@SuppressLint("DefaultLocale")
fun Long.toMb(): String {
    if (this == 0L) {
        return "0B"
    } else if (this < 1024) {
        return String.format("%.2f", this) + "B"
    } else if (this < 1024 * 1024) {
        return String.format("%.2f", this / 1000f) + "Kb"
    } else if (this < 1024 * 1024 * 1024) {
        return String.format("%.2f", this / 1000f / 1000f) + "Mb"
    } else {
        return String.format("%.2f", this / 1000f / 1000f / 1000f) + "Gb"
    }
}


fun Long.toMbs(): Map<String, Long> {
    val units = mapOf(
        "B" to 1L,
        "KB" to 1000L,
        "MB" to 1000 * 1000L,
        "GB" to 1000 * 1000 * 1000L,
        "TB" to 1000 * 1000 * 1000 * 1000L
    )

    return units.mapValues { (_, factor) -> this / factor }
}
