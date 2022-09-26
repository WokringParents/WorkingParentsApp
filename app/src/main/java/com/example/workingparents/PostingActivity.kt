package com.example.workingparents

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.PostingActivity.Companion.post_ccnt
import kotlinx.android.synthetic.main.activity_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private val TAG="PostingActivity"
private var comments = ArrayList<Comment>()
private var ccomments = ArrayList<Ccomment>()
private val dataList = ArrayList<Dataitem>()
private var postingcno :Int = 0
private var pccnt:Int = 0
private var adapter: CommentAdapter? = null



class PostingActivity : AppCompatActivity() {

    init {
        instance=this
    }

    companion object{
        lateinit var instance:PostingActivity
        fun PostingActivityContext() : Context{
            return instance.applicationContext
        }
        lateinit var recyclerView: RecyclerView
        lateinit var handler: Handler
        lateinit var msg : Message
        var pno by Delegates.notNull<Int>()
        lateinit var post_ccnt : TextView
        lateinit var input : EditText
        lateinit var sendbtn : ImageButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        recyclerView= findViewById(R.id.comment_rv)
        post_ccnt=findViewById(R.id.ccnt)
        input=findViewById(R.id.input1)
        sendbtn=findViewById(R.id.sendBtn)

        pid.setText(intent.getStringExtra("rv_pid"))
        village.setText(intent.getStringExtra("rv_village"))
        goback.setText(intent.getStringExtra("rv_goback"))
        pcontent.setText(intent.getStringExtra("rv_pcontent"))
        hcnt.setText(intent.getStringExtra("rv_hcnt"))
        post_ccnt.setText(intent.getStringExtra("rv_ccnt"))
        pccnt = intent.getStringExtra("rv_ccnt")?.toInt()!!
        pno = intent.getStringExtra("rv_pno")?.toInt()!!

        var post_time : String = intent.getStringExtra("rv_pdate")!!
        val stringBuilder = StringBuilder()

        stringBuilder.append(post_time.substring(5,7))
        stringBuilder.append("/")
        stringBuilder.append(post_time.substring(8,10))
        stringBuilder.append(" ")
        stringBuilder.append(post_time.substring(11,16))
        posttime.setText(stringBuilder.toString())


        Log.d(TAG, "왜 시간 표시가 안되냐고"+(intent.getStringExtra("rv_pdate")))


        comments.clear()
        ccomments.clear()
        dataList.clear()

        comment_rv.layoutManager= LinearLayoutManager(this@PostingActivity, LinearLayoutManager.VERTICAL, false)
        handler=MainHandler()

        val checkCmentThread = CheckCmentThread(pno) //insert thread 불러오기
        checkCmentThread.start()


        Log.d(TAG, pno.toString())
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //댓글달기 클릭시 키보드+입력창 올라옴
        input.setOnClickListener {
            input.post(Runnable {
                input.setFocusableInTouchMode(true)
                input.requestFocus()
                imm.showSoftInput(input, 0)
            })

        }

        sendbtn.setOnClickListener {
            var comment = input.text.toString()

            if (pno != null) {
                //checkcomment해서 cno증가시켜서 추가불러오기

                Log.d(TAG, "333333333333333")
                Log.d(TAG,"checkThread 왔니")
                val insertCmentThread = InsertCmentThread(pno,comment) //insert thread 불러오기
                insertCmentThread.InsertCmentThread2(pno,comment)
                insertCmentThread.start()
                input1.setText(null)
                imm.hideSoftInputFromWindow(input1.windowToken,0)
            }

            Log.d(TAG, pno.toString())
            Log.d(TAG, postingcno.toString())
            Log.d(TAG, UserData.id.toString())
            Log.d(TAG, comment)

        }
    }


    class CheckCmentThread(pno: Int?) : Thread() {
        private var pno = pno
        override fun run() {
            super.run()
            pno?.let {
                RetrofitBuilder.api.getComment(it).enqueue(object : Callback<List<Comment>>{
                    override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                        if (response.isSuccessful) {

                            Log.d(TAG, "1111111111111111")
                            var result: List<Comment>? = response.body()
                            comments= response.body() as ArrayList<Comment>
                            //일부러 따로해줌
                            Log.d(TAG, "222222222222")
                            msg= handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_GET_CMENTS)
                            handler.handleMessage(msg)

                        } else {
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        }
                    }

                    override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                        Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
                    }

                })
            }
        }

    }


    class InsertCmentThread(pno: Int?, comment: String) : Thread() {
        private var pno = 0
        private var cment: String? = null
        fun InsertCmentThread2(pno: Int, cment: String) {
            Log.d(TAG, "4444444444444")
            this.pno = pno
            this.cment = cment
            Log.d(TAG, comments.toTypedArray().toString() + "")
            if (pno != null) {
                if (comments.isEmpty()) {
                    Log.d(TAG, "empty하다")
                    postingcno = 1
                } else {
                    for (comment in comments) {         //제일큰 cno검증
                        if (comment.cno > postingcno) {
                            postingcno = comment.cno
                        }
                    }
                    postingcno++
                    Log.d(TAG, "cno 증가시켜"+postingcno)
                }
            }
        }
        override fun run() {
            super.run()
            RetrofitBuilder.api.postComment(pno, postingcno, UserData.id, cment)
                .enqueue(object : Callback<Comment> {
                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "55555555555555")

                            var result: Comment? = response.body()
                            // 정상적으로 통신이 성공된 경우
                            Log.d(TAG, "onResponse: 댓글 성공" + result?.toString())

                            comments.add(result!!)
                            var dataitem= Dataitem()
                            dataitem.Commentitem(result,1)
                            adapter?.cmentDataList?.add(dataitem)
                            adapter?.notifyDataSetChanged()
                            updateCmentCnt(pno,"plus") //댓글 수 증가
                            Log.d(TAG,"여기 notify 전 result" + comments.toString())

                        } else {
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        }
                    }
                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
                    }

                })
        }

    }

    class CheckCcmentThread(pno: Int?) : Thread() {
        private var pno = pno
        override fun run() {
            super.run()
            pno?.let {
                RetrofitBuilder.api.getCcomment(it).enqueue(object : Callback<List<Ccomment>> {

                    override fun onResponse(call: Call<List<Ccomment>>, response: Response<List<Ccomment>>) {
                        if (response.isSuccessful) {

                            var result: List<Ccomment>? = response.body()
                            ccomments= response.body() as ArrayList<Ccomment>
                            msg = handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_GET_CCMENTS)
                            handler.sendMessage(msg)

                            // 정상적으로 통신이 성공된 경우
                            Log.d(TAG, "onResponse: thread 대댓글 불러오기 성공" + result?.toString())

                        } else {
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        }
                    }

                    override fun onFailure(call: Call<List<Ccomment>>, t: Throwable) {
                        Log.d(TAG, "onFailure 대댓글 실패 : " + t.message.toString())
                    }

                })
            }
        }
    }


    internal class MainHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {

                StateSet.BoardMsg.MSG_SUCCESS_GET_CMENTS->{
                    for (cment in comments) {
                        var withcment = false
                        if (cment.cccnt > 0) {
                            Log.d(TAG, "1번째 데이터리스트"+dataList.toString())
                            val CheckCcmentThread = CheckCcmentThread(pno)
                            //insert thread 불러오기
                            CheckCcmentThread.start()
                            withcment=true
                            break
                        }
                        if(withcment==false){
                            //대댓글 하나도 없을 경우 바로 보여줌
                            var dataitem= Dataitem()
                            dataitem.Commentitem(cment,1)
                            dataList.add(dataitem)
                            Log.d(TAG, "대댓글 없음 dataitem추가됨" + dataitem)
                        }
                    }
                    var msg : Message = handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_GET_ALLCMENTS)
                    handler.sendMessage(msg)
                }

                StateSet.BoardMsg.MSG_SUCCESS_GET_CCMENTS->{

                    //대댓글 받아오면 dataList를 리사이클러뷰 구성할 수있도록 순서 맞춰줘야함.
                    //에혀 걍 DB에서 댓글별로 대댓글 각각 들고오는거 말고 포스팅별로 대댓글 전체 다 들고오는게 훨씬 더 편했을 듯.  ---->수정함 12/01
                    for (cment in comments) {
                        var dataitem= Dataitem()
                        dataitem.Commentitem(cment,1)
                        dataList.add(dataitem)
                        Log.d(TAG, "여기는 handler 안 대댓글 있는 댓글 추가")
                        for (ccment in ccomments) {
                            if (cment.cno === ccment.cno) {
                                var dataitem= Dataitem()
                                dataitem.Ccommentitem(ccment,2)
                                dataList.add(dataitem)
                                Log.d(TAG, "여기는 handler 안 대댓글 추가")

                            }
                        }
                    }
                    var msg : Message = handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_GET_ALLCMENTS)
                    handler.sendMessage(msg)

                }

                StateSet.BoardMsg.MSG_SUCCESS_GET_ALLCMENTS -> {

                    //리사이클러뷰 선언
                    Log.d(TAG, "3번째 데이터리스트"+dataList.toString())
                    recyclerView.visibility=View.VISIBLE
                    recyclerView.setHasFixedSize(true) //리사이클러뷰 성능 개선?
                    adapter= CommentAdapter(dataList)
                    recyclerView.adapter= adapter

                }
            }
        }
    }


}

fun updateCmentCnt(pno: Int, sign: String) {

    //삭제 시 ccnt를 마이너스 시키고 다시 보여줌
    if(sign=="minus")
    {
        pccnt--
       post_ccnt.setText(pccnt.toString())
    }
    else if(sign=="plus")
    {
        pccnt++
        post_ccnt.setText(pccnt.toString())
    }


    RetrofitBuilder.api.putCommentCnt(pno, sign).enqueue(object : Callback<Int> {

        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            if (response.isSuccessful) {

                var result: Int? = response.body()
                // 정상적으로 통신이 성공된 경우
                Log.d(TAG, "onResponse: 댓글 조회수 성공" + result?.toString())

            } else {
                // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {
            Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
        }

    })
}

fun updateCcmentCnt(pno: Int, cno:Int, sign: String) {

    //삭제 시 ccnt를 마이너스 시키고 다시 보여줌
    if(sign=="minus")
    {
        pccnt--
        post_ccnt.setText(pccnt.toString())
    }
    else if(sign=="plus")
    {
        pccnt++
        post_ccnt.setText(pccnt.toString())
    }

    RetrofitBuilder.api.putCcommentCnt(pno,cno,sign).enqueue(object : Callback<Int> {

        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            if (response.isSuccessful) {

                var result: Int? = response.body()
                if (sign == "plus") {
                    Log.d("tag", "cccnt++")
                } else {
                    Log.d("tag", "cccnt--")
                }

            } else {
                // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {
            Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
        }

    })
}
