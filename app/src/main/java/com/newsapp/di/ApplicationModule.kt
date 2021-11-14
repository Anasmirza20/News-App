package com.newsapp.di

import android.content.Context
import com.newsapp.apis.ApiInterface
import com.newsapp.apis.Urls.BASE_URL
import com.newsapp.utils.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getSharedPrefInstance(@ApplicationContext context: Context) = SharedPref(context)

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {
        val okHttpClient = OkHttpClient()
        val interceptor = HttpLoggingInterceptor()
        val timeout: Long = 120
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.newBuilder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)
}
