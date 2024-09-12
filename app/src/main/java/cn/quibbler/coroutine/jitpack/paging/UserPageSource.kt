package cn.quibbler.coroutine.jitpack.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.quibbler.coroutine.jitpack.parcelize.ExternalClass
import cn.quibbler.coroutine.jitpack.parcelize.User

/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-network-db?hl=zh-cn#room-entities
 */
class UserPageSource : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val nextPageNumber = params.key ?: 1
        return LoadResult.Page(
            data = randomUsers(),
            prevKey = null,
            nextKey = nextPageNumber + 1
        )
    }

    private fun randomUsers(): List<User> {
        val list = ArrayList<User>()
        for (i in 0..10) {
            list.add(User("Zhao", "Peng", 32, ExternalClass(10086)))
        }
        return list
    }

}