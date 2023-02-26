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
        suspendCoroutine<Unit> { continuation ->
            executor.schedule({ continuation.resume(Unit) }, time, unit)
        }
    }

}