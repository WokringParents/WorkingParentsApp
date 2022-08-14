package com.example.workingparents

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_posting.*




class PostingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        pid.setText(intent.getStringExtra("rv_pid"))
        village.setText(intent.getStringExtra("rv_village"))
        goback.setText(intent.getStringExtra("rv_goback"))
        ptime.setText(intent.getStringExtra("rv_pdate"))
        pcontent.setText(intent.getStringExtra("rv_pcontent"))
        hcnt.setText(intent.getStringExtra("rv_hcnt"))
        ccnt.setText(intent.getStringExtra("rv_ccnt"))


    }


}