package com.example.workingparents

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("image")
    var image: String,

    @SerializedName("nid")
    var nid: Int
)
