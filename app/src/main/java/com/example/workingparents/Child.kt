package com.example.workingparents

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Child(
    @SerializedName("couplenum")
    var couplenum: Int,

    @SerializedName("childId")
    var childId: Int,

    @SerializedName("kname")
    var kname: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("sex")
    var sex: String,


) : Parcelable
