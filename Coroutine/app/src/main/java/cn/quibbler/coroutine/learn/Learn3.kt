package cn.quibbler.coroutine.learn

import kotlin.concurrent.thread
import kotlin.coroutines.*

/**
 * main函数也可以声明为挂起函数，启动就获取一个协程，所有程序都运行再这个协程体内。
 *
 * @param args Array<String>
 */
suspend fun main(args: Array<String>) {
    /**
     * 先创建一个协程，再执行
     */
    Learn3().continuation.resume(Unit)

    /**
     * 直接启动一个协程
     */
    suspend {
        "Hello Quibbler"
    }.startCoroutine(object : Continuation<String> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            print("directly start coroutine, result is $result")
        }
    })

    /**
     * 使用协程构造器创建一个协程
     */
    launchCoroutine(ProducerScope<Int>()) {
        produce(1)

        //Restricted suspending functions can only invoke member or extension suspending functions on their restricted coroutine scope
        //test()
    }

    Learn3().addCoroutineContext()
}

suspend fun test() {

}

/**
 * 通过反射，在普通函数中调用挂起函数。
 */
fun notSuspend() {
    val ref = ::test
    val result = ref.call(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("resume with :${result.getOrNull()}")
        }
    })

}

/**
 * 启动协程，协程的构造器，启动器。
 * @param receiver R
 * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<R, T>
 */
fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
    block.startCoroutine(receiver, object : Continuation<T> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
            println("Coroutine End : $result")
        }
    })
}

/**
 * 模拟一个生成器协程的作用域，使用它来创建一个协程
 *
 * RestrictsSuspension注解的作用域，协程体内无法调用其它外部挂起函数，只能调用这个作用域内部的挂起函数。
 * @param T
 */
@RestrictsSuspension
class ProducerScope<T> {
    suspend fun produce(value: T) {

    }
}

/**
 * 函数分为：挂起函数、普通函数.
 *
 * 挂起函数可以调用所有函数.
 *
 * 普通函数只能调用普通函数.
 *
 * @property continuation Continuation<Unit>
 * @property testVal Unit
 */
class Learn3 {

    /**
     * 创建一个协程体
     */
    val continuation = suspend {
        print("In coroutine.\n")
        5
    }.createCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            print("Coroutine End: $result\n")
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


    /**
     * 同步返回
     * 类型： suspend(Int)->Unit
     * @param a Int
     */
    suspend fun suspendFunc01(a: Int) {

    }

    /**
     * 异步逻辑
     * 类型：suspend(String,String)->Int
     *
     * @param a String
     * @param b String
     * @return Int
     */
    suspend fun suspendFunc02(a: String, b: String) = suspendCoroutine<Int> { continuation ->
        thread {
            //回复调用
            continuation.resumeWith(Result.success(5))
            //或者回复调用的扩展函数
            continuation.resume(6)
        }
    }

    var list = emptyList<Int>()

    fun t() {
        list += 0

        list += listOf(1, 2, 3)
    }

    var coroutineContextTmp: CoroutineContext = EmptyCoroutineContext

    class CoroutineName(val name: String) : AbstractCoroutineContextElement(Key) {
        companion object Key : CoroutineContext.Key<CoroutineName>
    }

    class CoroutineExceptionHandler(val onErrorAction: (Throwable) -> Unit) : AbstractCoroutineContextElement(Key) {
        companion object Key : CoroutineContext.Key<CoroutineExceptionHandler>

        fun onError(error: Throwable) {
            error.printStackTrace()
            onErrorAction(error)
        }
    }

    fun addCoroutineContext() {
        coroutineContextTmp += CoroutineName("co-01")

        coroutineContextTmp += CoroutineExceptionHandler {
            //
        }

        //or
        coroutineContextTmp += CoroutineName("co-01") + CoroutineExceptionHandler {
            //
        }

        suspend {
            //在协程体内部获取全局上下文
            println("In Coroutine [${coroutineContext[CoroutineName]}]")

            println("is equal:${coroutineContext == coroutineContextTmp}")

            1
        }.startCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = coroutineContextTmp

            override fun resumeWith(result: Result<Int>) {
                result.onFailure {
                    context[CoroutineExceptionHandler]?.onError(it)
                }
            }
        })
    }


}