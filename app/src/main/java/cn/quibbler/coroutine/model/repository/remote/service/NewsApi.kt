package cn.quibbler.coroutine.model.repository.remote.service

import retrofit2.http.GET

interface NewsApi {

    @GET("")
    suspend fun getNewList(): String

}