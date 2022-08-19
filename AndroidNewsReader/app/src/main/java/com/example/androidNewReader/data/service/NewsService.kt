package com.example.androidNewReader.data.service

import com.example.androidNewReader.data.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("q") searchKeyword: String
    ): Response<News>
}