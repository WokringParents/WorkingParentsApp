package com.example.workingparents

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_join.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    private val TAG="RFS"
    private var validatePW = false
    private var validateID= false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        //UI요소임 눈누르면 비밀번호 보이고 다시누르면 안보이고
        showHideBtn.setOnClickListener{
        showHideBtn?.isSelected=showHideBtn?.isSelected!=true
            if(showHideBtn.isSelected){
                edit_joinPW.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else{
                edit_joinPW.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("FBS", "토큰 받아오는 타이밍:$token")

        })


        edit_joinPW.addTextChangedListener(object : TextWatcher {
            //비밀번호에 리스너를 달아서 비밀번호 형식에 맞는지 검사 부분
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length <= 10) {
                    val p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9]).$")
                    val m = p.matcher(s.toString())
                    if (!m.matches()) {
                        checkIDPW.setTextColor(-0x5510b7b6)
                        checkIDPW.setText("영문자,숫자,특수문자 조합")
                        validatePW = false
                    } else {
                        checkIDPW.setTextColor(-0x919192)
                        checkIDPW.setText("사용 가능한 비밀번호입니다.")
                        validatePW = true
                    }
                } else  //6글자 미만
                {
                    checkIDPW.setTextColor(-0x5510b7b6)
                    checkIDPW.setText("영문,숫자 포함 10자 이내내")
                   validatePW = false
                }
            }
        })


        btn_CheckID.setOnClickListener(
            View.OnClickListener
            //ID중복검사 버튼
            {
                val id: String = edit_joinID.getText().toString()
                if (validateID) {
                    return@OnClickListener  //검증 완료
                }
                if (id == "") { //아이디 입력안했을 때
                    Toast.makeText(this@JoinActivity, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                //DB에  가입된 아이디인지 체크하는 함수
                checkID(id)
            })

    }



    private fun checkID(id:String){

        RetrofitBuilder.api.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Toast.makeText(this@JoinActivity, "이미 가입된 아이디입니다", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d(TAG, "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)

                Log.d(TAG, "테이블에 존재하지 않는 ID라 등록가능" + t.message.toString())

                if (t.message == "End of input at line 1 column 1 path $") {

                    Toast.makeText(this@JoinActivity, "사용가능한 아이디입니다", Toast.LENGTH_SHORT).show()
                    edit_joinID.setEnabled(false) //아이디값 고정
                    validateID = true //검증 완료
                }
            }
        })
    }
}