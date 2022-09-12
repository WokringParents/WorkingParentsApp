package com.example.workingparents

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.acriviry_testcoroutines.*
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.coroutines.CoroutineContext

class CoroutinesTest : AppCompatActivity() {

    var hashMap= HashMap<CalendarDay,String>()
    val contextMain= this@CoroutinesTest

//Dispatcher는 코루틴이 어떤 쓰레드에서 동작할 지 지정하는 역할

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acriviry_testcoroutines)

        val CoupleColor  =  intArrayOf(
            //  Color.rgb(255, 184, 203),
            //  Color.rgb(155, 205, 255)
            Color.rgb(255, 168, 177),
            Color.rgb(158, 193, 255)
        )

        val FamaleColor  =  intArrayOf(
            // Color.rgb(255, 184, 203)
            Color.rgb(255, 168, 177)
        )

        val MaleColor  =  intArrayOf(
            //Color.rgb(155, 205, 255)
            Color.rgb(158, 193, 255)
        )


        val list = ArrayList<CalendarRecyclerData>()
//Get할 때 CalendarData형식으로 넣어주는 리스트



        //launch {
            Log.d("order","1")
            RetrofitBuilder.api.getCalendar(CalendarActivity.couplenum).enqueue(object: Callback<List<CalendarData>> {

                override fun onResponse(call: Call<List<CalendarData>>, response: Response<List<CalendarData>>) {
                    Log.d("order","2")
                    if(response.isSuccessful){
                    Log.d("order","3")
                        var result: List<CalendarData>? = response.body()
                        // 정상적으로 통신이 성공된 경우
                        Log.d("order","4")
                        Log.d("Retrofit", "onResponse: 캘린더성공"+result?.toString())

                        textView.setText("Ji")
                        Log.d("order","5")

                    }else{
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        Log.d("Retrofit", "onResponse: 캘린더실패")
                    }
                }

                override fun onFailure(call: Call<List<com.example.workingparents.CalendarData>>, t: Throwable) {
                    Log.d("Retrofit", "onFailure 캘린더 실패 에러1: " + t.message.toString())

                }


            })



        }
    //}

}