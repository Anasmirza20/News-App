package com.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.R
import com.newsapp.databinding.NewsItemLayoutBinding
import com.newsapp.models.news.Article
import com.newsapp.utils.CommonMethods.setDislike
import com.newsapp.utils.CommonMethods.setLike
import com.newsapp.utils.CommonMethods.setThumbnail

class NewsAdapter(
    private val newsCallback: NewsCallback?,
    private val context: Context
) : ListAdapter<Article, NewsAdapter.LiveVideoViewHolder>(VideoDiffCallback()) {

    inner class LiveVideoViewHolder(private val binding: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    newsCallback?.videoCallback(adapterPosition)
                }

                like.setOnClickListener {
                    val item = getItem(adapterPosition)
                    doLike(item)
                    if (item.isAlreadyDislike) {
                        doDislike(item)
                    }
                }

                dislike.setOnClickListener {
                    val item = getItem(adapterPosition)
                    doDislike(item)
                    if (item.isAlreadyLike) {
                        doLike(item)
                    }
                }
            }
        }

        private fun doLike(item: Article) {
            item.isAlreadyLike = !item.isAlreadyLike
            setLike(item.isAlreadyLike, binding.like)
        }

        private fun doDislike(item: Article) {
            item.isAlreadyDislike = !item.isAlreadyDislike
            setDislike(item.isAlreadyDislike, binding.dislike)
        }

        fun bind(data: Article) {
            binding.apply {
                setThumbnail(
                    context,
                    data.urlToImage,
                    image,
                    R.drawable.ic_launcher_background
                )
                title.text = data.title
                time.text = context.getString(R.string.min_ago, adapterPosition.toString())
                setLike(data.isAlreadyLike, like)
                setDislike(data.isAlreadyDislike, dislike)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LiveVideoViewHolder(
        NewsItemLayoutBinding.inflate(from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: LiveVideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VideoDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

    interface NewsCallback {
        fun videoCallback(position: Int)
    }
}