package cn.quibbler.coroutine.learn

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *
 */
object SymCoroutines {

    val coroutine0: SymCoroutine<Int> = SymCoroutine.create<Int> { param: Int ->
        println("coroutine-0 $param")
        val result = transfer(coroutine1, 0)
        println("coroutine-0 1 $result")
    }


    val coroutine1: SymCoroutine<Int> = SymCoroutine.create<Int> { param: Int ->
        println("coroutine-1 $param")
        val result = transfer(coroutine2, 1)
        println("coroutine-1 1 $result")
    }

    val coroutine2: SymCoroutine<Int> = SymCoroutine.create<Int> { param: Int ->
        println("coroutine-2 $param")
        val result = transfer(coroutine0, 2)
        println("coroutine-2 1 $result")
    }
}

interface SymCoroutineScope<T> {
    suspend fun <P> transfer(sumCoroutines: SymCoroutine<P>, value: P): T
}

class SymCoroutine<T>(
    override val context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend SymCoroutineScope<T>.(T) -> Unit
) : Continuation<T> {

    companion object {
        lateinit var main: SymCoroutine<Any?>

        suspend fun main(
            block: suspend SymCoroutineScope<Any?>.() -> Unit
        ) {
            SymCoroutine<Any?> {
                block()
            }.also {
                main = it
            }.start(Unit)
        }

        fun <T> create(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend SymCoroutineScope<T>.(T) -> Unit
        ): SymCoroutine<T> {
            return SymCoroutine<T>(context, block)
        }

    }

    suspend fun start(value: T) {

    }

    val isMain: Boolean
        get() = this == main

    override fun resumeWith(result: Result<T>) {

    }

}
