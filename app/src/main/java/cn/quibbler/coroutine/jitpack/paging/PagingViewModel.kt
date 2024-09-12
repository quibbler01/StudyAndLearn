package cn.quibbler.coroutine.jitpack.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class PagingViewModel : ViewModel() {

    val pagingFlow = Pager(config = PagingConfig(pageSize = 20)) {
        UserPageSource()
    }.flow
        .cachedIn(viewModelScope)

}