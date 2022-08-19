package com.example.codingtest.ui.newslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.codingtest.R
import com.example.codingtest.data.model.Article
import com.example.codingtest.databinding.WidgetArticleItemBinding
import com.example.codingtest.extentions.displayDateTime
import com.example.codingtest.extentions.loadImage

class ArticlePagerAdapter(private val onItemClick: (Article, FragmentNavigator.Extras) -> Unit) :
    PagingDataAdapter<Article, ArticlePagerAdapter.ArticleViewHolder>(ArticleComparator) {

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        getItem(position)?.let { article ->
            holder.bind(article, onItemClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WidgetArticleItemBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    class ArticleViewHolder(private val binding: WidgetArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var pairTimeUpdated: Pair<View, String>
        private lateinit var pairContent: Pair<View, String>
        private lateinit var pairTitle: Pair<View, String>
        private lateinit var pairImage: Pair<View, String>

        fun bind(article: Article, onItemClick: (Article, FragmentNavigator.Extras) -> Unit) {
            binding.cardView.animation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.list_animator)
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
                    text = article.content
                    transitionName = article.content
                }
                textTimeUpdated.apply {
                    text = article.publishedAt?.displayDateTime()
                    transitionName = article.publishedAt?.displayDateTime()
                }
                setUpPair(binding, article)
                root.setOnClickListener {
                    onItemClick.invoke(
                        article,
                        FragmentNavigatorExtras(pairImage, pairTitle, pairContent, pairTimeUpdated)
                    )
                }
            }
        }

        private fun setUpPair(binding: WidgetArticleItemBinding, article: Article) {
            binding.apply {
                pairImage = Pair(imageNews as View, article.urlToImage ?: "")
                pairTitle = Pair(textTitle as View, article.title ?: "")
                pairContent = Pair(textContent as View, article.content ?: "")
                pairTimeUpdated =
                    Pair(textTimeUpdated as View, article.publishedAt?.displayDateTime() ?: "")
            }

        }
    }


    object ArticleComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.content == newItem.content
        }
    }
}
