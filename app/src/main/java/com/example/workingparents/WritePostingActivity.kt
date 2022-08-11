package com.example.workingparents

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_write_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var checkPostingContent = false
var checkGoback = false
private val TAG="PostBoard"
lateinit private var goback: String


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

        completeBt.setOnClickListener(View.OnClickListener {

            Log.d(TAG,"작성완료 버튼 눌림")

            if (checkPostingContent) {
                insertPostingToBoard()
                Log.d(TAG,"포스팅완료")

            } else {
                Toast.makeText(applicationContext, "내용을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun insertPostingToBoard() {

        var pid=UserData.id
        var content=inputContent.text.toString()
        var village=UserData.village

        when(radio_group_goback.checkedRadioButtonId){
            R.id.goBtn->{
                goback = "go"
                checkGoback=true
            }
            R.id.backBtn-> {
                goback = "back"
                checkGoback=true
            }
        }

        Log.d(TAG,pid)
        Log.d(TAG,content)
        Log.d(TAG,village)

        if(checkGoback)
        {
            RetrofitBuilder.api.postPosting(pid,village,goback,content).enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        var result: Int? = response.body()
                        finish() //main으로 돌아감
                        Log.d(TAG, "onResponse: 포스팅 성공")
                    } else {
                        Log.d(TAG, "onResponse: 포스팅 실패")
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure 포스팅 실패 에러: " + t.message.toString())

                }
            })
        }
        else
        {
            Toast.makeText(applicationContext, "등하원을 선택해주세요", Toast.LENGTH_SHORT).show()
        }

    }
}