package cn.quibbler.coroutine.learn.Learn5

class CancelCoroutine {

    /**
     * 协程的取消状态
     */
    sealed class CancelState {

        object InComplete : CancelState()

        /**
         * 比InComplete，只是多了一个OnCancel回调
         * @property onCancel OnCancel
         * @constructor
         */
        class CancelHandler(val onCancel: OnCancel) : CancelState()

        class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CancelState()

        object Cancelled : CancelState()

    }

    enum class CancelDecision {
        UNDECIDED, SUSPENDED, RESUMED
    }


}