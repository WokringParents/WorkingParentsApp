package com.example.workingparents

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.RegisterChildActivity.Companion.kingdergarden
import com.example.workingparents.UserData.connectedChild
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_join2.*
import kotlinx.android.synthetic.main.activity_register_child.*
import kotlinx.android.synthetic.main.activity_register_child.daySpinner
import kotlinx.android.synthetic.main.activity_register_child.monthSpinner
import kotlinx.android.synthetic.main.activity_register_child.yearSpinner
import kotlinx.android.synthetic.main.activity_search_kingergarden.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private val TAG="Child"

class RegisterChildActivity : AppCompatActivity() {

    //전역변수
    companion object{
        lateinit var kingdergarden:TextView
        lateinit var sex:String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_child)


        //뒤로가기
        registerchild_back.setOnClickListener{
            onBackPressed()
        }

        //유치원 찾기 버튼 클릭 시
        btn_search.setOnClickListener {
            val intent = Intent(this@RegisterChildActivity, SearchKingergardenActivity::class.java)
            startActivity(intent)
        }

        edit_kg.setOnClickListener {
            val intent = Intent(this@RegisterChildActivity, SearchKingergardenActivity::class.java)
            startActivity(intent)
        }


        var yearList = (1950..2022).toList()
        var monthList = (1..12).toList()
        var dateList = (1..31).toList()

        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }

        kingdergarden=findViewById(R.id.edit_kg)


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


        radio_group_child.setOnCheckedChangeListener{
                group, checkedId ->
            when(checkedId){
                R.id.girlBtn -> sex =  "F"
                R.id.boyBtn -> sex =  "M"
            }
        }

        btn_childFinish.setOnClickListener {

            var kname = edit_kg.text.toString()
            var cname= edit_childname.text.toString()

            if (kname == "" || cname == "") {
                Toast.makeText(this@RegisterChildActivity, "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (sex ==null) {
                Toast.makeText(this@RegisterChildActivity, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                postChild(UserData.couplenum, kname, cname, sex)
            }

        }

    }

    fun postChild(couplenum:Int, kname:String, name:String, sex:String)
    {
        RetrofitBuilder.api.postChild(couplenum, kname, name, sex).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    var result: Int? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d(TAG, "onResponse: 아이등록성공" + result?.toString())
                    Toast.makeText(this@RegisterChildActivity, "아이등록 완료", Toast.LENGTH_SHORT).show()
                    UserData.setChildInfo(kname) //이걸 넣어둬야 아이등록했을 때 이름이 뜸
                    //물론 다자녀따윈 고려하지 않는다. 이미 아이가 있는데 또 등록을 하면 그 아이 이름을 뜰 수밖에...

                    finish()
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Toast.makeText(this@RegisterChildActivity, "아이등록 실패", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 회원가입 실패 에러: " + t.message.toString())
                Toast.makeText(this@RegisterChildActivity, "아이등록 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()

            }
        })
    }
}

fun selectkg(name : String)
{
    kingdergarden.setText(name)
    kingdergarden.setTextColor(Color.parseColor("#000000"))
}