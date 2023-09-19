package cn.quibbler.coroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import cn.quibbler.coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainScope by lazy { MainScope() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //use Coroutine in Lifecycle
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(5000)
                val name = Thread.currentThread().name
                mainScope.launch {
                    binding.text.text = "$name    hello test ${Looper.getMainLooper() == Looper.myLooper()}"
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

}