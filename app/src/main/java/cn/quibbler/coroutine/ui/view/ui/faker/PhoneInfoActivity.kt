package cn.quibbler.coroutine.ui.view.ui.faker

import android.app.ActivityManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R

class PhoneInfoActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_phone_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val label = ""

        var description = ActivityManager.TaskDescription.Builder()
            .setLabel(label)
            .setIcon(R.drawable.app_icon_one)
            .build()

        var bitmap:Bitmap? = null

        description = ActivityManager.TaskDescription(label,bitmap)

        setTaskDescription(description)

    }
}