package com.example.workingparents

import kotlin.properties.Delegates

object UserData {

    lateinit var id: String
    lateinit var sex: String
    lateinit var name: String
    lateinit var token: String
    lateinit var village: String
    lateinit var pNumber: String

    var couplenum by Delegates.notNull<Int>()
    lateinit var spouseID: String
    lateinit var spouseName: String

    fun setUserInfo(id: String, sex:String, name: String, pNumber: String, token: String, village: String){
        this.id=id
        this.sex=sex
        this.name=name
        this.pNumber=pNumber
        this.token=token
        this.village=village
    }

    fun setCoupleInfo(num:Int, id:String, spouseName: String){
        this.couplenum=num
        this.spouseID=id
        this.spouseName=spouseName
    }

    //부부연결된 사용자인지 아닌지 알려준다. 연결되어있으면 true
    fun connectedCouple():Boolean{
        return !(spouseID=="NONE" && couplenum==-1)
    }

}