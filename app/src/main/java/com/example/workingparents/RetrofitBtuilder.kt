package com.example.workingparents

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: RetrofitService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://workingparents-env-1.eba-ysfya3ek.ap-northeast-2.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}