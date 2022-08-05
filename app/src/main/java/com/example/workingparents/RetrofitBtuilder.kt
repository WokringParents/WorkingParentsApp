package com.example.workingparents

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: RetrofitService

    init {
        var gson=GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://workingparents-env-1.eba-ysfya3ek.ap-northeast-2.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}