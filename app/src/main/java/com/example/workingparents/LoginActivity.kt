package com.example.workingparents

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private val TAG="LoginTAG"
    lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //시스템상 다크모드 끄는거

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
            } else if(id=="ssam"&&pw=="pw00")
            {
                val intent = Intent(this@LoginActivity, TeacherMainActivity::class.java)
                startActivity(intent)
            }
            else {
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

                    if(result!=null){
                        if(!result.pw.equals(pw))
                            Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show()
                        else{

                            //만약 DB속 토큰과 지금 앱의 토큰이 다르다면? update해준다.
                            if( this@LoginActivity::token.isInitialized && !result.token.equals(token)){
                                Log.d(TAG, "token 업데이트할거임");
                                updateToken(id,token)
                            }else{
                                Log.d(TAG, "token 같아서 업데이트 안해도됨")
                            }

                            UserData.setUserInfo(result.id,result.sex,result.name,result.pnumber,result.token, result.village)

                            checkCouple()

                        }
                    }
                }else
                    Log.d(TAG, "onResponse 실패: loginApplication");
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                if (t.message == "End of input at line 1 column 1 path $") {
                    Toast.makeText(
                        applicationContext,
                        "미가입된 계정입니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


    fun checkCouple() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)

        RetrofitBuilder.api.getCouplebyID(UserData.id).enqueue(object : Callback<Couple> {
            override fun onResponse(call: Call<Couple>, response: Response<Couple>) {
                if (response.isSuccessful) {
                    var result: Couple? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d(TAG, "onResponse: 부부정보 들고오기 성공" + result?.toString())

                    if(result!=null){
                        if(UserData.sex=="M") {
                            UserData.setCoupleInfo(
                                result.couplenum,
                                result.mid,
                                result.spouseName
                            ) //사용자가 남자일때, 배우자에는 엄마아이디
                            checkChild(result.couplenum, intent)
                        }
                         else {
                            UserData.setCoupleInfo(result.couplenum, result.did, result.spouseName)
                            checkChild(result.couplenum, intent)
                        }
                    }

                } else {
                    Log.d(TAG, "onResponse 부부정보 들고오기 에러: ")
                }
            }
            override fun onFailure(call: Call<Couple>, t: Throwable) {
                if(t.message == "End of input at line 1 column 1 path $") {
                    UserData.setCoupleInfo(-1,"NONE","NONE")
                    //부부가 아님
                    startActivity(intent)

                }
                Log.d(TAG, "onFailure 부부확인 실패 에러 테스트: " + t.message.toString())

            }
        })

    }

    fun checkChild(couplenum: Int, intent: Intent) {

        RetrofitBuilder.api.getChildbyCoupleNum(UserData.couplenum).enqueue(object : Callback<Child> {
            override fun onResponse(call: Call<Child>, response: Response<Child>) {
                if (response.isSuccessful) {
                    var result: Child? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d(TAG, "onResponse: 아이정보 들고오기 성공" + result?.toString())

                    if (result != null) {
                        UserData.setChildInfo(
                            result.name
                        )
                    }
                    startActivity(intent)

                } else {
                    Log.d(TAG, "onResponse 아이정보 들고오기 에러: ")
                }
            }

            override fun onFailure(call: Call<Child>, t: Throwable) {
                if (t.message == "End of input at line 1 column 1 path $") {
                    UserData.setChildInfo("NONE")
                    startActivity(intent)
                }
                Log.d(TAG, "onFailure 아이정보 실패 에러 테스트: " + t.message.toString())
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


