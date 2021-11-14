package com.newsapp.repositories

import com.newsapp.apis.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun login() = apiInterface.login()

    suspend fun getNews() = apiInterface.getNews()

}