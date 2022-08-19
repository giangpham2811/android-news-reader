package com.example.codingtest.ui.newsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codingtest.data.model.Article
import com.example.codingtest.databinding.FragmentNewsDetailBinding
import com.example.codingtest.extentions.displayDateTime
import com.example.codingtest.extentions.loadImage
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding

    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
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
        sharedElementEnterTransition = MaterialContainerTransform()
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
    }

    private fun initData() {
        article = NewsDetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
    }

    private fun initView() {
        setupAppBar()
        setupArticleView()
    }


    private fun setupArticleView() {
        binding.apply {
            imageNews.apply {
                loadImage(article.urlToImage)
                transitionName = article.urlToImage
            }
            textTitle.apply {
                text = article.title
                transitionName = article.title
            }
            textContent.apply {
                text = article.description
                transitionName = article.description
            }
            textUpdatedTime.apply {
                text = article.publishedAt?.displayDateTime()
                transitionName = article.publishedAt?.displayDateTime()
            }
        }
    }

    private fun setupAppBar() {
        binding.apply {
            appbar.apply {
                textViewTitle.text = null
                textViewBack.isVisible = true
                viewSearchBar.layoutSearch.isVisible = false
                toolbar.setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
                textViewBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }
}