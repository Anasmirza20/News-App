package com.newsapp.models.news

data class Article(
    val author: String,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String?,
    val urlToImage: String?,
    var isAlreadyLike: Boolean = false,
    var isAlreadyDislike: Boolean = false,
)