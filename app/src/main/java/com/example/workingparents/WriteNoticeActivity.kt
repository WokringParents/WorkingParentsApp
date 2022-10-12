package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_write_notice.*
import kotlinx.android.synthetic.main.activity_write_posting.*

class WriteNoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_notice)

        //뒤로가기
        writeNotice_back.setOnClickListener{
            onBackPressed()
        }

    }
}