package cn.quibbler.coroutine.ui.view.ui.payload

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityPayloadBinding

class PayloadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayloadBinding

    private val adapter = PayloadAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPayloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView(){
        binding.recyclerView.adapter = adapter

        binding.notifyPayload.setOnClickListener{
            adapter.apply {
                isChange = true
                notifyItemRangeChanged(0,itemCount,PayloadAdapter.PAYLOAD)
            }
        }

        binding.notifyNull.setOnClickListener{
            adapter.apply {
                isChange = false
                notifyItemRangeChanged(0,itemCount)
            }
        }
    }


}