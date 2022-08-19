package com.example.codingtest.data.usercase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.codingtest.data.model.Article
import com.example.codingtest.data.repository.NewsRepository
import com.example.codingtest.data.source.NewsPagingSource
import javax.inject.Inject

class NewsUserCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    fun execute(
        apiKey: String,
        country: String,
        searchKeyword: String,
    ): Pager<Int, Article> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsRepository, apiKey, country, searchKeyword)
            },
            initialKey = 1
        )
    }
}