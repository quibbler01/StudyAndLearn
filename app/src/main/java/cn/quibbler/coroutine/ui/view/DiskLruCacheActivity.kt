package cn.quibbler.coroutine.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import cn.quibbler.coroutine.databinding.ActivityDiskLruCacheBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.jakewharton.disklrucache.DiskLruCache
import org.json.JSONObject

/**
 * DiskLruCache简介   http://quibbler.cn/?thread-286.htm
 * DiskLruCache源码   http://quibbler.cn/?thread-831.htm
 * @property binding ActivityDiskLruCacheBinding
 */
class DiskLruCacheActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_DiskLruCacheActivity"

        const val key = "quibbler"

        const val imageUrl = "https://so1.360tres.com/dr/270_500_/t012ff798d83b269e1c.jpg"
    }

    private lateinit var binding: ActivityDiskLruCacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiskLruCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initDiskLruCache()

        saveBitmap()
        getBitmap()
    }

    private fun initView() {
        binding.jumpSettings.setOnClickListener {
            openSettingView("com.xunmeng.pinduoduo")
        }
    }

    fun openSettingView(pkg: String) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", pkg, null)
        startActivity(intent)
    }

    private var diskLruCache: DiskLruCache? = null

    private fun initDiskLruCache() {
        diskLruCache = DiskLruCache.open(cacheDir, 1, 1, 1024 * 1024 * 24L)
        Log.d(TAG, "diskLruCache $diskLruCache")
    }

    private fun saveBitmap() {
        binding.saveBitmap.setOnClickListener {
            Glide.with(this@DiskLruCacheActivity)
                .asBitmap()
                .load(imageUrl)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val editor = diskLruCache?.edit(key)
                        Log.d(TAG, "editor $editor")
                        val outputStream = editor?.newOutputStream(0)
                        Log.d(TAG, "outputStream $outputStream")
                        outputStream?.let {
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, it)
                            it.close()
                        }
                        editor?.commit()
                    }
                })
        }
    }

    private fun getBitmap() {
        binding.getBitmap.setOnClickListener {
            val snapShot = diskLruCache?.get(key)
            Log.d(TAG, "snapshot $snapShot")
            val inputStream = snapShot?.getInputStream(0)
            Log.d(TAG, "inputStream $inputStream")
            inputStream?.let {
                val bitmap = BitmapFactory.decodeStream(it)
                Log.d(TAG, "bitmap $bitmap")
                binding.image.setImageBitmap(bitmap)
            }
        }
    }

}