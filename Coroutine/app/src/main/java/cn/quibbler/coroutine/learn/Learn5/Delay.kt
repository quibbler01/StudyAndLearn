package cn.quibbler.coroutine.learn.Learn5

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Delay {

    private val executor = Executors.newScheduledThreadPool(1) { runnable ->
        Thread(runnable, "schedule").apply {
            isDaemon = true
        }
    }

    suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        if (time <= 0) {
            return
        }

        suspendCoroutine<Unit> { continuation ->
            val future = executor.schedule({ continuation.resume(Unit) }, time, unit)

            //添加协程的回调，在协程取消时回调  future.cancel(true)
        }
    }

}