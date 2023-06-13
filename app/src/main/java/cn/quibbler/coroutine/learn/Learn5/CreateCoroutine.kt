package cn.quibbler.coroutine.learn.Learn5

import kotlinx.coroutines.*
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.selects.SelectClause1
import kotlin.coroutines.Continuation
import kotlin.coroutines.startCoroutine

class CreateCoroutine {

    @OptIn(InternalCoroutinesApi::class)
    class DeferredCoroutine<T>(context: CoroutineContext) : AbstractCoroutine<T>(context, false, false), Deferred<T> {
        override val onAwait: SelectClause1<T>
            get() = TODO("Not yet implemented")

        override suspend fun await(): T {
            TODO("Not yet implemented")
        }

        @ExperimentalCoroutinesApi
        override fun getCompleted(): T {
            TODO("Not yet implemented")
        }
    }

    companion object {

        fun <T> async(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend () -> T
        ) {
            block.startCoroutine(DeferredCoroutine(context))
        }

        fun launch(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend () -> Unit
        ): Unit {
            val completion = object : Continuation<Unit> {
                override val context: CoroutineContext
                    get() = TODO("Not yet implemented")

                override fun resumeWith(result: Result<Unit>) {
                    TODO("Not yet implemented")
                }
            }
            block.startCoroutine(completion)
        }

    }

}
