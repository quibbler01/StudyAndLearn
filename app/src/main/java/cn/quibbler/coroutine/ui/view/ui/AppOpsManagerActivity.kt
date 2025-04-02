package cn.quibbler.coroutine.ui.view.ui

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R

@RequiresApi(Build.VERSION_CODES.Q)
class AppOpsManagerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_AppOpsManagerActivity"
    }

    private lateinit var appOpsManager: AppOpsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_ops_manager)
        appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        appOpsManager.startWatchingMode("OPSTR_RECORD_AUDIO_SANDBOXED", packageName, wacth)

        ops()

        //@Composable invocations can only happen from the context of a @Composable function
//        App()
    }

    @Composable
    fun App(): Int {
        Header()

        Body()
        return 1    //Composable不允许有返回值
    }

    @Composable
    fun Header() {

    }

    @Composable
    fun Body() {
        Button(onClick = {}) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appOpsManager.stopWatchingMode(wacth)
    }

    val wacth = object : AppOpsManager.OnOpChangedListener {
        override fun onOpChanged(op: String, packageName: String) {
            val uid: Int = packageManager.getPackageUid(packageName, 0)
            val result = appOpsManager.checkOpNoThrow(op, uid, packageName)
            Log.e(TAG, "权限发生变更 op = ${op}  packageName = ${packageName} ${result}")
        }
    }

    private fun ops() {
        appOpsManager.apply {

        }
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