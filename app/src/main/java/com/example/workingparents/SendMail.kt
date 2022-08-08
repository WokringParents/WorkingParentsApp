package com.example.workingparents

import android.os.Message
import android.util.Log
import javax.mail.MessagingException
import javax.mail.SendFailedException

class SendMail() :
    Thread() {
    var receptEmail = "erropp89@naver.com"
    var content: String
    override fun run() {
        try {
            val gMailSender = MailSender()
            Log.d("tag", "sender Make")
            gMailSender.sendMail(
                "[컬러풀 카드앱] 비밀번호 안내",
                content, receptEmail
            )
        } catch (e: SendFailedException) {
        } catch (e: MessagingException) {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        content =
            name + "님, 안녕하세요.<br>회원님의 가입된 컬러풀 카드앱의 비밀번호는 아래와 같습니다.<br><br>비밀번호: "  + "<br>"
    }
}