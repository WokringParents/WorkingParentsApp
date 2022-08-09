package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CoupleCode(

    @SerializedName("code")
    val code: String,

    @SerializedName("ctime")
    val ctime: Timestamp,

    @SerializedName("cid")
    val cid: String
    )
