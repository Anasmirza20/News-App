package com.newsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.adapters.NewsAdapter
import com.newsapp.databinding.FragmentHomeBinding
import com.newsapp.utils.Constants
import com.newsapp.utils.SharedPref
import com.newsapp.utils.Utils
import com.newsapp.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var newsAdapter: NewsAdapter? = null
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    @Inject
    lateinit var session: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (session.getBoolean(Constants.LOGIN_KEY) == false) {
            findNavController().navigate(R.id.loginFragment)
            return
        }
        binding = FragmentHomeBinding.bind(view)
        setObservers()
        setupRecyclerView()
        setMenuListeners()
        if (viewModel.newsList.value == null)
            viewModel.getNews()
    }

    private fun setObservers() {
        viewModel.newsList.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (it != null)
                newsAdapter?.submitList(it)
        }
    }

    private fun setMenuListeners() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.github -> {
                    context?.let { Utils.openBrowser(it, getString(R.string.github_link)) }
                    true
                }
                R.id.linkedin -> {
                    context?.let { Utils.openBrowser(it, getString(R.string.linkedin_url)) }
                    true
                }
                R.id.logout -> {
                    session.putBoolean(Constants.LOGIN_KEY, false)
                    findNavController().navigate(R.id.loginFragment)
                    true
                }
                R.id.contact_me -> {
                    context?.let { Utils.openDialPad(it) }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = context?.let {
            NewsAdapter(object : NewsAdapter.NewsCallback {
                override fun videoCallback(position: Int) {
                    viewModel.selectedItem.value = viewModel.newsList.value?.get(position)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment())
                }
            }, it)
        }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }
}