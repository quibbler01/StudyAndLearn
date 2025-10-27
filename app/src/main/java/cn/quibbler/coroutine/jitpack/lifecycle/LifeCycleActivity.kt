package cn.quibbler.coroutine.jitpack.lifecycle

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.FrameMetrics
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.FrameMetricsAggregator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import com.airbnb.lottie.LottieAnimationView

class LifeCycleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TAG_LifeCycleActivity"
    }

    private val aggregator: FrameMetricsAggregator = FrameMetricsAggregator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_life_cycle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val handler: HandlerThread = HandlerThread(TAG)
        handler.start()
        window.addOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener, Handler(handler.looper))

        setShowWhenLocked(true)

        var lottieAnimationView: LottieAnimationView? = null

        lottieAnimationView?.setAnimation("")
        lottieAnimationView?.imageAssetsFolder = ""

        val countDownTimer: CountDownTimer = object : CountDownTimer(100,10){
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG,"onTick")
            }

            override fun onFinish() {
                Log.d(TAG,"onFinish")
            }
        }

        aggregator.add(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        window.removeOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener)
        aggregator.remove(this)
    }

    private val onFrameMetricsAvailableListener: Window.OnFrameMetricsAvailableListener = object : Window.OnFrameMetricsAvailableListener{
        override fun onFrameMetricsAvailable(
            window: Window?,
            frameMetrics: FrameMetrics?,
            dropCountSinceLastInvocation: Int
        ) {
            frameMetrics?.apply {
                this.getMetric(FrameMetrics.FIRST_DRAW_FRAME)
            }
        }
    }

}