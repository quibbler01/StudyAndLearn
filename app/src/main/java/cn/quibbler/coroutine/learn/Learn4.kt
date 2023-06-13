package cn.quibbler.coroutine.learn

import kotlinx.coroutines.yield
import kotlin.coroutines.Continuation

fun main(vararg args: String): Unit {
    for (element in Learn4().sequence) {
        println(element)
    }
}

/*
val nums = generator { start: Int ->
    for (i in 0..5) {
        yield(start + i)
    }
}
*/

interface Generator<T> {
    operator fun iterator(): Iterable<T>
}

/*fun <T> generator(block: suspend Generator<T>.(T) -> Unit): (T) -> Generator<T> {
    return { parameter ->
        GeneratorImpl(block, parameter)
    }
}*/

sealed class State {

    class NoteReady(val continuation: Continuation<Unit>) : State()

    class Ready<T>(val continuation: Continuation<Unit>, val nextValue: T) : State()

    object Done : State()

}

/*class GeneratorIterator<T>{
    private val block : suspend GeneratorScope<T>.(T) ->Unit,
}*/

class Learn4 {

    /**
     * 标准库中的序列生成器
     */
    val sequence = sequence {//传入的参数就是一个协程体，序列生成的执行体
        yield(1)
        yield(2)
        yield(3)
        yield(4)
        yieldAll(listOf(1, 2, 3, 4))
    }   //返回的就是一个迭代器

}

class User {}

suspend fun getUser(): User {
    return getUserLocal() ?: getUserRemote()
}

suspend fun getUserRemote(): User {
    return User()
}

suspend fun getUserLocal(): User? {
    return null
}
