package cn.quibbler.coroutine.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityDiskLruCacheBinding
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File

/**
 * DiskLruCache简介   http://quibbler.cn/?thread-286.htm
 * DiskLruCache源码   http://quibbler.cn/?thread-831.htm
 * @property binding ActivityDiskLruCacheBinding
 */
class DiskLruCacheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiskLruCacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiskLruCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDiskLruCache()
    }

    private fun initDiskLruCache() {
        val diskLruCache = DiskLruCache.open(File(""), 1, 1, 1L)
    }

}