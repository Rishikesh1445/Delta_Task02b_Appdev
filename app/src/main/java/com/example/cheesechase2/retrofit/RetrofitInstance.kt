package com.example.cheesechase2.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: jerryAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://chasedeux.vercel.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(jerryAPI::class.java)
    }
}