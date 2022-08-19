package com.example.androidNewReader.data.repository

import com.example.androidNewReader.data.model.News
import com.example.androidNewReader.data.service.NewsService
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {

    suspend fun getNews(
        apiKey: String,
        country: String,
        page: Int,
        pageSize: Int,
        searchKeyword: String
    ): Response<News> {
        return newsService.getNews(apiKey, country, page, pageSize, searchKeyword)
    }

}
