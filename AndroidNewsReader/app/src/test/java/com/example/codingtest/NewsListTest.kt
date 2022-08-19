package com.example.codingtest

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.codingtest.data.model.Article
import com.example.codingtest.data.model.News
import com.example.codingtest.data.repository.NewsRepository
import com.example.codingtest.data.source.NewsPagingSource
import com.example.codingtest.data.usercase.NewsUserCase
import com.example.codingtest.ui.newslist.NewsListViewModel
import io.github.serpro69.kfaker.Faker
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsListTest {

    private lateinit var faker: Faker

    @MockK
    lateinit var newsRepository: NewsRepository

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
        faker = Faker()
    }

    @Test
    fun loadListNewsWhenSuccess() {
        val result = mockNewsResponse(faker)

        coEvery {
            newsRepository.getNews("", NewsListViewModel.DEFAULT_SEARCH_COUNTRY, 1, NewsPagingSource.PAGE_SIZE, "")
                .body()
        }.returns(result)

        val newsUserCase = NewsUserCase(newsRepository)
        val newsResult = newsUserCase.execute(BuildConfig.API_KEY, NewsListViewModel.DEFAULT_SEARCH_COUNTRY, "")

        assertEquals(
            expected = newsResult,
            actual = Pager(
                config = PagingConfig(
                    pageSize = NewsPagingSource.PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    NewsPagingSource(newsRepository, BuildConfig.API_KEY, NewsListViewModel.DEFAULT_SEARCH_COUNTRY, "")
                },
                initialKey = 1
            )
        )
    }

    private fun mockNewsResponse(faker: Faker): News {
        val newsList = mockArticlesData(faker)
        return News(
            totalResults = faker.random.nextInt(),
            status = "ok",
            articles = newsList
        )
    }

    private fun mockArticlesData(faker: Faker): List<Article> {
        return (1..NewsPagingSource.PAGE_SIZE).map { faker.randomProvider.randomClassInstance<Article>() }.toList()
    }
}