package com.example.workingparents

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("pw")
    val pw: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("sex")
    val sex: String,

    @SerializedName("token")
    val token: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("pnumber")
    val pnumber: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("village")
    val village: String,

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑

) : Parcelable
