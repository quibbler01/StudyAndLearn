package cn.quibbler.coroutine

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.quibbler.coroutine.databinding.ActivityMainBinding
import cn.quibbler.coroutine.jitpack.viewmodel.SearchViewModel
import cn.quibbler.coroutine.kotlin.exceptiontest.ExceptionTest
import cn.quibbler.coroutine.kotlin.flow.LeastNewsUiState
import cn.quibbler.coroutine.kotlin.flow.StateFlowVM
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainScope by lazy { MainScope() }

    private val viewModel: SearchViewModel by viewModels()

    private val stateFlowVM: StateFlowVM by viewModels()

    private fun loadImage(img: ImageView, images: List<String>, index: Int) {
        if (index >= images.size) return
        Glide.with(applicationContext)
            .load(images[index])
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    loadImage(img, images, index + 1)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    return true
                }
            }).preload()


        val requestBuilder = Glide.with(this).asDrawable()
        requestBuilder
            .error("")
            .preload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.uiState.update { str ->
            "str$str"
        }

        Glide.with(applicationContext)
            .load("")
            .error("")
            .apply(RequestOptions().apply {
                centerCrop()
                placeholder(R.drawable.ic_launcher_background)
                priority(Priority.HIGH)
                diskCacheStrategy(DiskCacheStrategy.NONE)
            }).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    return true
                }
            }).preload()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                stateFlowVM.uiState.collect {
                    when (it) {
                        is LeastNewsUiState.Success -> {
                            val newsList = it.news
                        }

                        is LeastNewsUiState.Error -> {

                        }
                    }
                }
            }
        }

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

        ExceptionTest().catch()

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

}