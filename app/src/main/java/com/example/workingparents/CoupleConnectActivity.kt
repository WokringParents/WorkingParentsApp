package com.example.workingparents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_couple_connect.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class CoupleConnectActivity : AppCompatActivity() {

    lateinit var newCode: String
    lateinit var inputCode: String
    private val TAG="RFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_couple_connect)

   //     val intent: Intent = getIntent()
   //     val loginUser= intent.getParcelableExtra<User>("LoginUser")


        createCodeBtn.setOnClickListener(View.OnClickListener {

            newCode = randomCode()
            codeText.setText(newCode)
            createCodeBtn.setClickable(false)
            if (!newCode.isNullOrEmpty() && !UserData.id.isNullOrEmpty()) {
                regiCoupleCode(newCode,UserData.id)
            }

        })

        inputCodeBtn.setOnClickListener(View.OnClickListener {

            codeEditText.visibility=View.VISIBLE
            connectBtn.visibility=View.VISIBLE
        })

        connectBtn.setOnClickListener(View.OnClickListener {

            if(!codeEditText.text.isNullOrEmpty()){

                inputCode= codeEditText.text.toString()

                RetrofitBuilder.api.getSpouseID(inputCode).enqueue(object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        if(response.isSuccessful){
                            // 정상적으로 통신이 성공된 경우
                            var spouseID: String? = response.body()
                            Log.d(TAG, "onResponse: 부부연결을 위한 배우자아이디 불러오기 성공: "+spouseID.toString())
                            connectCouple(spouseID!!, UserData!!.id, UserData.sex)


                        }else{
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                            Log.d(TAG, "onResponse: 부부연결을 위한 배우자아이디 불러오기 실패")
                        }

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d(TAG, "onFailure 배우자아이디 실패에러: " + t.message.toString())
                    }
                })
            }
        })
    }

    private fun randomCode( ): String {

        val buf = StringBuffer()

        for (i in 0..7) {
            if (Random.nextBoolean()) {
                buf.append((Random.nextInt(26) + 97).toChar())
            } else {
                buf.append(Random.nextInt(10))
            }
        }
        return buf.toString()
    }


    private fun regiCoupleCode(code: String, id:String){

        RetrofitBuilder.api.postCode(code,id).enqueue(object: Callback<Int> {

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: Int? = response.body()
                    Log.d(TAG, "onResponse: 커플코드등록 성공"+result?.toString())
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d(TAG, "onResponse: 커플코드등록 실패")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 커플코드 실패에러: " + t.message.toString())
                Toast.makeText(this@CoupleConnectActivity, "코드등록 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()

            }

        })
    }


    private fun connectCouple(spouseID: String, loginID:String, loginSex: String){

        lateinit var mid: String
        lateinit var did: String

        if(loginSex=="M"){ //로그인한 사용자가 남자라면 아빠아이디에
            mid=spouseID
            did=loginID
        }else{
            //로그인한 사용자가 여자라면 엄마아이디에
            mid=loginID
            did=spouseID
        }

        RetrofitBuilder.api.postCouple(mid,did).enqueue(object:Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: 부부등록 성공")
                    Toast.makeText(this@CoupleConnectActivity, "배우자와 연결되었어요!", Toast.LENGTH_SHORT).show()

                    if(!inputCode.isNullOrEmpty()) {
                        delCoupleCode(inputCode)
                    }
                }else{
                    Toast.makeText(this@CoupleConnectActivity, "이미 배우자와 연결되었어요!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onResponse: 부부등록 실패")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {

                Log.d(TAG, "onFailure 부부등록 실패에러: " + t.message.toString())
                Toast.makeText(this@CoupleConnectActivity, "부부연결 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()
            }


        })

    }

    private fun delCoupleCode(code: String){

        Log.d(TAG, "code: "+ code)

        RetrofitBuilder.api.deleteCode(code).enqueue(object: Callback<Int>{

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.isSuccessful){
                    val result : Int?= response.body()
                    Log.d(TAG, "onResponse: 코드파괴 성공"+ result.toString())

                }else{
                    Log.d(TAG, "onResponse: 코드파괴 실패")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 코드파괴 실패에러: " + t.message.toString())
               }
        })
    }

}