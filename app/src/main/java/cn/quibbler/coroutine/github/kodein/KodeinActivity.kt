package cn.quibbler.coroutine.github.kodein

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.quibbler.coroutine.R
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class KodeinActivity : AppCompatActivity(), DIAware {

    override val di: DI by lazy {
        DI {

        }
    }

    val str: String by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodein)
    }

}