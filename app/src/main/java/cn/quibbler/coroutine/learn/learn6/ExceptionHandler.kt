package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

fun main() {

    GlobalCoroutineExceptionHandler().launch(Dispatchers.Default, start = CoroutineStart.DEFAULT) {
        println("GlobalCoroutineExceptionHandler")
        throw Exception("********** Hey!")
    }.start()

}

class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = this

    companion object KEY : CoroutineContext.Key<GlobalCoroutineExceptionHandler>

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Global Coroutine exception : $exception")
    }

}

class ExceptionHandler {


}