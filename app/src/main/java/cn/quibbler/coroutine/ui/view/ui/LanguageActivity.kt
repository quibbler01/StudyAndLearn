package cn.quibbler.coroutine.ui.view.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityLanguageBinding
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_LanguageActivity"
    }

    private lateinit var binding: ActivityLanguageBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(changeLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()

        Log.d(TAG, "layoutDirection:${resources.configuration.layoutDirection}")
    }

    private fun initView() {
        VehicleKeyboardHelper.bind(binding.vehicleTick)
    }

    private fun kill() {
        android.os.Process.killProcess(android.os.Process.myPid())

        System.exit(0)
        Runtime.getRuntime().exit(0);

    }

    fun changeLanguage(context: Context?, code: String = "ar"): Context? {
        if (context == null) return context

        val oldCode = context.resources.configuration.locales[0].language
        val oldCountry = context.resources.configuration.locales[0].country
        val newLocal = Locale(code, oldCountry)


        val newConfiguration = Configuration(context.resources.configuration)
        newConfiguration.setLocales(LocaleList(newLocal))
        newConfiguration.setLocale(newLocal)
        newConfiguration.setLayoutDirection(newLocal)
        Locale.setDefault(newLocal)

        return context.createConfigurationContext(newConfiguration)
    }

}


