package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.*

suspend fun main() {

    /**
     * then time out 1s,cancel this coroutine, and throw exception
     */
    GlobalScope.launch {
        val t = withTimeout(1000) {
            task()
            println("withTimeout")
        }
    }.join()

    /**
     * cancel when time 2s it out, but not throw Exception
     */
    GlobalScope.launch {
        val t = withTimeoutOrNull(2000) {
            task()
            println("withTimeoutOrNull")
        }
    }.join()

}

private suspend fun task() {

}

class CancelCoroutineTimeOut {

    suspend fun uncancelable() {
        yield()
        /**
         * [NonCancellable] only used in [withContext] method, don't used in [async] or [launch]
         */
        withContext(NonCancellable) {
            delay(1000)
        }
    }

}