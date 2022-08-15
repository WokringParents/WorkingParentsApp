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
    var mdo : Boolean,

    @SerializedName("fdo")
    var fdo : Boolean,

    @SerializedName("dayOfWeek")
    val dayOfWeek: Int
)
