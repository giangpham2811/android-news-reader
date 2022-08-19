package com.example.codingtest.data.repository

import com.example.codingtest.data.model.News
import com.example.codingtest.data.service.NewsService
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
