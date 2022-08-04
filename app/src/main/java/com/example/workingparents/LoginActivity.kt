package com.example.workingparents

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private val TAG="RFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener{

            var id = edit_id.text.toString()
            var pw = edit_pw.text.toString()


            if (id == "") {
                Toast.makeText(this@LoginActivity, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (pw == "") {
                Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                loginApplication(id, pw)
            }
        }

        btn_join.setOnClickListener{
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }

        }



    private fun loginApplication(id: String, pw: String) {

        RetrofitBuilder.api.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){

                    // 정상적으로 통신이 성공된 경우
                    var result: User? = response.body()
                    Log.d(TAG, "onResponse 성공: " + result?.toString());

                    if(!result?.pw.equals(pw)){
                        Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d(TAG, "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d(TAG, "onFailure 에러: " + t.message.toString());
            }
        })

    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setMessage("애플리케이션을 종료하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    finishAffinity()
                    System.runFinalization()
                    System.exit(0)
                })
            .setNegativeButton("취소",null)
        // 다이얼로그를 띄워주기
        builder.show()
    }

}


