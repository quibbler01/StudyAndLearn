package cn.quibbler.coroutine.jitpack.recyclerview

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
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
            repeat(5) {
                addAdapter(FoldAdapter())
            }
        }
        binding.jumpSmallWindow.setOnClickListener { view ->
            val intent = getPackageManager().getLaunchIntentForPackage("com.mmbox.xbrowser.pro")!!
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)

            val options: ActivityOptions = ActivityOptions.makeBasic()
            options.setLaunchBounds(Rect(100, 100, 600, 800))
            val bundle = options.toBundle()
            bundle.putInt("android.activity.launchWindowingMode", 5)

            runCatching {
                val setLaunchWindowingMode =
                    options.javaClass.getDeclaredMethod("setLaunchWindowingMode", Int::class.java)
                        .apply {
                            isAccessible = true
                        }
                setLaunchWindowingMode(options, 5)
            }.onFailure {
                Log.e(TAG, "setLaunchWindowingMode", it)
            }
            runCatching {
                val mLaunchWindowingMode =
                    options.javaClass.getDeclaredField("mLaunchWindowingMode")
                        .apply {
                            isAccessible = true
                        }
                mLaunchWindowingMode.set(options, 5)
            }.onFailure {
                Log.e(TAG, "mLaunchWindowingMode", it)
            }
            runCatching {
                val mMiuiWindowingMode =
                    options.javaClass.getDeclaredField("mMiuiWindowingMode")
                        .apply {
                            isAccessible = true
                        }
                mMiuiWindowingMode.set(options, 5)
            }.onFailure {
                Log.e(TAG, "mMiuiWindowingMode", it)
            }

            startActivity(intent, bundle)
        }
    }

}