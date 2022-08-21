package com.example.workingparents

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm_loading.*

class AlarmLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_loading)


        circularProgressBar.apply{

            progressMax=100f
            setProgressWithAnimation(100f,2000)
            progressBarWidth=8f
            backgroundProgressBarWidth=10f
            progressBarColor= Color.parseColor("#FF9769")

        }


    }
}