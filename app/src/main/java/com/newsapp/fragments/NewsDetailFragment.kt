package com.newsapp.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textview.MaterialTextView
import com.newsapp.R
import com.newsapp.databinding.FragmentNewsDetailBinding
import com.newsapp.models.news.Article
import com.newsapp.utils.CommonMethods
import com.newsapp.utils.CommonMethods.getFormattedDate
import com.newsapp.utils.CommonMethods.setDislike
import com.newsapp.utils.CommonMethods.setLike
import com.newsapp.utils.Utils.openBrowser
import com.newsapp.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private lateinit var binding: FragmentNewsDetailBinding
    private val viewModel by lazy { activityViewModels<HomeViewModel>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsDetailBinding.bind(view)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.apply {
            val item=viewModel.value.selectedItem.value!!
            moreInfo.setOnClickListener {
                val url = item.url
                if (!url.isNullOrEmpty())
                    context?.let { it1 -> openBrowser(it1, url) }
            }

            like.setOnClickListener {
                doLike(item)
                if (item.isAlreadyDislike) {
                    doDislike(item)
                }
            }

            dislike.setOnClickListener {
                doDislike(item)
                if (item.isAlreadyLike) {
                    doLike(item)
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.value.selectedItem.observe(viewLifecycleOwner) {
            binding.apply {
                context?.let { it1 ->
                    CommonMethods.setThumbnail(
                        it1,
                        it.urlToImage,
                        image,
                        R.drawable.ic_launcher_background
                    )
                }
                title.text = it.title
                setDataIfNotNull(description, it.description)
                setDataIfNotNull(content, it.content)
                source.text = getString(R.string.source, it.source.name)
                date.text = getFormattedDate(it.publishedAt)
                setLike(it.isAlreadyLike, like)
                setDislike(it.isAlreadyDislike, dislike)
            }
        }
    }

    private fun setDataIfNotNull(textView: MaterialTextView, data: String?) {
        textView.visibility = if (data.isNullOrEmpty()) GONE
        else {
            textView.text = data
            VISIBLE
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


}