package com.example.workingparents

class StateSet {
    object MailMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS = 1
    }

    object RegisterCardMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS_VAILCHECK = 1
    }

    object HomeMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS_GETFAVORSTORE = 1
        const val MSG_SUCCESS_GETSTORE = 2
    }

    object LoadingMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS_BALCHECK = 1
    }

    object SearchMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS_SEARCH = 1
        const val MSG_SEARCH_NO_WORD = 2
    }

    object BoardMsg {
        const val MSG_FAIL = 0
        const val MSG_SUCCESS_GET_FIRST = 1
        const val MSG_SUCCESS_GETPOSTINGS = 2
        const val MSG_NO_POSTINGS = 3
        const val MSG_ALREADY_GET_ALLPOSTINGS = 4
        const val MSG_NEED_TO_CCMENTS = 10
        const val MSG_SUCCESS_GET_ALLCMENTS = 11
        const val MSG_SUCCESS_GET_CMENTS = 5
        const val MSG_SUCCESS_GET_CCMENTS = 6
        const val MSG_SUCCESS_DEL_POSTING = 7
        const val MSG_SUCCESS_HEARTPRESS = 8
        const val MSG_SUCCESS_INSERT_CMENT = 9
    }

    object ViewType {

        const val posting = 0
        const val comment = 1
        const val ccomment = 2
        const val loading = 3
        const val searchResult = 4

    }
}