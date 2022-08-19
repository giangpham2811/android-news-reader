package com.example.codingtest.ui.newslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.codingtest.R
import com.example.codingtest.databinding.FragmentNewsListBinding
import com.example.codingtest.extentions.textChanges
import com.example.codingtest.ui.newslist.adapter.ArticlePagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.FlowPreview
class NewsListFragment : Fragment() {

    private val viewModel: NewsListViewModel by viewModels()

    private lateinit var binding: FragmentNewsListBinding

    private var snackbar: Snackbar? = null

    private lateinit var articlePagerAdapter: ArticlePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initTransition()
    }

    private fun initTransition() {
        sharedElementReturnTransition = MaterialContainerTransform()
    }

    private fun initData() {
        viewModel.newsListLiveData.observe(viewLifecycleOwner) { data ->
            articlePagerAdapter.submitData(lifecycle, data)
        }
    }

    private fun initView() {
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            articlePagerAdapter = ArticlePagerAdapter(onItemClick = { article, extras ->
                val direction = NewsListFragmentDirections.actionShowDetail(article)
                findNavController().navigate(direction, extras)
            }).apply {
                recyclerView.adapter = this
                recyclerView.doOnPreDraw {
                    startPostponedEnterTransition()
                }
                addLoadStateListener { loadState ->
                    if (loadState.refresh is LoadState.Loading ||
                        loadState.append is LoadState.Loading
                    ) {
                        progressBar.isVisible = true
                        textEmptyData.isVisible = false
                        snackbar?.dismiss()
                    } else {
                        progressBar.isVisible = false
                        textEmptyData.isVisible = false
                        if (loadState.append is LoadState.Error ||
                            loadState.prepend is LoadState.Error ||
                            loadState.refresh is LoadState.Error
                        ) {
                            showNetworkExceptionSnackBar(getString(R.string.error_something_went_wrong))
                        } else {
                            textEmptyData.isVisible = articlePagerAdapter.itemCount == 0
                        }
                    }
                }
            }
        }
    }


    private fun setupSearchView() {
        binding.appbar.apply {
            viewSearchBar.layoutSearch.isVisible = true
            viewSearchBar.editText.textChanges()
                .debounce(500)
                .onEach {
                    viewModel.searchNews(key = it.toString())
                }
                .launchIn(lifecycleScope)
            toolbar.navigationIcon = null
        }
    }

    private fun showNetworkExceptionSnackBar(message: String) {
        view?.let {
            snackbar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.error_retry)) {
                    articlePagerAdapter.retry()
                }
            snackbar?.show()
        }
    }
}