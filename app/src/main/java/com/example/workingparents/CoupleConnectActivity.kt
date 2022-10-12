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
    private val TAG="RFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_couple_connect)

   //     val intent: Intent = getIntent()
   //     val loginUser= intent.getParcelableExtra<User>("LoginUser")


        createCodeBtn.setOnClickListener(View.OnClickListener {

            newCode = randomCode()
            spouseCodeText.setText(newCode)
            createCodeBtn.setClickable(false)
            if (!newCode.isNullOrEmpty() && !UserData.id.isNullOrEmpty()) {
                regiCoupleCode(newCode,UserData.id)
            }

        })


        inputCodeBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@CoupleConnectActivity, InputCodeActivity::class.java)
            startActivity(intent)
            finish()
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





}