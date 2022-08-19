package com.example.codingtest.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.codingtest.data.model.Article
import com.example.codingtest.data.repository.NewsRepository

class NewsPagingSource(
    private val newsRepository: NewsRepository,
    private val apiKey: String,
    private val country: String,
    private val searchKeyword: String
) : PagingSource<Int, Article>() {

    companion object {
        const val PAGE_SIZE = 10
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val response = newsRepository.getNews(
                apiKey = apiKey,
                country = country,
                page = page,
                pageSize = pageSize,
                searchKeyword = searchKeyword
            )
            val data = response.body()!!.articles
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}