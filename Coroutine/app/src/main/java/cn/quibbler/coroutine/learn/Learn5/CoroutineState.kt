package cn.quibbler.coroutine.learn.Learn5

sealed class CoroutineState {

    /**
     * Considering that the job has the ability to add cancel callbacks and complete callbacks, it is also necessary to add new members for these states. Therefore, it must be declared as class. Do not declare as object.
     */
    class Incomplete : CoroutineState()

    class Cancelling : CoroutineState()

    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CoroutineState()

}
