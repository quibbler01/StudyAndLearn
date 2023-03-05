package cn.quibbler.coroutine.learn.Learn5

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

open class DispatcherContext(private val dispatcher: Dispatcher) : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> = DispatcherContinuation(continuation, dispatcher)

}

private class DispatcherContinuation<T>(
    val delegate: Continuation<T>,
    val dispatcher: Dispatcher
) : Continuation<T> {
    override val context: CoroutineContext
        get() = delegate.context

    override fun resumeWith(result: Result<T>) {
        dispatcher.dispatch {
            delegate.resumeWith(result)
        }
    }
}

interface Dispatcher {
    fun dispatch(block: () -> Unit)
}

object Dispatchers {
    val Default by lazy {
        DispatcherContext(CoroutineDispatcher.DefaultDispatcher)
    }

    val Android by lazy {
        DispatcherContext(AndroidDispatcher)
    }
}

class CoroutineDispatcher {

    /**
     * 默认的调度器
     */
    object DefaultDispatcher : Dispatcher {

        private val threadGroup = ThreadGroup("DefaultDispatcher")

        private val threadIndex = AtomicInteger(0)

        private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1) { runnable ->
            Thread(threadGroup, runnable, "${threadGroup.name}--worker-${threadIndex.getAndIncrement()}").apply { isDaemon = true }
        }

        override fun dispatch(block: () -> Unit) {
            executor.execute(block)
        }

    }

}

/**
 * Android主线程调度器
 */
object AndroidDispatcher : Dispatcher {

    private val handler = Handler(Looper.getMainLooper())

    override fun dispatch(block: () -> Unit) {
        handler.post(block)
    }

}







