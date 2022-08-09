package com.example.workingparents

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private val TAG="RFS"
    lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            //Log.d("FBS", "토큰 받아오는 타이밍:$token")

        })


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

    private fun updateToken(id: String, token: String) {

        RetrofitBuilder.api.putUserToken(id,token).enqueue(object:Callback<Int>{

            override fun onResponse(call: Call<Int>, response: Response<Int>) {

                if(response.isSuccessful){
                    var result: Int? = response.body()
                    if(result==1){
                        Log.d(TAG, "token 업데이트 완료");
                    }
                }else{
                    Log.d(TAG, "token 업데이트실패");
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "updateToken onFailure 에러: " + t.message.toString());
            }
        })
    }

    private fun loginApplication(id: String, pw: String) {

        RetrofitBuilder.api.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){

                    var result: User? = response.body()
                    Log.d(TAG, "onResponse 성공: " + result?.toString());

                    if(!result?.pw.equals(pw)){
                        Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    else {

                        //만약 DB속 토큰과 지금 앱의 토큰이 다르다면? update해준다.
                        if( this@LoginActivity::token.isInitialized && !result?.token.equals(token)){
                            Log.d(TAG, "token 업데이트할거임");
                            updateToken(id,token)

                        }else{
                            Log.d(TAG, "token 같아서 업데이트 안해도됨")
                        }
                        UserData.setUserInfo(result!!.id,result!!.sex)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("LoginUser",result)
                        startActivity(intent)
                    }

                }else{
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


