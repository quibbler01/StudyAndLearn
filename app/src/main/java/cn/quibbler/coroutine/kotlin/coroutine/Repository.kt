package cn.quibbler.coroutine.kotlin.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

suspend fun getRemoteSync(): Boolean {
    return suspendCancellableCoroutine { con ->
        if (true) {
            con.resume(false)
        } else {
            con.resume(true)
        }
    }
}

class Repository {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    init {
        val job1 = scope.launch {

        }

        val job = scope.launch(Dispatchers.Default + CoroutineName("BackgroundCoroutine")) {

        }
    }

    suspend fun fetchData() {
        get()
    }

    private suspend fun get() {
        withContext(Dispatchers.IO) {

        }
    }

    private suspend fun fetchTwoDoc() {
        coroutineScope {
            val s1 = async { fetchDoc() }
            val s2 = async { fetchDoc() }

            s1.await()
            s2.await()
        }
    }

    private suspend fun fetchTwoDocAll() {
        coroutineScope {
            val deferreds = listOf(
                async { fetchDoc() },
                async { fetchTwoDoc() }
            )
            deferreds.awaitAll()
        }
    }

    private suspend fun fetchDoc(): String {
        withContext(Dispatchers.Default) {
            delay(1000)
        }
        return ""
    }

}