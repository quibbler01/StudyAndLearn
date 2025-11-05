package cn.quibbler.coroutine.kotlin.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import cn.quibbler.coroutine.databinding.ActivityFlowBinding
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlowActivity : AppCompatActivity() {

    companion object{
        const val TAG = "TAG_FlowActivity"
    }

    private lateinit var binding: ActivityFlowBinding

    private val _flow: MutableStateFlow<String?> = MutableStateFlow(null)
    val flow: StateFlow<String?> = _flow.asStateFlow()

    /**
     * 不支持多线程并发访问，只支持主线程安全
     *
     * 多个协程并发访问可能创建多个VM，必须主线程访问触发初始化
     */
    private val stateVM: StateFlowVM by viewModels<StateFlowVM>()

    val defaultScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        lifecycleScope.launch {
            MMKV.defaultMMKV().putString("key", "hello")
            delay(10000)
            binding.console.append("${MMKV.defaultMMKV().getString("key", "default")}")
        }

        stateVM.uiState

        lifecycleScope.launch {
            lifecycleScope.launch  {
//                delay(100)
                stateVM.uiState.collect {
                    Log.d(TAG,"111        $it".apply { console(this) })
                }
            }

            lifecycleScope.launch {
//                delay(10)
                stateVM.uiState.collect {
                    Log.d(TAG,"222        $it".apply { console(this) })
                }
            }

            defaultScope.launch(Dispatchers.Default)  {
                delay(50)
                stateVM.uiState.collect {
                    Log.d(TAG,"333        $it".apply { console(this) })
                }
            }

        }
    }

    private fun console(msg: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.console.append("$msg\n")
        }
    }

    @OptIn(FlowPreview::class)
    private fun initView() {
        _flow.debounce(500)
            .catch {
                Log.e("QUIBBLER", "catch", it)
            }.onEach {
                it?.trim()
            }

        binding.editText.addTextChangedListener(
            onTextChanged = { text, start, before, count ->
                lifecycleScope.launch {
                    //flow.emit(text?.toString())

                    //flow.value = text?.toString()

                    if (_flow.tryEmit(text?.toString())) {
                        //emit success
                    } else {
                        //emit failed
                    }
                }
            }
        )

        lifecycleScope.launch(Dispatchers.IO) {
            var i = 0
            while (true) {
                delay(100)
                _flow.emit("${i++}")
            }
        }
    }

}