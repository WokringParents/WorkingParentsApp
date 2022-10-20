package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_input_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputCodeActivity : AppCompatActivity() {

    val TAG="Connect"
    lateinit var inputCode: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_code)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        connectBtn.setOnClickListener(View.OnClickListener {

            if(!spouseCodeText.text.isNullOrEmpty()){

                inputCode= spouseCodeText.text.toString()

                RetrofitBuilder.api.getSpouseID(inputCode).enqueue(object: Callback<String> {
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

        RetrofitBuilder.api.postCouple(mid,did,loginSex).enqueue(object:Callback<Couple>{
            override fun onResponse(call: Call<Couple>, response: Response<Couple>) {
                if(response.isSuccessful){

                    var result: Couple? = response.body()
                    Log.d(TAG, "onResponse: 부부등록 성공")
                    Log.d(TAG,"배우자이름:"+result!!.spouseName+"아빠ID"+result.did+"엄마ID:"+result.mid)
                    Toast.makeText(this@InputCodeActivity, "배우자와 연결되었어요!", Toast.LENGTH_SHORT).show()
                    finish()
                    //다시 마이페이지로 돌아가거나 그냥 화면끊거나,,,,
                    //+은아 여기 그냥 뒤로가기 누르면 네브바가 이상하게 돌아가서 finish() 넣어줬어요

                    UserData.setCoupleInfo(result.couplenum,spouseID,result.spouseName)

                    if(!inputCode.isNullOrEmpty()) {
                        delCoupleCode(inputCode)
                    }
                }else{
                    Toast.makeText(this@InputCodeActivity, "이미 배우자와 연결되었어요!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onResponse: 부부등록 실패")
                }
            }

            override fun onFailure(call: Call<Couple>, t: Throwable) {

                Log.d(TAG, "onFailure 부부등록 실패에러: " + t.message.toString())
                Toast.makeText(this@InputCodeActivity, "부부연결 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()
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