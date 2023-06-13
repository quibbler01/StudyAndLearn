package cn.quibbler.coroutine.learn

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.*

interface AsyncScope

class AsyncCoroutine(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<Unit>, AsyncScope {
    override fun resumeWith(result: Result<Unit>) {
        result.getOrNull()
    }
}

fun async(context: CoroutineContext = EmptyCoroutineContext, block: suspend AsyncScope.() -> Unit) {
    val completion = AsyncCoroutine(context)
    block.startCoroutine(completion, completion)
}

suspend fun <T> AsyncScope.await(block: () -> Call<T>) = suspendCoroutine<T> { continuation ->
    val call = block()
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let(continuation::resume) ?: continuation.resumeWithException(NullPointerException())
            } else {
                continuation.resumeWithException(HttpException(response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            continuation.resumeWithException(t)
        }
    })
}

class AsyncAwait {

}