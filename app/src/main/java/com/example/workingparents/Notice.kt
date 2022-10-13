package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Notice(

    @SerializedName("nid")
    var nid: Int,

    @SerializedName("tid")
    var tid: Int,

    @SerializedName("ndate")
    var ndate: Timestamp,

    @SerializedName("ntitle")
    var ntitle: String,

    @SerializedName("ncontent")
    var ncontent: String,

    @SerializedName("image")
    var image: String
)
