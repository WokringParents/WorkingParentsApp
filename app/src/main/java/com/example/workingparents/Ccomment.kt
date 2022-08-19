package com.example.workingparents

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import android.os.Parcelable as Parcelable1

@Parcelize

data class Ccomment(
    @SerializedName("pno")
    var pno: Int,

    @SerializedName("cno")
    var cno: Int,

    @SerializedName("ccno")
    var ccno: Int,

    @SerializedName("ccid")
    var ccid: String,

    @SerializedName("ccment")
    var ccment: String,

    @SerializedName("ccdate")
    var ccdate: Timestamp



    ) : Parcelable1
