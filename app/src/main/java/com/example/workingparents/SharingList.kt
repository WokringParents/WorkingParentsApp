package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class SharingList(

    @SerializedName("couplenum")
    val couplenum: Int,

    @SerializedName("sdate")
    val sdate: Timestamp,

    @SerializedName("content")
    val content : String,

    @SerializedName("mdo")
    val mdo : Int,

    @SerializedName("fdo")
    val fdo : Int,

    @SerializedName("dayOfWeek")
    val dayOfWeek: Int
)
