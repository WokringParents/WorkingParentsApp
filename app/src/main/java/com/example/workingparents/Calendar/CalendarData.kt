package com.example.workingparents.Calendar

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CalendarData (

    @SerializedName("couplenum")
    var couplenum: Int,
    @SerializedName("cdate")
    val cdate: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("ccontent")
    val ccontent: String,
    @SerializedName("csex")
    val csex: String

)

