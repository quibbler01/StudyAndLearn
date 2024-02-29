package cn.quibbler.coroutine.github.koin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import cn.quibbler.coroutine.R
import org.koin.core.context.startKoin
import java.lang.Exception

class KoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koin)

        val lp = window.attributes
        lp.screenBrightness = 0.5f
        window.attributes = lp



        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
        } else {
            try {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 60);

                val screenBright = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);

            } catch (e: Exception) {
                Log.e("QUIBBLER", "SCREEN_BRIGHTNESS", e)
            }
        }

        startKoin {

        }
    }

}