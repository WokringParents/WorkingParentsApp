package com.example.workingparents

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object FCMRetrofitBuilder {

    var api: FCMService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FCMService::class.java)
    }

    fun takeJsonObject (to:String, title:String, body:String) :JsonObject{

        val obj = JsonObject()
        val notification = JsonObject()

        notification.addProperty("title", title)
        notification.addProperty("body", body)

        obj.addProperty("to",to)
        obj.add("notification", notification)

        return obj
    }
}

