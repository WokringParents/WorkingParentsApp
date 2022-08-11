package com.example.workingparents

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_write_posting.*

var checkPostingContent = false


class WritePostingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_posting)

        inputContent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                //사용자가 글을 썼다면
                if (s.toString().length > 0) {
                    NotifyWordCnt.text = s.toString().length.toString() + "/500"
                    checkPostingContent = true
                } else { //아니라면
                    NotifyWordCnt.text = "0/500"
                    checkPostingContent = false
                }
            }
        })
    }
}