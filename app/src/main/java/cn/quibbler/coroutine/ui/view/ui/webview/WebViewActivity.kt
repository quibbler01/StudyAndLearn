package cn.quibbler.coroutine.ui.view.ui.webview

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityWebViewBinding
import cn.quibbler.coroutine.ui.view.ui.webview.WebViewActivity.Companion.TAG

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_WebViewActivity"
    }

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        //FrameLayout
        Log.d(TAG, "decorView.rootView ${window.decorView.rootView.javaClass.name}")
        //FrameLayout
        Log.d(TAG, "decorView ${window.decorView.javaClass.name}")
    }

    private fun initView() {


        val chromeClient = object : WebChromeClient() {

            private var customView: View? = null
            private var customViewCallback: CustomViewCallback? = null

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                Log.d(TAG, "onShowCustomView ----  ${view?.javaClass?.name}")
                (view as? FrameLayout)?.apply {
                    Log.d(TAG, "childCount ***** ${this.childCount}")
                }?.children?.apply {
                }?.forEach {
                    Log.d(TAG, "forEach ***** ${it?.javaClass?.name}")
                }

                if (customView != null) {
                    onHideCustomView()
                    return
                }

                customView = view
                customViewCallback = callback

                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//                (window.decorView.rootView as ViewGroup).addView(customView, layoutParams)

                (binding.root as ViewGroup).addView(customView, 0, layoutParams)

//                binding.container.addView(customView,layoutParams)
//                binding.container.visibility = View.VISIBLE

                //切换横屏
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onHideCustomView() {
                Log.d(TAG, "onHideCustomView")
//                (window.decorView.rootView as ViewGroup).removeView(customView)

                (binding.root as ViewGroup).removeView(customView)

//                binding.container.removeView(customView)
//                binding.container.visibility = View.GONE

                customViewCallback?.onCustomViewHidden()

                customView = null
                customViewCallback = null

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            @Deprecated("Deprecated in Java")
            override fun onShowCustomView(view: View?, requestedOrientation: Int, callback: CustomViewCallback?) {
                super.onShowCustomView(view, requestedOrientation, callback)
            }
        }

        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = chromeClient

        binding.webView.settings.apply {
            allowFileAccess = true
            javaScriptEnabled = true
            allowContentAccess = true
            allowFileAccessFromFileURLs = true
        }

        binding.webView.loadUrl("http://tv.cctv.com/2024/05/27/VIDEnA5ZjOCi5iYLqu192To0240527.shtml")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}

val chromeClient = object : WebChromeClient() {

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        super.onHideCustomView()
    }

    @Deprecated("Deprecated in Java")
    override fun onShowCustomView(view: View?, requestedOrientation: Int, callback: CustomViewCallback?) {
        super.onShowCustomView(view, requestedOrientation, callback)
    }
}