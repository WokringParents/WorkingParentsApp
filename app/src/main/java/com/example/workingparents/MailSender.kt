package com.example.workingparents

import android.util.Log
import java.lang.Exception
import java.util.*
import javax.mail.*
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailSender : Authenticator() {
    private val auth: Authenticator
    private val msg: MimeMessage
    private val session: Session
    @Synchronized
    @Throws(Exception::class)
    fun sendMail(title: String?, content: String?, recipients: String?) {
        try {
            msg.sentDate = Date()
            var from = InternetAddress()
            from = InternetAddress("ColorfulCardApp<podojom@naver.com>")
            msg.setFrom(from)
            val to = InternetAddress(recipients)
            msg.setRecipient(Message.RecipientType.TO, to)
            msg.setSubject(title, "UTF-8")
            msg.setText(content, "UTF-8")
            msg.setHeader("content-Type", "text/html")
            Transport.send(msg)
            Log.d("Mail","전송함")
        } catch (addr_e: AddressException) {
            addr_e.printStackTrace()
        } catch (msg_e: MessagingException) {
            msg_e.printStackTrace()
        }
    }

    internal inner class MyAuthentication : Authenticator() {
        var account: PasswordAuthentication
        public override fun getPasswordAuthentication(): PasswordAuthentication {
            return account
        }

        init {
            val id = "podojom"
            val pw = "yeon21912091"
            account = PasswordAuthentication(id, pw)
        }
    }

    init {
        val props = Properties()
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.naver.com"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "587"
        auth = MyAuthentication()
        session = Session.getDefaultInstance(props, auth)
        msg = MimeMessage(session)
    }
}