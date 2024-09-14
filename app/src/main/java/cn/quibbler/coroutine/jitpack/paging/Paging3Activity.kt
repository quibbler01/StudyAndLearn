package cn.quibbler.coroutine.jitpack.paging

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.quibbler.coroutine.databinding.ActivityPaging3Binding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650255228&idx=1&sn=f73e49c6934d9e235e0692a2452f21cd&chksm=88635e13bf14d705c5a9a2c49b1aeac24abeb7fa38206afb089043f9869f099da982841f8bba&scene=27
 */
class Paging3Activity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_Paging3Activity"
    }

    private val pagingViewModel by viewModel<PagingViewModel>()

    private val pagingAdapter by lazy { UserPagingAdapter() }

    private lateinit var binding: ActivityPaging3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaging3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.recyclerView.adapter = pagingAdapter
        lifecycleScope.launch {
            pagingViewModel.pagingFlow.collectLatest { userData ->
                Log.d(TAG, "pagingFlow $userData")
                pagingAdapter.submitData(userData)
            }
        }
    }

}