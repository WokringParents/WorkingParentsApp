package com.example.workingparents

class NoticeData {
    private var ntype: String? = null
    private var icnt : Int? = 0
    private var viewType : Int? = 0

    fun Noticeitem(ntype: String?, viewType :Int) {
        this.ntype = ntype
        this.viewType =viewType
    }

    fun getnType(): String? {
        return ntype
    }

    fun geticnt(): Int? {
        return icnt
    }

    fun getViewType(): Int? {
        return viewType
    }
}