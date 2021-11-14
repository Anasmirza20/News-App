package com.newsapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.models.news.Article
import com.newsapp.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : ViewModel() {

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>> get() = _newsList

    val selectedItem = MutableLiveData<Article>()
    val likeCount = MutableLiveData<Int>()
    val dislikeCount = MutableLiveData<Int>()

    fun getNews() {
        viewModelScope.launch {
            kotlin.runCatching { repository.getNews() }.onSuccess {
                _newsList.value = it.articles
            }.onFailure {
                //TODO: Handle Exception Here
            }
        }
    }

    suspend fun login() = repository.login()
}