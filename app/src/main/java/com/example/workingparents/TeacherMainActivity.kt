package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.workingparents.Goback.GobackFragment
import com.example.workingparents.Notice.NoticeFragment
import com.example.workingparents.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//MainActivity.kt
class TeacherMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val TAG = "FCM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_main)

        val intent: Intent = getIntent()
        // val LoginUser = intent.getParcelableExtra<User>("LoginUser")
        //checkcouple()

/*
        couplePageBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@MainActivity, CoupleConnectActivity::class.java)
            intent.putExtra("LoginUser", LoginUser)
            startActivity(intent)
        })*/


        var bnv_main = findViewById(R.id.teacher_bottom_menu) as BottomNavigationView
        var menu= bnv_main.menu
        bnv_main.itemIconTintList = null
        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.

        //바텀바 아이템 꾹누르면 이름 떴던거 없앰
        val longClickListener = View.OnLongClickListener { true }
        findViewById<View>(R.id.first_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.second_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.third_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.fourth_tab).setOnLongClickListener(longClickListener)

        bnv_main.run {

            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first_tab -> {
                        //다른 프래그먼트 화면으로 이동하는 기능
                        val MainFragment = GobackFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, MainFragment).commit()

                        //아이콘 변경
                        it.setIcon(R.drawable.icon_goback2)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)
                    }
                    R.id.second_tab -> {
                        val NoticeFragment = NoticeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, NoticeFragment).commit()


                        it.setIcon(R.drawable.icon_notice2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)



                    }
                    R.id.third_tab -> {
                        val CafeteriaFragment = CafeteriaFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CafeteriaFragment).commit()


                        it.setIcon(R.drawable.icon_cafeteria2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)
                    }

                    R.id.fourth_tab -> {
                        val TeacherMyPageFragment = TeacherMyPageFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, TeacherMyPageFragment).commit()


                        it.setIcon(R.drawable.bottom_mypage2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                    }



                }
                true
            }
            selectedItemId = R.id.first_tab

        }

        // val retrofit = Builder().baseUrl("http://workingparents-env-1.eba-ysfya3ek.ap-northeast-2.elasticbeanstalk.com/")
        //    .addConverterFactory(GsonConverterFactory.create()).build();
        //val service = retrofit.create(RetrofitService::class.java);

        /*
        RetrofitBuilder.api.getUser("testid").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성고된 경우
                    var result: User? = response.body()
                    Log.d("TAG", "onResponse 성공: " + result?.toString());
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("TAG", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("TAG", "onFailure 에러: " + t.message.toString());
            }
        })*/

        /*JSON
        * {"payerReg":{"crc":"aas22","payerDevManufacturer":"Samsung"}}
        *
        * */


/*

        val obj = JsonObject()
        val notification = JsonObject()
        notification.addProperty("title", "04:00 fore그라운드 오류나지마")
        notification.addProperty("body", "제발 코딩꾸버신이여 저를 도와주소서")
        obj.addProperty("to","c7UAgs7nSYKeqr_6zFeDpq:APA91bGJmhvQzbtW396sZu2l9vWxKxROIe8A5BXpUArDGF7ps5TQqyqs6H5xt5opSX0o6WqLdNlOjO2QVi3IBSGZ9AhBG9dsVxAcZ9EY5sRI80LJX7h55-ONY9ISmBg_6wpqaAtlhMh-")
        obj.add("notification", notification)
*/


        //    val token: String = "c7UAgs7nSYKeqr_6zFeDpq:APA91bGJmhvQzbtW396sZu2l9vWxKxROIe8A5BXpUArDGF7ps5TQqyqs6H5xt5opSX0o6WqLdNlOjO2QVi3IBSGZ9AhBG9dsVxAcZ9EY5sRI80LJX7h55-ONY9ISmBg_6wpqaAtlhMh-"
        //


        //상대방 핸드폰에 푸시알람보내는 것임!!!!
/*
        pushBtn.setOnClickListener(View.OnClickListener {

            RetrofitBuilder.api.getUser("qurtks2224").enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    var result: User? = response.body()

                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse 성공: $result");
                        if (result != null) {
                            requestPushAlram(result.token)
                        }

                    } else {
                        Log.d(TAG, "onResponse 실패: ");
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure 에러 " + t.message.toString());
                }
            })


        })*/

    }


    fun requestPushAlram(token: String) {

        val obj = FCMRetrofitBuilder.takeJsonObject(token, "08-05 16:51 포그라운드", "경주 핸드폰 푸시알람테스트")

        FCMRetrofitBuilder.api.pushAlram(obj.toString()).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse 성공: " + response?.body().toString());
                } else {
                    Log.d(TAG, "onResponse 실패: ");
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure 에러: " + t.message.toString());
            }
        })
    }


}