package com.newsapp.apis

import com.newsapp.apis.Urls.LOGIN_API
import com.newsapp.apis.Urls.NEWS_API
import com.newsapp.models.news.NewsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiInterface {

    @GET(LOGIN_API)
    suspend fun login(): ResponseBody

    @GET(NEWS_API)
    suspend fun getNews(): NewsResponse
}