package cn.quibbler.coroutine.jitpack.recyclerview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ConcatAdapter
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_RecyclerViewActivity"
    }

    private lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVie()
    }

    private fun initVie() {
        binding.recyclerView.adapter = ConcatAdapter().apply {
            repeat(5){
                addAdapter(FoldAdapter())
            }
        }
    }

}