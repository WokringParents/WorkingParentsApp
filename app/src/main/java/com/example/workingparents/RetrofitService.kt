package com.example.workingparents

import com.example.workingparents.Calendar.CalendarData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {


    //--------------------------User---------------------------//
    @GET("user/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @GET("user/{village}")
    fun getTokenListByVillage(@Path("village") village: String ) : Call<List<String>>

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
        @Field("token") token:String,
        @Field("name") name:String,
        @Field("pnumber") pnumber:String,
        @Field("city") city:String,
        @Field("village") village:String,
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

    ): Call<Posting>

    @GET("postingContent/{content}")
    fun getPostingbyContent(@Path("content") pcontent: String?): Call<List<Posting>>

    @GET("/posting/all")
    fun getPosting():Call<List<Posting>>

    @GET("posting/{attribute}")
    fun getBoardPosting(
        @Path("attribute") attribute: String?
    ): Call<List<Posting>>

    @FormUrlEncoded
    @PUT("posting/ccnt/{pno}")
    fun putCommentCnt(
        @Path("pno") pno: Int,
        @Field("sign") sign: String?
    ): Call<Int> //sign에 plus or minus


    @FormUrlEncoded
    @PUT("posting/hcnt/{pno}")
    fun putHeartCnt(
        @Path("pno") pno: Int,
        @Field("sign") sign: String?
    ): Call<Int> //sign에 plus or minus                                                                                    )


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/posting/delete", hasBody = true)
    fun deleteBoardPosting(@Field("pno") pno: Int): Call<Int>

    //--------------------------SharingList---------------------------//

     @GET("sharinglist")
     fun getSharingList(
         @Query("couplenum") couplenum: Int,
         @Query("startdate") startdate: String,
         @Query("enddate") enddate: String
     ):Call<List<SharingList>>
    //get할때 param으로 주는건 query로 해야함

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
    @HTTP(method = "DELETE", path = "/sharinglist/today", hasBody = true)
    fun deleteTodaySharingList(
        @Field("couplenum") couplenum: Int,
        @Field("sdate") sdate: String,
    ): Call<Int>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/sharinglist/daily", hasBody = true)
    fun deleteDailySharingList(
        @Field("couplenum") couplenum: Int,
        @Field("content") content:String,
        @Field("startdate") startdate: String,
        @Field("enddate") enddate:String
    ): Call<Int>


    @FormUrlEncoded
    @PUT("sharinglist/mdo")
    fun putMaleDo(@Field("couplenum") couplenum: Int, @Field("sdate") sdate: String): Call<Int>

    @FormUrlEncoded
    @PUT("sharinglist/fdo")
    fun putFemaleDo(@Field("couplenum") couplenum: Int, @Field("sdate") sdate: String): Call<Int>

    @FormUrlEncoded
    @PUT("sharinglist/today")
    fun putTodayContent(
        @Field("couplenum") couplenum: Int,
        @Field("sdate") sdate:String,
        @Field("content")content:String,
    ): Call<Int>


    @FormUrlEncoded
    @PUT("sharinglist/daily")
    fun putDailyContent(
        @Field("couplenum") couplenum: Int,
        @Field("prevcontent") prevcontent:String,
        @Field("content")content:String,
        @Field("startdate")startdate:String,
        @Field("enddate")enddate:String
    ): Call<Int>



    //--------------------------calendar---------------------------//

    @GET("calendar/{couplenum}")
    fun getCalendar(@Path("couplenum") couplenum: Int): Call<List<CalendarData>>


    @FormUrlEncoded
    @POST("calendar/{couplenum}")
    fun postCalender(
        @Path("couplenum") couplenum: Int,
        @Field("cdate") cdate:String,
        @Field("ctitle") ctitle:String,
        @Field("ccontent") ccontent:String,
        @Field("csex") csex: String?
    ): Call<Int>

    @FormUrlEncoded
    @PUT("calendar/{couplenum}")
    fun putCalender(
        @Path("couplenum") couplenum: Int,
        @Field("cdate") cdate: String,
        @Field("ctitle") ctitle:String,
        @Field("ccontent") ccontent:String
    ): Call<Int>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/calendar/{couplenum}", hasBody = true)
    fun deleteCalender(@Path("couplenum") couplenum: Int,  @Field("cdate") cdate: String): Call<Int>


    //-------------------------------Comment------------------------------//

    @GET("comment/{pno}")
    fun getComment(@Path("pno") pno: Int): Call<List<Comment>>

    @FormUrlEncoded
    @POST("comment/{pno}")
    fun postComment(
        @Path("pno") pno: Int,
        @Field("cno") cno: Int,
        @Field("cid") cid: String?,
        @Field("cment") cment: String?
    ): Call<Comment>

    @FormUrlEncoded
    @PUT("comment/{pno}")
    fun putCcommentCnt(
        @Path("pno") pno: Int,
        @Field("cno") cno: Int,
        @Field("sign") sign: String?
    ): Call<Int>//sign==plus or minus


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/comment/delete", hasBody = true)
    fun deleteComment(@Field("pno") pno: Int, @Field("cno") cno: Int): Call<Int>

    
    //-------------------------------Ccomment------------------------------//

    @GET("ccomment/{pno}")
    fun getCcomment(@Path("pno") pno: Int): Call<List<Ccomment>>

    @FormUrlEncoded
    @POST("ccomment/{pno}")
    fun postCcomment(
        @Path("pno") pno: Int,
        @Field("cno") cno: Int,
        @Field("ccno") ccno: Int,
        @Field("ccid") ccid: String?,
        @Field("ccment") ccment: String?
    ): Call<Ccomment>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/ccomment/delete", hasBody = true)
    fun deleteCcomment(
        @Field("pno") pno: Int,
        @Field("cno") cno: Int,
        @Field("ccno") ccno: Int
    ): Call<Int>

    //-------------------------------Child------------------------------//

    @GET("child/all")
    fun getAllChild(): Call<List<Child>>

    @GET("child/{couplenum}")
    fun getChild(@Path("couplenum") couplenum: Int): Call<Child>

    @FormUrlEncoded
    @POST("child/{couplenum}")
    fun postChild(

        @Path("couplenum") couplenum: Int,
        @Field("kname") kname: String,
        @Field("name") name: String,
        @Field("sex") sex: String

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