package cn.quibbler.coroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import cn.quibbler.coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainScope by lazy { MainScope() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //use Coroutine in Lifecycle
        lifecycleScope.launch {
            //todo
        }

        val register: ActivityResultContract<Intent, ActivityResult> = ActivityResultContracts.StartActivityForResult()

        val callback = object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                Log.d("TAG", "resultCode:${result?.resultCode}  data:${result?.data}")
            }
        }

        val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(register, callback)

        val intent = Intent()

        launcher.launch(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}