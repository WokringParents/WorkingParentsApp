package com.example.workingparents


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//MainActivity.kt
class MainActivity : AppCompatActivity() {

    //깃허브 테스트용
    private val TAG="FCM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent: Intent = getIntent()
        val LoginUser= intent.getParcelableExtra<User>("LoginUser")

        couplePageBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@MainActivity, CoupleConnectActivity::class.java)
            intent.putExtra("LoginUser",LoginUser)
            startActivity(intent)

        })

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

        pushBtn.setOnClickListener(View.OnClickListener {

            RetrofitBuilder.api.getUser("qurtks2224").enqueue(object:Callback<User>{

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    var result: User? = response.body()

                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse 성공: $result");
                        if (result != null) {
                            requestPushAlram(result.token)
                        }

                    }else{
                        Log.d(TAG, "onResponse 실패: ");
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure 에러: " + t.message.toString());
                }
            })


        })

    }


fun requestPushAlram(token: String){

    val obj= FCMRetrofitBuilder.takeJsonObject(token,"08-05 16:51 포그라운드","경주 핸드폰 푸시알람테스트")

   FCMRetrofitBuilder.api.pushAlram(obj.toString()).enqueue(object: Callback<ResponseBody>{

       override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
          if(response.isSuccessful){
              Log.d(TAG, "onResponse 성공: " + response?.body().toString());
          }else{
              Log.d(TAG, "onResponse 실패: ");
          }
       }
       override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
           Log.d(TAG, "onFailure 에러: " + t.message.toString());
       }
   })

}




}