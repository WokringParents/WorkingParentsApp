package com.example.workingparents

class Dataitem() {
    private var comment: Comment? = null
    private var ccomment: Ccomment? = null
    private var viewType : Int? = 0

    fun Commentitem(comment: Comment?, viewType :Int) {
        this.comment = comment
        this.viewType =viewType
        ccomment = null
    }

    fun Ccommentitem(ccomment: Ccomment?, viewType :Int) {
        this.ccomment = ccomment
        this.viewType =viewType
        comment = null
    }

    fun getComment(): Comment? {
        return comment
    }

    fun getCcomment(): Ccomment? {
        return ccomment
    }

    fun getViewType(): Int {
        return viewType!!
    }
}