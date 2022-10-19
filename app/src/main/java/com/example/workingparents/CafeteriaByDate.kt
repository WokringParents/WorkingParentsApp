package com.example.workingparents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CafeteriaByDate(var _cdate:String): Parcelable {

     var cdate = _cdate
     var images: MutableMap<Int,String> = mutableMapOf()
     var contents: MutableMap<Int,String> = mutableMapOf()
     var imageBytes: MutableMap<Int,ByteArray> = mutableMapOf()


}