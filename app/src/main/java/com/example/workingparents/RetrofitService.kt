package com.example.workingparents

import retrofit2.Call
import retrofit2.http.*
import java.sql.Timestamp

interface RetrofitService {


    //--------------------------User---------------------------//
    @GET("user/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @GET("useremail/{email}")
    fun getUserbyEmail(@Path("email") email: String ) : Call<User>


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


    //--------------------------CoupleCode---------------------------//
    @FormUrlEncoded
    @POST("couplecode/{code}")
    fun postCode(
        @Path("code") code: String,
        @Field("id") id: String
    ): Call<Int>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/couplecode/delete", hasBody = true)
    fun deleteCode(@Field("code") code: String): Call<Int>

    @GET("couplecode/{code}")
    fun getSpouseID(@Path("code") code: String): Call<String>

    //--------------------------Couple---------------------------//
    @FormUrlEncoded
    @POST("couple")
    fun postCouple(
        @Field("mid") mid: String,
        @Field("did") did: String
    ): Call<Int>

    @GET("couple/{id}")
    fun getCouplebyID(@Path("id")id: String):Call<Couple>
    //커플을 등록하면 user에 couplenum에 자동으로 올려주는것도 괜찮을거 같다. 나중에 조인연산을 줄일 수 있을 거같음


    //--------------------------Posting---------------------------//


    @FormUrlEncoded
    @POST("posting/{pid}")
    fun postPosting(

        @Path("pid") pid: String,
        @Field("village") village: String,
        @Field("goback") goback: String,
        @Field("content") content: String

    ): Call<Int>

    @GET("/posting/all")
    fun getPosting():Call<List<Posting>>




    //--------------------------SharingList---------------------------//

     @GET("sharinglist")
     fun getSharingList(
         @Query("couplenum") couplenum: Int,
         @Query("startdate") startdate: String,
         @Query("enddate") enddate: String
     ):Call<List<SharingList>>


    @FormUrlEncoded
    @POST("sharinglist")
    fun postSharingList(
        @Field("couplenum") couplenum: Int,
        @Field("sdate") sdate: String,
        @Field("content") content: String,
        @Field("daily") daily: Boolean,
        @Field("enddate") enddate: String
    ):Call<Int>

    @FormUrlEncoded
    @PUT("sharinglist/mdo")
    fun putMaleDo(@Field("couplenum") couplenum: Int, @Field("sdate") sdate: Timestamp): Call<Int>

    @FormUrlEncoded
    @PUT("sharinglist/fdo")
    fun putFemaleDo(@Field("couplenum") couplenum: Int, @Field("sdate") sdate: Timestamp): Call<Int>





    //--------------------------calendar---------------------------//

    @GET("calendar/{couplenum}")
    fun getCalendar(@Path("couplenum") couplenum: Int): Call<List<Calendar>>


    @FormUrlEncoded
    @POST("calendar/{couplenum}")
    fun postCalender(
        @Path("couplenum") couplenum: Int,
        @Field("cdate") cdate:String,
        @Field("ctitle") ctitle:String,
        @Field("ccontent") ccontent:String,
        @Field("csex") csex: String?
    ): Call<Int>


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