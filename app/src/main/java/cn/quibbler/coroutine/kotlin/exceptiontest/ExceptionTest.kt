package cn.quibbler.coroutine.kotlin.exceptiontest

import android.util.Log

class ExceptionTest {

    fun catch() {
        try {
            TestThrow.tes()
        } catch (e: Throwable) {
            Log.e("QUIBBLER", "TestThrow.tes()", e)
        }

    }

}