package com.example.workingparents

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {


    //GET 예제
    @GET("user/{id}")
    fun getUser(@Path("id") id: String): Call<User>

   // @GET("posts/{page}")
   // fun getUserPage(@Path("page") page: String): Call<User>


//    @GET("posts/1")
//    fun getStudent(@Query("school_id") schoolId: Int,
//                   @Query("grade") grade: Int,
//                   @Query("classroom") classroom: Int): Call<ExampleResponse>
//
//
//    //POST 예제
//    @FormUrlEncoded
//    @POST("posts")
//    fun getContactsObject(@Field("idx") idx: String): Call<JsonObject>
}