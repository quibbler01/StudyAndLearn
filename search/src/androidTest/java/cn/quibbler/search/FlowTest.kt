package cn.quibbler.search

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test

class FlowTest {

    @Test
    fun continouslyCollect() {
        val values = mutableListOf<Int>()

        val flow = flow<Int> {
            emit(10)
        }.catch {
            if (it is RuntimeException) throw it
        }

        GlobalScope.launch {
            flow.collect {
                values.add(it)
                assertEquals(1, values[0])
            }
        }

    }

}