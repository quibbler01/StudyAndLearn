package cn.quibbler.coroutine.jitpack.material

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityTabLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

class TabLayoutActivity : AppCompatActivity() {

    companion object{
        const val TAG = "TAG_TabLayoutActivity"
    }

    private lateinit var binding: ActivityTabLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setTabLayout()
    }

    private fun setTabLayout(){
        for (i in 0 until binding.tabLayout.tabCount) {
            val tab: TabLayout.Tab? = binding.tabLayout.getTabAt(i)
            tab?.let {
                //val paddingEnd = if (i == binding.tabLayout.tabCount -1) resources.getDimensionPixelSize(R.dimen.tab_layout_gaps) else 0
                val paddingEnd = resources.getDimensionPixelSize(R.dimen.tab_layout_gaps)
                Log.i(TAG,"$tab ${tab.view} paddingEnd:$paddingEnd, i:$i")
                tab.view.setPadding(paddingEnd,0,paddingEnd,paddingEnd)
                tab.view.gravity = Gravity.BOTTOM
            }
        }
    }

    private fun testAddTab(){
        val tab: Tab = binding.tabLayout.newTab()
        binding.tabLayout.addTab(tab)

    }

}