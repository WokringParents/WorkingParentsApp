package com.example.workingparents

import kotlin.properties.Delegates

object UserData {

    lateinit var id: String
    lateinit var sex: String
    var couplenum by Delegates.notNull<Int>()
    lateinit var spouseID: String

    fun setUserInfo(id: String, sex:String){
        this.id=id
        this.sex=sex
    }

    fun setCoupleInfo(num:Int, id:String){
        this.couplenum=num
        this.spouseID=id
    }

    //부부연결된 사용자인지 아닌지 알려준다. 연결되어있으면 true
    fun connectedCouple():Boolean{
        return !(spouseID=="NONE" && couplenum==-1)
    }
}