package com.example.workingparents

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Comment (

    @SerializedName("pno")
    var pno: Int,

    @SerializedName("cno")
    var cno: Int,

    @SerializedName("cid")
    var cid: String,

    @SerializedName("cment")
    var cment: String,

    @SerializedName("cdate")
    var cdate: Timestamp,

    @SerializedName("cccnt")
    var cccnt: Int

)

