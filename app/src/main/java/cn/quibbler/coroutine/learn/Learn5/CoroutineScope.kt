package cn.quibbler.coroutine.learn.Learn5

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {

}

/**
 * 1. Relevant functions that constrain coroutines cannot be called arbitrarily
 * 2. Provide some additional capabilities for coroutines
 *
 * @property scopeCoroutine CoroutineContext
 */
interface CoroutineScope {

    val scopeCoroutine: CoroutineContext

}

/**
 * top-level scope
 */
object GlobalScope : CoroutineScope {
    override val scopeCoroutine: CoroutineContext
        get() = EmptyCoroutineContext
}

/**
 * Add scope to the context in which the coroutine was launched
 * @receiver CoroutineScope
 * @param context CoroutineContext
 * @return CoroutineContext
 */
fun CoroutineScope.newCoroutineContext(context: CoroutineContext): CoroutineContext {
    val combind = scopeCoroutine + context
    return combind
}

