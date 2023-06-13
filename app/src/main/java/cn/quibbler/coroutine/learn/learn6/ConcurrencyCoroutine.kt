package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger


suspend fun main() {
    test()

    testSafe()

    testWithMutexLock()

    testWithSemaphore()
}

suspend fun test() {

    var count = 0

    List(1000) {
        GlobalScope.launch {
            //not safe
            count++
        }
    }.joinAll()

    println("test = $count")

}


suspend fun testSafe() {

    //声明为原子类型，确保都是原子操作
    val count = AtomicInteger(0)

    List(1000) {
        GlobalScope.launch {
            count.incrementAndGet()
        }
    }.joinAll()

    println("testSafe = $count")

}

suspend fun testWithMutexLock(){
    var count = 0
    val mutex = Mutex()
    List(1000){
        GlobalScope.launch {
            mutex.withLock {
                ++count
            }
        }
    }.joinAll()
    println("testWithMutexLock = $count")
}

suspend fun testWithSemaphore(){
    var count = 0
    val semaphore = Semaphore(1)
    List(1000){
        GlobalScope.launch {
            semaphore.withPermit {
                ++count
            }
        }
    }.joinAll()
    println("testWithSemaphore = $count")
}

class ConcurrencyCoroutine {


}