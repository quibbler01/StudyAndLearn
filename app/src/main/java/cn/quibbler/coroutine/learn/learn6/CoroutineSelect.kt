package cn.quibbler.coroutine.learn.learn6

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.select

fun main() {

}

class CoroutineSelect {

    suspend fun test() {

        val channels = List(10) {
            Channel<Int>()
        }

        val result = select<Int?> {
            channels.forEach {
                it.onReceive {
                    it
                }
            }
        }

    }

}