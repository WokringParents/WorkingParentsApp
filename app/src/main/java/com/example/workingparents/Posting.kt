package com.example.workingparents

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Posting(
    @SerializedName("pno")
    var pno: Int,

    @SerializedName("pid")
    var pid: String,

    @SerializedName("village")
    var village: String,

    @SerializedName("goback")
    var goback: String,

    @SerializedName("pdate")
    var pdate: Timestamp,

    @SerializedName("content")
    var content: String,

    @SerializedName("hcnt")
    var hcnt: Int,

    @SerializedName("ccnt")
    var ccnt: Int


) : Parcelable

