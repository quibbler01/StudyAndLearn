package cn.quibbler.coroutine.github.koin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.R
import org.koin.core.context.startKoin

class KoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koin)

        startKoin {

        }
    }

}