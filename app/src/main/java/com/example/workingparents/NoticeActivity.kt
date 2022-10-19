package com.example.workingparents

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private val noticeData = ArrayList<NoticeData>()
private val TAG = "NoticeActivity"
lateinit var noticedetailAdapter: NoticeDetailAdapter

class NoticeActivity : AppCompatActivity() {

    companion object{

        lateinit var noticedetailrv: RecyclerView
        lateinit var recyclerView: RecyclerView
        lateinit var context : Context
        lateinit var handler: Handler
        lateinit var msg : Message
        var position by Delegates.notNull<Int>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)


        context = this@NoticeActivity
        noticetitle.setText(intent.getStringExtra("rv_title"))
        //xml id랑 lateinit var 선언 이름을 똑같이 하지말것 인식을 못함 이유는 모름
        noticedetailrv=findViewById(R.id.noticedetail_rv)

        var notice_time : String = intent.getStringExtra("rv_date")!!
        val stringBuilder = StringBuilder()

        stringBuilder.append(notice_time.substring(5,7))
        stringBuilder.append("/")
        stringBuilder.append(notice_time.substring(8,10))
        noticedate.setText(stringBuilder.toString())

        noticeData.clear()
        handler = MainHandler()
        var dataitem= NoticeData()
        dataitem.Noticeitem(intent.getStringExtra("rv_content"),0)
        noticeData.add(dataitem)

        var nid : Int = intent.getStringExtra("rv_nid")!!.toInt()
        var tid : Int = intent.getStringExtra("rv_tid")!!.toInt()
        position = intent.getStringExtra("rv_position")!!.toInt()
        Log.d(TAG,"nid"+nid)


        noticeActivity_back.setOnClickListener{
            onBackPressed()
        }

        RetrofitBuilder.api.getImagebynid(nid)
            .enqueue(object : Callback<List<Image>> {
                override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                    if (response.isSuccessful) {

                        var result: List<Image>? = response.body()
                        Log.d(TAG,result.toString())
                        Log.d(TAG,"111111111111111")
                        for(i in 0 until result!!.size){
                            var dataitem= NoticeData()
                            dataitem.Noticeitem(result.get(i).image,1)
                            noticeData.add(dataitem)
                        }
                        Log.d(TAG,"222222222222222")
                        msg = handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_GET_NOTICES)
                        handler.handleMessage(msg)
                        // 정상적으로 통신이 성공된 경우
                        Log.d(TAG, "onResponse: 다중 이미지 불러오기 성공" + result?.toString())

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    }
                }
                override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                    Log.d(TAG, "onFailure 다중 이미지 불러오기 실패 : " + t.message.toString())
                }
            })


        nbtn.setOnClickListener {
            if(tid.equals(1))
            {
                Log.d(TAG,"삭제 버튼 진입")
                val builder = AlertDialog.Builder(this)
                    .setMessage("알림장을 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            deleteNotice(nid)
                            finish()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, which ->
                        })
                builder.show()
            }
        }
    }


    internal class MainHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {

                StateSet.BoardMsg.MSG_SUCCESS_GET_NOTICES -> {
                    Log.d(TAG,"현재 크기"+noticeData.size.toString())
                    noticedetailrv.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    //리사이클러뷰 선언
                    noticedetailrv.visibility= View.VISIBLE
                    noticedetailrv.setHasFixedSize(true) //리사이클러뷰 성능 개선?
                    noticedetailAdapter = NoticeDetailAdapter(noticeData, context)
                    noticedetailrv.adapter= noticedetailAdapter//adapter 선언
                    Log.d(TAG,"333333333333333333")
                }
            }
        }
    }

    fun deleteNotice(nid : Int){
        RetrofitBuilder.api.deleteNotice(nid!!).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {

                    var result: Int? = response.body()
                    deleteNoticeAdapter(position)

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 대댓글 실패 : " + t.message.toString())
            }

        })
    }

}