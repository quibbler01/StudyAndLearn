package cn.quibbler.coroutine.jitpack.lifecycle

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import com.airbnb.lottie.LottieAnimationView

class LifeCycleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TAG_LifeCycleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_life_cycle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

    }

}