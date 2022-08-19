package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class SharingList(

    @SerializedName("couplenum")
    val couplenum: Int,

    @SerializedName("sdate")
    var sdate: Timestamp,

    @SerializedName("content")
    var content : String,

    @SerializedName("mdo")
    var mdo : Boolean,

    @SerializedName("fdo")
    var fdo : Boolean,

    @SerializedName("dayOfWeek")
    var dayOfWeek: Int,

    @SerializedName("daily")
    val daily: Boolean,

    var inputMode: Boolean = false

)
