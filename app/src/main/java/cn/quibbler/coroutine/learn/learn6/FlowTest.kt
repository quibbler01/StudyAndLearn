package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

suspend fun main() {
    cancelFlow()

    FlowTest().intFlow.flowOn(Dispatchers.IO).catch {
        //Flow异常处理
        println("Caught error: $it")
    }.collect {
        println(it)
    }.runCatching {

    }

    flow<Int> {
        emit(1)
        throw Exception("throw exception in flow!")
    }.catch {
        //Flow异常处理
        println("Caught error: $it")
    }.collect()


    /**
     * flow完成回调onCompletion
     */
    flow<Int> {
        emit(1)
    }.onCompletion {
        println("Finally.")
    }.collect()

    val flow = flow<Int> {
        emit(1)
    }.onEach {
        //对于每个一抽取到在这里单独执行具体的操作
        println(it)
    }

    //消费和触发分离
    flow.collect()

}

suspend fun cancelFlow() {
    val job = GlobalScope.launch {
        val intFlow = flow {
            (0..3).forEach {
                delay(1000)
                emit(it)
            }
        }

        intFlow.collect {
            println("cancelFlow $it")
        }
    }

    //wait for 2.5s
    delay(2500)

    //cancel job Coroutine
    job.cancelAndJoin()
}

//
val bufferFLow = flow {
    List(100) {
        emit(it)
    }
}.buffer()  //也可以为buffer指定一个capacity容量

suspend fun conflateFlow() {
    val conflateFlow = flow {
        List(100) {
            emit(it)
        }
    }.conflate()

    conflateFlow.collect {
        println("Collecting $it")
        delay(100)
        println("$it collected")
    }
}


suspend fun convertFlowToCollection() {

    flow<Int> {
        List(5) {
            emit(it)
        }.map {
            it
        }
    }

}

class FlowTest {

    /**
     * 序列生成器
     */
    val ints = sequence {
        (1..3).forEach {
            yield(it)
        }
    }

    val intFlow = flow {
        (1..3).forEach {
            //emit不是线程安全的
            emit(it)
            kotlinx.coroutines.delay(500)
        }
    }


}