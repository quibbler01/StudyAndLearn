package cn.quibbler.coroutine.ui.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityMultiBinding
import cn.quibbler.coroutine.ui.view.ui.faker.PhoneInfoActivity

class MultiActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var binding:ActivityMultiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMultiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        when (v?.id) {
            binding.launcher1.id->{
                intent.setClass(this,PhoneInfoActivity::class.java)
            }
            binding.launcher2.id->{
                intent.setClass(this,PhoneInfoActivity::class.java)
            }
            binding.launcher3.id->{
                intent.setClass(this,PhoneInfoActivity::class.java)
            }
            else->{
                intent.setClass(this,PhoneInfoActivity::class.java)
            }
        }
        startActivity(intent)
    }

    private fun init(){
        binding.launcher1.setOnClickListener(this)
        binding.launcher2.setOnClickListener(this)
        binding.launcher3.setOnClickListener(this)
        binding.image.setOnClickListener(this)
    }

}