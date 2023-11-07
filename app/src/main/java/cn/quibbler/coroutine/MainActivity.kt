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
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.quibbler.coroutine.databinding.ActivityMainBinding
import cn.quibbler.coroutine.jitpack.viewmodel.SearchViewModel
import cn.quibbler.coroutine.kotlin.flow.LeastNewsUiState
import cn.quibbler.coroutine.kotlin.flow.StateFlowVM
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.uiState.update { str ->
            "str$str"
        }


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

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

}