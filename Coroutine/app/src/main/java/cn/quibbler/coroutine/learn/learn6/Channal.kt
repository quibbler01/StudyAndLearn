package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    Channal().test()
}

class Channal {

    suspend fun test() {

        /**
         * 并发安全的队列，用来连接协程
         * 实现不同协程的通信
         *
         * 默认创建的Channel是没有缓冲区的，大小RENDEZVOUS为0。send发生和接收receive会相互为对方阻塞。
         */
        val channel = Channel<Int>()

        val producer = GlobalScope.launch {
            var i = 0
            while (true) {
                //delay(10000)
                //也会挂起，当元素队列满了，缓冲区存满。send就挂起，直到接收者取走数据。
                channel.send(i++)
            }
        }

        val consumer = GlobalScope.launch {
            while (true) {
                delay(1000)
                val element = channel.receive()
                println(element)
            }
        }

        producer.join()
        consumer.join()

    }

    suspend fun iteratorChannel() {

        val channel = Channel<Int>()

        val producer = GlobalScope.launch {
            var i = 0
            while (true) {
                channel.send(i++)
            }
        }

        val consumer = GlobalScope.launch {
            //可以用迭代器来访问Channel通道
            val iterator = channel.iterator()

            while (iterator.hasNext()) {
                val element = iterator.next()
                println(element)
                delay(2000)
            }
        }

        val consumer1 = GlobalScope.launch {
            for (element in channel) {
                //简化成for ... in ...
                println(element)
                delay(2000)
            }
        }

    }

}