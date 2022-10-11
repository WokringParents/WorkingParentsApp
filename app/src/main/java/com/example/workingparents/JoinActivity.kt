package com.example.workingparents

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_join.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern
import javax.mail.MessagingException
import javax.mail.SendFailedException

class JoinActivity : AppCompatActivity() {

    private val TAG="RFS"
    private var validatePW = false  //비밀번호 형식통과했는지
    private var validateID= false  //아이디 중복검사했는지
    lateinit var token : String     //사용자가 앱깔때 들어온 토큰
    private var validateEmailFormat = false //이메일 형식통과했는지
    private var validateEmail = false //이메일 중복검사했는지
    private var sysNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)


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
            Log.d("MSG","이메일 버튼 클릭")


            var email = edit_email.text.toString()
            if (validateEmail) {
                return@OnClickListener  //검증 완료
            }

            if(validateEmailFormat) {
                checkEmail(email)
            }else{
                Toast.makeText(this@JoinActivity, "유효한 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            //이메일 체크하여 DB에 가입되어있지 않은 이메일인 경우 해당 메일로 인증번호 전송, 아닐경우 메세지 출력





        })
*/
/*
        btn_joinFinish.setOnClickListener(View.OnClickListener {

            var id = edit_joinID.text.toString()
            var pw= edit_joinPW.text.toString()
            var checkPW= edit_checkPW.text.toString()
            var email= edit_email.getText().toString()
            var userNumber = edit_emailCode.text.toString()


            if (id == "" || pw == "" || checkPW == "" || !this::token.isInitialized) {
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
            else if (userNumber != sysNumber.toString()) {
                Toast.makeText(this@JoinActivity, "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, JoinActivity2::class.java)
                intent.putExtra("id", id)
                intent.putExtra("pw", pw)
                intent.putExtra("email", email)
                intent.putExtra("token", token)
                startActivity(intent)

                }


        })





    }

    private fun checkEmail(email: String) {
        //DB에 이미 저장된 메일이 있는지 검사, 없다면? 검증완료 된 것,그리고 메일을 발송함
        Log.d("MSG","이메일 체크 진입")
        RetrofitBuilder.api.getUserbyEmail(email).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Log.d("MSG","레트로핏 성공")
                    emailText.setTextColor(-0x5510b7b6)
                    emailText.text = "이미 가입된 이메일입니다"
                }else{
                    Log.d(TAG, "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("MSG","레트로핏 실패")
                emailText.setTextColor(-0x919192)
                emailText.text = "인증번호를 전송합니다."

                edit_email.setEnabled(false)
                validateEmail=true

                edit_email.isEnabled = false //이메일값 고정
                val random = Random() //인증번호 발생시키는 부분

                val range = Math.pow(10.0, 6.0).toInt()
                val trim = Math.pow(10.0, 5.0).toInt()
                sysNumber = random.nextInt(range) + trim
                if (sysNumber > range) {
                    sysNumber = sysNumber - trim
                }
                Log.d("tag", sysNumber.toString() + "")

                val thread = SendNumberByMailThread(email, sysNumber.toString())
                emailText.setText("인증번호 전송완료") //이자리가 맞을까
                //킹치만 핸들러도, 스레드에서 UI건드리기도 실패한 난..어쩔수없었어

                thread.start()

            }
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



    internal class SendNumberByMailThread(var receptEmail: String, number: String) :
        Thread() {
        var content: String
        override fun run() {

            try {
                val mailSender = NaverMailSender()

                mailSender.sendMail(
                    "[워킹패런츠 앱] 애플리케이션 회원가입 인증번호",
                    content, receptEmail
                )

            } catch (e: SendFailedException) {

                Log.d("MSG","3"+e.printStackTrace())
            } catch (e: MessagingException) {
                Log.d("MSG","4"+e.printStackTrace())
            } catch (e: Exception) {
                Log.d("MSG","5"+e.printStackTrace())
            }
        }

        init {
            content =
                "안녕하세요, 워킹패런츠 앱입니다.<br>아래의 인증번호 6자리를 인증번호 입력창에 입력 후 회원가입을 진행해주세요.<br><br>인증번호: $number<br>"
        }


    }


}