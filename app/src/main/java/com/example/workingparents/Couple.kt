package com.example.workingparents
import com.google.gson.annotations.SerializedName

data class Couple(

    @SerializedName("couplenum")
    val couplenum: Int,

    @SerializedName("mid")
    val mid: String,

    @SerializedName("did")
    val did: String

)
