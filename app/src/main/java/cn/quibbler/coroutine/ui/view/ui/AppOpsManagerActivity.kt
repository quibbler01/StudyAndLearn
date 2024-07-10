package cn.quibbler.coroutine.ui.view.ui

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R

@RequiresApi(Build.VERSION_CODES.Q)
class AppOpsManagerActivity : AppCompatActivity() {

    private lateinit var appOpsManager: AppOpsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app_ops_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        appOpsManager.startWatchingMode("", packageName, wacth)

        kotlin.runCatching {
            var userAgent = System.getProperty("http.agent")
            //Dalvik/2.1.0 (Linux; U; Android 13; V2055A Build/TP1A.220624.014)
            Log.d("QUIBBLER_A", "userAgent : $userAgent")

            val webView = WebView(this)
            val setting = webView.settings
            userAgent = setting.userAgentString
            //Mozilla/5.0 (Linux; Android 13; V2055A Build/TP1A.220624.014; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/115.0.5790.166 Mobile Safari/537.36
            Log.d("QUIBBLER_A", "setting.userAgentString : $userAgent")

            //replace UA
            setting.userAgentString = userAgent
        }

        ops()
    }

    override fun onDestroy() {
        super.onDestroy()
        appOpsManager.stopWatchingMode(wacth)

    }

    val wacth = object : AppOpsManager.OnOpChangedListener {
        override fun onOpChanged(op: String, packageName: String) {
            val uid: Int = packageManager.getPackageUid(packageName, 0)
            val result = appOpsManager.checkOpNoThrow(op, uid, packageName)
            Log.e("zh", "权限发生变更 op = ${op}  packageName = ${packageName} ${result}")
        }
    }

    private fun ops() {
        appOpsManager
    }

    fun checkPermission(pkg: String, op: String): Int {
        return try {
            val uid = packageManager.getPackageUid(pkg, 0)
            appOpsManager.checkOp(op, uid, pkg)
            appOpsManager.unsafeCheckOp(op, uid, pkg)
        } catch (_: Exception) {
            -1
        }
    }

    fun safeCheckPermission(pkg: String, op: String): Int {

        val uid = packageManager.getPackageUid(pkg, 0)
        appOpsManager.checkOpNoThrow(op, uid, pkg)
        return appOpsManager.unsafeCheckOpNoThrow(op, uid, pkg)
    }

    fun checkUid(pkg: String, uid: Int): Boolean {
        return runCatching {
            appOpsManager.checkPackage(uid, pkg)

            packageManager.getPackageUid(pkg, 0) == uid
        }.isSuccess

    }

}