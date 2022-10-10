package com.example.workingparents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_join.btn_joinFinish
import kotlinx.android.synthetic.main.activity_join2.*
import kotlinx.android.synthetic.main.activity_join2.daySpinner
import kotlinx.android.synthetic.main.activity_join2.monthSpinner
import kotlinx.android.synthetic.main.activity_join2.yearSpinner
import kotlinx.android.synthetic.main.posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity2 : AppCompatActivity() {
    private val TAG="JOIN2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join2)


        val Join2Intent = intent
        val id = Join2Intent.getStringExtra("id")
        val pw = Join2Intent.getStringExtra("pw")
        val email = Join2Intent.getStringExtra("email")
        val token = Join2Intent.getStringExtra("token")

        var sex:String?= null

        radio_group_join.setOnCheckedChangeListener{
                group, checkedId ->
            when(checkedId){
                R.id.momBtn -> sex =  "F"
                R.id.dadBtn -> sex =  "M"
            }
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

        btn_joinFinish.setOnClickListener(View.OnClickListener {

            var name = edit_joinName.text.toString()
            var pnumber= edit_pnumber.text.toString()
            var city= join_city.text.toString()
            var village= join_village.text.toString()

            if (name == "" || city == "" || village == "" || pnumber == "") {
                Toast.makeText(this@JoinActivity2, "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (sex ==null) {
                Toast.makeText(this@JoinActivity2, "성별을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                    joinApplication(id!!, pw!!, email!!, sex!!, token!!, name, pnumber, city, village)//모든 조건 완료시 회원 가입 시키는 함수
            }

        })

    }

    private fun joinApplication(
        id: String,
        pw: String,
        email: String,
        sex: String,
        token: String,
        name: String,
        pnumber: String,
        city: String,
        village: String
    ) {

        RetrofitBuilder.api.postUser(id, pw, email, sex, token, name, pnumber, city,village
        ).enqueue(object : Callback<Int> {

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {

                    var result: Int? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d(TAG, "onResponse: 회원등록성공" + result?.toString())
                    Toast.makeText(this@JoinActivity2, "회원가입 완료", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@JoinActivity2, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Toast.makeText(this@JoinActivity2, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 회원가입 실패 에러: " + t.message.toString())
                Toast.makeText(this@JoinActivity2, "회원가입 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()

            }

        })


    }
}