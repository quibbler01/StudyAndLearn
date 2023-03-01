package cn.quibbler.coroutine.learn.Learn5

import kotlin.coroutines.CoroutineContext

interface Job : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<Job> {
        private const val TAG = "Key_Job"
    }

    override val key: CoroutineContext.Key<*>
        get() = Job

    val isActive: Boolean

    fun invokeOnCancel(onCancel: OnCancel): Disposable

    fun invokeOnCompletion(onComplete: OnComplete): Disposable

    fun cancel()

    fun remove(disposable: Disposable)

    suspend fun join()

}