package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

suspend fun main() {
    //Channal().test()

    broadcastChannel()
}

class Channal {

    /**
     * 启动一个生产者协程，可以用来接收数据
     */
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        repeat(100) {
            delay(100)
            send(it)
        }
    }

    /**
     * 启动一个消费者协程，向里面发送数据
     */
    val sendChannel: SendChannel<Int> = GlobalScope.actor {
        while (true) {
            val element = receive()
            println(element)
        }
    }

    /**
     * public interface Channel<E> : SendChannel<E>, ReceiveChannel<E>
     */
    val channel = Channel<Int>()

    private suspend fun testProducerAndActor() {
        receiveChannel.receive()

        sendChannel.send(10)
    }

    private fun closeChannel() {

        channel.close()

        //isClosedForSend立即为true
        channel.isClosedForSend

        //但是isClosedForReceive等所有元素都被读取完毕之后才返回true
        channel.isClosedForReceive

    }

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

@OptIn(ObsoleteCoroutinesApi::class)
suspend fun broadcastChannel() {
    //广播Channel，一对多，多个接收端不存在互斥
    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)

    //订阅广播
    val receiverChannel = broadcastChannel.openSubscription()

    //val v = receiverChannel.receive()

    val producer = GlobalScope.launch {
        List(3) {
            delay(100)
            broadcastChannel.send(it)
        }
        broadcastChannel.close()
    }

    List(3) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (i in receiveChannel) {
                println("[#$index] received:$i")
            }
        }
    }.joinAll()
}

suspend fun convertNormalChannelToBroadChannel(){
    //普通 Channel
    val channel = Channel<Int>()

    //创建一个缓冲区大小为3的 BroadcastChannel
    val broadcastChannel = channel.broadcast(3)

    //创建的broadcastChannel与原来的channel是级联关系
}

suspend fun broadcastChannelProducer(){

    val broadcastProducer = GlobalScope.broadcast<Int> {

    }





}