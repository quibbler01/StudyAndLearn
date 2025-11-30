package cn.quibbler.coroutine.jitpack.recyclerview

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MockData(val title: String, val content: String) {

    companion object {

        fun mockSingleData(): MockData {
            return MockData("", "")
        }

        fun mockListData(times: Int = 1): MutableList<MockData> {
            return ArrayList<MockData>().apply {
                repeat(times) {
                    add(mockSingleData())
                }
            }
        }

    }

    suspend fun getData(): String = coroutineScope {
        val deferred = async {
            delay(100)
            "value"
        }
        val result = deferred.await()

        result
    }

    suspend fun getFlow(): Flow<String> = flow {
        emit("A")
        emit("B")
        emit("C")
        emit("D")
    }.flowOn(Dispatchers.Default)

}