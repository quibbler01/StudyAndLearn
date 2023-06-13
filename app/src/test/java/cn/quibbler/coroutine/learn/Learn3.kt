package cn.quibbler.coroutine.learn

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine


class Learn3 {

    /**
     * Create a coprocessor
     */
    val continuation = suspend {
        print("In coroutine.")
        5
    }.createCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            print("Coroutine End: $result")
        }
    })

    /**
     * hello()是()->Int这个类型的方法对象的扩展函数
     * @receiver (() -> Int)
     */
    fun (() -> Int).hello() {

    }

    /**
     * ()->Int 类型的方法
     * @return Int
     */
    fun test(): Int {
        return 5
    }

    /**
     * ()->Int类型的函数对象
     */
    val testVal = {
        5
    }.hello()   //可以调用hello()扩展方法


}