package cn.quibbler.coroutine.jitpack.paging

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R

/**
 * https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650255228&idx=1&sn=f73e49c6934d9e235e0692a2452f21cd&chksm=88635e13bf14d705c5a9a2c49b1aeac24abeb7fa38206afb089043f9869f099da982841f8bba&scene=27
 */
class Paging3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paging3)

    }
}