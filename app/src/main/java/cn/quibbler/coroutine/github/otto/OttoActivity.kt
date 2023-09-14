package cn.quibbler.coroutine.github.otto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.R
import com.squareup.otto.Bus
import com.squareup.otto.Produce
import com.squareup.otto.Subscribe

class OttoActivity : AppCompatActivity() {

    companion object {

        val bus = Bus()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otto)

        bus.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
    }

    class Event {

    }

    @Subscribe
    fun onResult(event: Event) {

    }

    @Produce
    fun event(): Event {
        return Event()
    }

}