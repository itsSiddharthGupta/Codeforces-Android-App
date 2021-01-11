package com.kars.codeforcesmobile

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    var retrofitClientCodeForces: Retrofit? = null
        get() {
            if (field == null) {
                field = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(CODEFORCES_BASE_URL)
                    .client(okHttpClient())
                    .build()
            }
            return field
        }

    private fun okHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.interceptors().add(interceptor)
        return httpClient.build()
    }
}