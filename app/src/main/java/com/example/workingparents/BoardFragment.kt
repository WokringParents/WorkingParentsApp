package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG="Board"
private lateinit var mContext:Activity
private var postings = ArrayList<Posting>()
private var adapter: PostingAdapter? = null


class BoardFragment : Fragment(){

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            mContext=context
        }
    }

    //Fragment의 뷰를 그릴 때 호출되는 콜백
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var view =inflater.inflate(R.layout.fragment_board, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_posting)

        postings.clear()
        RetrofitBuilder.api.getPosting().enqueue(object : Callback<List<Posting>> {
            override fun onResponse(call: Call<List<Posting>>, response: Response<List<Posting>>) {
                if (response.isSuccessful) {
                    var result= response.body()
                    if (result != null) { //recyclerview에 넣을 arraylist에 result 값을 넣는 for문
                        for (posting in result) {
                            postings.add(posting)
                        }
                    }


                    //divider
                    recyclerView.addItemDecoration(MyDecoration(10, Color.parseColor("#f2f2f2")))
                    recyclerView.layoutManager= LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                    //리사이클러뷰 선언
                    recyclerView.visibility=View.VISIBLE
                    recyclerView.setHasFixedSize(true) //리사이클러뷰 성능 개선?
                    adapter= PostingAdapter(postings)
                    recyclerView.adapter= adapter//adapter 선언

                    Log.d(TAG, "onResponse: 게시글 불러오기 성공" + result)

                } else {
                    Log.d(TAG, "onResponse 후 실패 에러: ")
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }
            override fun onFailure(call: Call<List<Posting>>, t: Throwable) {
                Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
            }

        })


        val writePostingBtn = view.findViewById<ImageButton>(R.id.writeposting_btn)

        writePostingBtn.setOnClickListener{
            Log.d(TAG,"클릭됨")
            val intent = Intent(mContext,WritePostingActivity::class.java)
            mContext.startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

}

