package com.example.workingparents

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_alarm_loading.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmLoadingActivity : AppCompatActivity() {

    val TAG="Alarm"
    lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_loading)

        val intent: Intent = getIntent()
        content= intent.getStringExtra("content").toString()

        inputContent2.setText(content)


        // val LoginUser = intent.getParcelableExtra<User>("LoginUser")
        circularProgressBar.apply{

            progressMax=100f
            setProgressWithAnimation(100f,2000)
            progressBarWidth=8f
            backgroundProgressBarWidth=10f
            progressBarColor= Color.parseColor("#FF9769")

        }


            RetrofitBuilder.api.getUser("TestUser3").enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    var result: User? = response.body()

                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse 성공: $result");
                        if (result != null) {
                            requestPushAlram(result.token)
                        }

                    } else {
                        Log.d(TAG, "onResponse 실패: ");
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure 에러 " + t.message.toString());
                }
            })
    }


    fun requestPushAlram(token: String) {

        val obj = FCMRetrofitBuilder.takeJsonObject(token, "방금전 같은 동네에서 새로운 글이 작성되었어요",
            "만촌동 주민에게 도움이 필요해요: $content"
        )

        FCMRetrofitBuilder.api.pushAlram(obj.toString()).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse 성공: " + response?.body().toString());


                } else {
                    Log.d(TAG, "onResponse 실패: ");
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure 에러: " + t.message.toString());
            }
        })
    }


}