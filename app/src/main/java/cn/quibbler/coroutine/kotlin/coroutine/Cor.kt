package cn.quibbler.coroutine.kotlin.coroutine

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine


suspend fun create() {

    coroutineScope {
        async {

        }
    }

    // result is Continuation<Unit>
    val s = suspend {
        1   // result
    }.createCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = Dispatchers.IO

        override fun resumeWith(result: Result<Int>) {
            Log.d("QUIBBLER", "createCoroutine result:${result.getOrNull()}")
        }
    })

    //start this Coroutine
    s.resume(Unit)


    //s1 is Unit type
    val s1 = suspend {
        99
    }.startCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            Log.d("QUIBBLER", "startCoroutine result:${result.getOrNull()}")
        }
    })


}
