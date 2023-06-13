package cn.quibbler.coroutine.github.coil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.databinding.ActivityCoilBinding
import coil.load

class CoilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadImageViaCoil()
    }

    private fun loadImageViaCoil() {
        binding.coilDemoImage.load("https://img2.baidu.com/it/u=3202947311,1179654885&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1679331600&t=69f12273abae16f8290a7d79d45c0cc3")
    }

}