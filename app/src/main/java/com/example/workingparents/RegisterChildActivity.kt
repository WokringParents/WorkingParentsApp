package com.example.workingparents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_register_child.*
import kotlinx.android.synthetic.main.activity_search_kingergarden.*

class RegisterChildActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_child)

        registerchild_back.setOnClickListener{
            onBackPressed()
        }

        btn_search.setOnClickListener {
            val intent = Intent(this@RegisterChildActivity, SearchKingergardenActivity::class.java)
            startActivity(intent)
        }

        var yearList = (1950..2022).toList()
        var monthList = (1..12).toList()
        var dateList = (1..31).toList()

        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }


        //wrapSelectorWheel : default 값은 true로 false시 picker의 범위가 시작 ~ 끝으로 고정됩니다. 아래 예제 년도는 false 값이고 월은 true 값입니다.
        //descendantFocusability : picker는 기본적으로 데이터를 클릭하면 edittext로 전환되어 특정 값을 입력할 수 있는데 해당 값을 'NumberPicker.FOCUS_BLOACK_DESCENDANTS' 로 변경하면 edittext로 전환을 막을 수 있습니다.
        //colorControlNormal : style을 통해 divider 색을 변경할 수 있는 옵션입니다.  그외 글씨 크기, 하이라이트된 글자 색상등을 아래와 같이 수정할 수 있습니다.
        yearSpinner.run {
            minValue = 0
            maxValue = yearStrConvertList.size - 1
            wrapSelectorWheel = true
            displayedValues = yearStrConvertList.toTypedArray()
        }

        monthSpinner.run {
            minValue = 0
            maxValue = monthStrConvertList.size - 1
            wrapSelectorWheel = true
            displayedValues = monthStrConvertList.toTypedArray()

        }

        daySpinner.run {
            minValue = 0
            maxValue = dateStrConvertList.size - 1
            wrapSelectorWheel = true
            displayedValues = dateStrConvertList.toTypedArray()
        }
    }
}