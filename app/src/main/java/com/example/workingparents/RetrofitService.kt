package com.example.workingparents

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {


    //--------------------------User---------------------------//
    @GET("user/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @FormUrlEncoded
    @PUT("user/{id}")
    fun putUserToken(@Path("id") id: String, @Field("token") token: String): Call<Int>

    @FormUrlEncoded
    @POST("user/{id}")
    fun postUser(
        @Path("id") id: String,
        @Field("pw") pw: String,
        @Field("email") email:String,
        @Field("sex") sex:String,
        @Field("token") token:String
    ): Call<Int>

    @GET("user/{id}")
    fun getUserbyEmail(@Path("email") email: String ) : Call<User>






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