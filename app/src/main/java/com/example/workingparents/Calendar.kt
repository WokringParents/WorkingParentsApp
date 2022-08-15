package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Calendar (

    @SerializedName("couplenum")
    var couplenum: Int,
    @SerializedName("cdate")
    val cdate: Timestamp,
    @SerializedName("ctitle")
    val ctitle: String,
    @SerializedName("ccontent")
    val ccontent: String,
    @SerializedName("csex")
    val csex: String


)

