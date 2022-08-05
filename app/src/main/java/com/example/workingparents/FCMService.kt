package com.example.workingparents

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FCMService {

    //헤더에 key는 우리 파이어베이스 서버키이다

    @Headers("Content-Type: application/json","Authorization: key=AAAA3j1gJr8:APA91bH5ASwfTakfRRdHWuiFnXFAmI8ag35vcl3h5sUeAzALd2ln7A5PZ5i_y66ypS2Qe13cf7bK1RXCwtXtwpdYKvck8iIbUujiDGag-I_MghODXrGaBfvHqEeNsVRvUXHcs2TlIZA0")
    @POST("fcm/send")
    fun pushAlram(@Body body: String):Call<ResponseBody>

}