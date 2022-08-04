package com.example.workingparents

import android.content.ContentValues
import android.content.Intent
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
    private var validatePW = false  //비밀번호 형식통과했는지
    private var validateID= false  //아이디 중복검사했는지
    lateinit var token : String     //사용자가 앱깔때 들어온 토큰
    private var validateEmailFormat = false //이메일 형식통과했는지
    private var validateEmail = false //이메일 중복검사했는지
    lateinit private var sex: String

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
            token = task.result
            Log.d("FBS", "토큰 받아오는 타이밍:$token")

        })


        btn_CheckID.setOnClickListener( View.OnClickListener
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


        edit_joinPW.addTextChangedListener(object : TextWatcher {
            //비밀번호에 리스너를 달아서 비밀번호 형식에 맞는지 검사 부분
            //형식은 그냥 영문,숫자 포함 4자리이상 10자리 이하로 지정하였습니다.
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length >=4) {
                    val p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{4,10}$")
                    val m = p.matcher(s.toString())
                    if (!m.matches()) {
                        checkIDPW.setTextColor(-0x5510b7b6)
                        checkIDPW.setText("영문,숫자 포함 10자이내")
                        validatePW = false
                    } else {
                        checkIDPW.setTextColor(-0x919192)
                        checkIDPW.setText("사용 가능한 비밀번호입니다.")
                        validatePW = true
                    }
                } else  //6글자 미만
                {
                    checkIDPW.setTextColor(-0x5510b7b6)
                    checkIDPW.setText("영문,숫자 포함 4자리 이상")
                    validatePW = false
                }
            }
        })



        //이메일 형식에 맞는지 검사 부분
        // @랑 @뒤에 . 하나라도 있는지 보면될듯,, com형식으로 안끝나는 이메일들도 있어가지구 예: 저희학교메일 @ynu.ac.kr
        edit_email.addTextChangedListener(object : TextWatcher {
            //비밀번호에 리스너를 달아서
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val p = Pattern.compile("^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z.-]+\\.[a-zA-Z]{2,6}$")
                val m = p.matcher(s.toString())

                if(!m.matches()){
                    emailText.setTextColor(-0x5510b7b6)
                    emailText.setText("이메일형식이 잘못되었습니다.")
                    validateEmailFormat= false
                }else{
                    emailText.setText("")
                    validateEmailFormat = true
                }

            }
        })


        btn_joinCode.setOnClickListener(View.OnClickListener {
            //이메일 코드 전송 버튼 클릭시
            //일단은 혹시몰라서 버튼만 누르면 그냥 검사 성공했다고 표현함

            if (validateEmail) {
                return@OnClickListener  //검증 완료
            }

            val email = edit_email.text.toString()

            if (email == "") { //사용자가 입력하지 않았을 시
                emailText.setTextColor(-0x919192)
                Toast.makeText(this@JoinActivity, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
                emailText.text = "이메일을 입력하십시오"
                return@OnClickListener
            }

            if (validateEmailFormat == false) {
                Toast.makeText(this@JoinActivity, "이메일형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }

            //이메일 체크하여 DB에 가입되어있지 않은 이메일인 경우 해당 메일로 인증번호 전송, 아닐경우 메세지 출력
            checkEmail(email)
        })


        radio_group_join.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.momBtn -> sex =  "M"
                R.id.dadBtn -> sex =  "D"
            }
        }


        btn_joinFinish.setOnClickListener(View.OnClickListener {

            var id = edit_joinID.text.toString()
            var pw= edit_joinPW.text.toString()
            var checkPW= edit_checkPW.text.toString()
            var email= edit_email.getText().toString()

            if (id == "" || pw == "" || checkPW == "" || !this::sex.isInitialized || !this::token.isInitialized) {
                Toast.makeText(this@JoinActivity, "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (validateID == false) {
                Toast.makeText(this@JoinActivity, "아이디 중복확인을 진행해주세요", Toast.LENGTH_SHORT).show()
            } else if (validatePW == false) {
                Toast.makeText(this@JoinActivity, "유효한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (validateEmail == false) {
                Toast.makeText(this@JoinActivity, "이메일 인증번호 전송을 진행해주세요", Toast.LENGTH_SHORT).show()
            } else if (!(pw == checkPW)) {
                Toast.makeText(this@JoinActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
            /*
            else if (userNumber != sysNumber.toString()) {
                Toast.makeText(this@JoinActivity, "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } */
            else {
                joinApplication(id, pw, email, sex, token) //모든 조건 완료시 회원 가입 시키는 함수
            }

        })


    }

    private fun joinApplication(id: String, pw: String, email: String, sex: String, token: String) {

    RetrofitBuilder.api.postUser(id, pw, email, sex, token).enqueue(object: Callback<Int>{

        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            if(response.isSuccessful){

                var result: Int? = response.body()
                // 정상적으로 통신이 성공된 경우
                Log.d(TAG, "onResponse: 회원등록성공"+result?.toString())
                Toast.makeText(this@JoinActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@JoinActivity, MainActivity::class.java)
                    startActivity(intent)
            }else{
                // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                Toast.makeText(this@JoinActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {
            Log.d(TAG, "onFailure 회원가입 실패 에러: " + t.message.toString())
            Toast.makeText(this@JoinActivity, "회원가입 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show()

        }

    })


    }


    private fun checkEmail(email: String) {

        //DB에 이미 저장된 메일이 있는지 검사, 없다면? 검증완료 된 것,그리고 메일을 발송함
        //마지막에 회원가입 버튼 클릭시에 시스템 생성 인증번호와 사용자 입력 인증번호가 같은지 체크함
        edit_email.setEnabled(false)
        validateEmail=true

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