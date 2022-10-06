package com.example.workingparents

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workingparents.PostingActivity.Companion.handler
import com.example.workingparents.PostingActivity.Companion.recyclerView
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_posting_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG="PS"

class PostingSearchActivity : AppCompatActivity() {

    companion object{
        lateinit var msg : Message
        private var adapter: PostingAdapter? = null
        lateinit var searchImage : ImageView
        lateinit var nosearchImage : ImageView
        lateinit var searchment : TextView
        lateinit var search : EditText

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting_search)

        handler=MainHandler()
        recyclerView= findViewById(R.id.search_recyclerview)
        recyclerView.addItemDecoration(MyDecoration(10, Color.parseColor("#f2f2f2")))
        recyclerView.layoutManager= LinearLayoutManager(this@PostingSearchActivity, LinearLayoutManager.VERTICAL, false)
        searchImage = findViewById(R.id.searchGlassImage)
        searchment = findViewById(R.id.searchMent)
        nosearchImage=findViewById(R.id.searchNoImage)
        search =findViewById(R.id.searchbar)


        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //검색창 클릭시 키보드+입력창 올라옴
        searchbar.setOnClickListener {
            searchbar.post(Runnable {
                searchbar.setFocusableInTouchMode(true)
                searchbar.requestFocus()
                imm.showSoftInput(searchbar, 0)
                searchbar.setImeOptions(EditorInfo.IME_ACTION_DONE);

            })
        }

        //입력 확인 눌렀는지 확인시키느 코드
        searchbar.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchbar.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG,"입력완료")
                    val inputWord = searchbar.text.toString()
                    Log.d(TAG,inputWord)
                    val getPostingThread = GetPostingThread(inputWord) //insert thread 불러오기
                   // getPostingThread.GetPostingThread(inputWord)
                    getPostingThread.start()
                    return true
                }
                return false
            }
        })

    }

    class GetPostingThread(pcontent: String?) : Thread() {
        private var pcontent = pcontent
        override fun run() {
            super.run()
            RetrofitBuilder.api.getPostingbyContent(pcontent).enqueue(object : Callback<List<Posting>> {
                override fun onResponse(call: Call<List<Posting>>, response: Response<List<Posting>>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "1111111111111111")
                        var result: List<Posting>? = response.body()
                        Log.d(TAG, result.toString())

                        if (search.getText().length > 0) {
                            //이전 검색결과 없음 기록이 남아있다면
                            search.setText(null)
                        }

                        searchImage.visibility=INVISIBLE
                        nosearchImage.visibility= INVISIBLE
                        searchment.visibility= INVISIBLE
                        recyclerView.visibility=VISIBLE
                        recyclerView.setHasFixedSize(true) //리사이클러뷰 성능 개선?
                        adapter= PostingAdapter(result as ArrayList<Posting>)
                        recyclerView.adapter= adapter
                        adapter?.notifyDataSetChanged()


                        if (result!!.isEmpty()) {

                            Log.d(TAG, "검색 결과 아무것도 없음")
                            msg = handler.obtainMessage(StateSet.BoardMsg.MSG_SUCCESS_SEARCH)
                            handler.handleMessage(msg)
                            recyclerView.visibility= INVISIBLE
                            searchImage.visibility=INVISIBLE
                            nosearchImage.visibility= VISIBLE
                            searchment.setText("검색 결과가 존재하지 않습니다")
                            searchment.visibility= VISIBLE


                        } else {
                            msg = handler.obtainMessage(StateSet.BoardMsg.MSG_SEARCH_NO_WORD)
                            handler.handleMessage(msg)
                        }
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    }
                }

                override fun onFailure(call: Call<List<Posting>>, t: Throwable) {
                    Log.d(TAG, "onFailure 검색 실패 : " + t.message.toString())
                }
            })
        }
    }

    internal class MainHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {

                StateSet.BoardMsg.MSG_SUCCESS_SEARCH->{
                }

                StateSet.BoardMsg.MSG_SEARCH_NO_WORD->{
                }

            }
        }
    }

}