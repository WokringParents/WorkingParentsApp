package com.example.workingparents


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit.*
import retrofit2.converter.gson.GsonConverterFactory

//MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // val retrofit = Builder().baseUrl("http://workingparents-env-1.eba-ysfya3ek.ap-northeast-2.elasticbeanstalk.com/")
        //    .addConverterFactory(GsonConverterFactory.create()).build();
        //val service = retrofit.create(RetrofitService::class.java);

        /*
        RetrofitBuilder.api.getUser("testid").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성고된 경우
                    var result: User? = response.body()
                    Log.d("TAG", "onResponse 성공: " + result?.toString());
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("TAG", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("TAG", "onFailure 에러: " + t.message.toString());
            }
        })*/
    }
}