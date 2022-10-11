package com.example.workingparents

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_alarm_loading.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AlarmLoadingActivity : AppCompatActivity() {

    val TAG="PushAlarm"
    lateinit var content: String
    var pushSum :Int=0
    var pushCnt :Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_loading)

        val intent: Intent = getIntent()
        content= intent.getStringExtra("content").toString()

        inputContent2.setText(content)

         circularProgressBar.apply{

            progressMax=100f
            setProgressWithAnimation(100f,1500)
            progressBarWidth=8f
            backgroundProgressBarWidth=10f
            progressBarColor= Color.parseColor("#FF9769")
        }


        RetrofitBuilder.api.getTokenListByVillage(UserData.village).enqueue(object: Callback<List<String>>{
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if(response.isSuccessful){

                    var result: List<String>? = response.body()
                    if(result!=null){
                        Log.d(TAG, "onResponse 성공: getTokenListByVillage")

                        for(token: String in result){

                           if(token!=UserData.token) {
                               pushSum++
                               requestPushAlram(token)
                               Log.d(TAG, token + "에게 알람보냄 " + pushSum)
                           }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {

            }


        })


        //상대방 핸드폰에 푸시알람보내는 것임!!!!
         /*   RetrofitBuilder.api.getUser("TestUser3").enqueue(object : Callback<User> {

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
            })*/
    }


    /*

        val obj = JsonObject()
        val notification = JsonObject()
        notification.addProperty("title", "04:00 fore그라운드 오류나지마")
        notification.addProperty("body", "제발 코딩꾸버신이여 저를 도와주소서")
        obj.addProperty("to","c7UAgs7nSYKeqr_6zFeDpq:APA91bGJmhvQzbtW396sZu2l9vWxKxROIe8A5BXpUArDGF7ps5TQqyqs6H5xt5opSX0o6WqLdNlOjO2QVi3IBSGZ9AhBG9dsVxAcZ9EY5sRI80LJX7h55-ONY9ISmBg_6wpqaAtlhMh-")
        obj.add("notification", notification)
*/


    fun requestPushAlram(token: String) {

        pushCnt++;

        val obj = FCMRetrofitBuilder.takeJsonObject(token, "방금전 같은 동네에서 새로운 글이 작성되었어요",
            UserData.village+" 주민에게 도움이 필요해요: $content"
        )

        FCMRetrofitBuilder.api.pushAlram(obj.toString()).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse 성공:  pushAlram "+ pushCnt +  circularProgressBar.progress);

                } else {
                    Log.d(TAG, "onResponse 실패: pushAlram");
                }

                if(pushCnt==pushSum){
                    val thread = Thread {
                        val task: TimerTask = object : TimerTask() {
                            override fun run() {
                                finish()
                            }
                        }
                        val timer = Timer()
                        timer.schedule(task, 1400)
                    }

                    thread.start()

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure 에러: " + t.message.toString());
            }
        })
    }
}