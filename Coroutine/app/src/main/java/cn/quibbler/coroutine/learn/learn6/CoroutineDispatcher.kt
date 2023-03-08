package cn.quibbler.coroutine.learn.learn6

import cn.quibbler.coroutine.learn.Learn5.Dispatcher
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {

    /**
     * [Dispatchers.Main]
     * [Dispatchers.Default]
     * [Dispatchers.IO]
     * [Dispatchers.Unconfined]
     *
     * actually IO and Default is the same Thread Pool.
     */
    GlobalScope.launch(Dispatchers.Main, CoroutineStart.LAZY) {

    }.start()

    /**
     * Use already defined Thread Pools.
     */
    Executors.newSingleThreadExecutor().asCoroutineDispatcher().use { dispatcher ->
        val result = GlobalScope.async(dispatcher) {
            delay(100)
            "hello world"
        }.await()
    }

    /**
     * Priority Of Use
     *
     * use API [withContext]
     */
    Executors.newSingleThreadExecutor().asCoroutineDispatcher().use {dispatcher ->
        val result = withContext(dispatcher){
            delay(100)
            "hello await"
        }
    }

}

/**
 * Define your self Dispatcher ,but this is not usually in project.
 */
class MyDispatcher : kotlinx.coroutines.CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        //TODO
    }

}

object GlobalScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
}

class CoroutineDispatcher {


}