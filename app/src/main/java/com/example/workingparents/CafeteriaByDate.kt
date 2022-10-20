package com.example.workingparents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CafeteriaByDate(

     var cdate :String,
     var images: MutableMap<Int,String>,
     var contents: MutableMap<Int,String> ,
     var imageBytes: MutableMap<Int,ByteArray>

): Parcelable {
     constructor(cdate: String) : this(cdate, mutableMapOf(), mutableMapOf(), mutableMapOf())
}