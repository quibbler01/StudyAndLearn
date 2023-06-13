package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {

    /**
     * [CoroutineStart.DEFAULT]
     * [CoroutineStart.ATOMIC]
     * [CoroutineStart.LAZY]
     * [CoroutineStart.UNDISPATCHED]
     */
    LaunchCoroutine().launch(start = CoroutineStart.ATOMIC) {

    }

}


class LaunchCoroutine : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

    suspend fun help() {

    }

}