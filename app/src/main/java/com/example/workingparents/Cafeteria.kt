package com.example.workingparents

import com.google.gson.annotations.SerializedName

data class Cafeteria(
    @SerializedName("tid")
    val tid: Int,

    @SerializedName("cdate")
    val cdate:String,

    @SerializedName("ctype")
    val ctype:Int,

    @SerializedName("content")
    val content:String,

    @SerializedName("image")
    val image:String

)
