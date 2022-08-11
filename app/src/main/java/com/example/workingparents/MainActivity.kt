package com.example.workingparents


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val TAG = "FCM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      //  val intent: Intent = getIntent()
      //  val LoginUser = intent.getParcelableExtra<User>("LoginUser")

        couplePageBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@MainActivity, CoupleConnectActivity::class.java)
        //    intent.putExtra("LoginUser", LoginUser)
            startActivity(intent)

        })

        var bnv_main = findViewById(R.id.bottom_menu) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first_tab -> {
                   //다른 프래그먼트 화면으로 이동하는 기능
                        val MainFragment = MainFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment).commit()
                    }
                    R.id.second_tab -> {
                        val boardFragment = BoardFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.container, boardFragment).commit()
                    }
                    R.id.third_tab -> {
                        val InfoFragment = InfoFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.container, InfoFragment).commit()
                    }

                    R.id.fourth_tab -> {
                        val MypageFragment = MypageFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.container, MypageFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.first_tab
        }


        //상대방 핸드폰에 푸시알람보내는 것임!!!!
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
                    Log.d(TAG, "onFailure 에러: " + t.message.toString());
                }
            })


        })

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