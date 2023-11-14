package cn.quibbler.coroutine.kotlin.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import cn.quibbler.coroutine.databinding.ActivityFlowBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
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

    private lateinit var binding: ActivityFlowBinding

    private val _flow: MutableStateFlow<String?> = MutableStateFlow(null)
    val flow: StateFlow<String?> = _flow.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
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

        lifecycleScope.launch(Dispatchers.IO){
            var i = 0
            while (true) {
                delay(100)
                _flow.emit("${i++}")
            }
        }

        lifecycleScope.launch {
            flow.collect {
                withContext(Dispatchers.Main) {
                    binding.console.text = "$it"
                }
            }
        }

    }

}