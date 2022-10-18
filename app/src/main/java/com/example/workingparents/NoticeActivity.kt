package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_notice.*

class NoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        noticedate.setText(intent.getStringExtra("rv_date"))
        noticetitle.setText(intent.getStringExtra("rv_title"))
    }
}