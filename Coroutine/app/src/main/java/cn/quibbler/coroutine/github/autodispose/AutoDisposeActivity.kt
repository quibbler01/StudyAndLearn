package cn.quibbler.coroutine.github.autodispose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.databinding.ActivityAutoDisposeBinding

class AutoDisposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutoDisposeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutoDisposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}