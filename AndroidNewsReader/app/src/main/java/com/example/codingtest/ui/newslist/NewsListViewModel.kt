package com.example.codingtest.ui.newslist

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.codingtest.BuildConfig
import com.example.codingtest.data.model.Article
import com.example.codingtest.data.usercase.NewsUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewsListViewModel @Inject constructor(private val newsUseCase: NewsUserCase) : ViewModel() {


    companion object {
        const val DEFAULT_SEARCH_COUNTRY = "us"
    }

    val newsListLiveData: LiveData<PagingData<Article>>
    private val searchLiveData = MutableLiveData<String>()

    init {
        newsListLiveData = searchLiveData.distinctUntilChanged().switchMap { searchKey ->
            getNewsList(searchKey)
        }
    }

    fun searchNews(key: String) {
        searchLiveData.value = key
    }

    private fun getNewsList(
        searchContent: String,
        country: String = DEFAULT_SEARCH_COUNTRY
    ): LiveData<PagingData<Article>> {
        return newsUseCase.execute(BuildConfig.API_KEY, country, searchContent).liveData.cachedIn(viewModelScope)
    }
}
