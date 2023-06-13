package cn.quibbler.coroutine.learn.Learn5

import cn.quibbler.coroutine.learn.launchCoroutine
import kotlinx.coroutines.CancellationException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

fun main() {

    launchCoroutine(CoroutineExceptionHandler { coroutineContext, throwable ->
        println("[ExceptionHandler] ${throwable.message}")
    }) {
        println(1)
        throw IllegalStateException("try  0 ")
        println(2)
    }

}

interface CoroutineExceptionHandler : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<CoroutineExceptionHandler>

    fun handleException(context: CoroutineContext, exception: Throwable)

}

private fun tryHandlerException(e: Throwable): Boolean {
    return when (e) {
        is CancellationException -> false
        else -> {
            handleJobException(e)
        }
    }
}

fun handleJobException(e: Throwable): Boolean {
    return false
}

inline fun CoroutineExceptionHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            handler.invoke(context, exception)
        }
    }