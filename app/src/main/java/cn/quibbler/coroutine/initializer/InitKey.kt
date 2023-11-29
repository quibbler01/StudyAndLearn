package cn.quibbler.coroutine.initializer

import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV

class InitKey : Initializer<Unit> {

    override fun create(context: Context) {
        MMKV.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

}