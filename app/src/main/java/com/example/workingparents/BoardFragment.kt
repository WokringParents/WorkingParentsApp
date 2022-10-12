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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.BoardFragment.Companion.Postingadapter
import com.example.workingparents.BoardFragment.Companion.postings
import com.example.workingparents.BoardFragment.Companion.recyclerView
import kotlinx.android.synthetic.main.fragment_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var TAG="Board"

class BoardFragment : Fragment(){

    companion object{
        private lateinit var mContext:Activity
        lateinit var postings : ArrayList<Posting>
        lateinit var Postingadapter: PostingAdapter
        lateinit var recyclerView: RecyclerView
    }

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
        recyclerView = view.findViewById<RecyclerView>(R.id.rv_posting)


        //포스팅 불러오기
        RetrofitBuilder.api.getPosting().enqueue(object : Callback<List<Posting>> {
            override fun onResponse(call: Call<List<Posting>>, response: Response<List<Posting>>) {
                if (response.isSuccessful) {
                    postings= response.body() as ArrayList<Posting>
                    //결과를 postings에 넣기
                    Log.d(TAG, postings.get(0).toString())

                    //divider
                    recyclerView.addItemDecoration(MyDecoration(10, Color.parseColor("#f2f2f2")))
                    recyclerView.layoutManager= LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                    //리사이클러뷰 선언
                    recyclerView.visibility=View.VISIBLE
                    recyclerView.setHasFixedSize(true) //리사이클러뷰 성능 개선?
                    Postingadapter= PostingAdapter(postings)
                    recyclerView.adapter= Postingadapter//adapter 선언


                } else {
                    Log.d(TAG, "onResponse 후 실패 에러: ")
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }
            override fun onFailure(call: Call<List<Posting>>, t: Throwable) {
                Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
            }

        })

        //작성하기 클릭 시
        val writePostingBtn = view.findViewById<ImageButton>(R.id.writeposting_btn)
        writePostingBtn.setOnClickListener{
            Log.d(TAG,"클릭됨")
            val intent = Intent(mContext,WritePostingActivity::class.java)
            mContext.startActivity(intent)
        }

        //검색창 클릭시
        val postingSearchBtn = view.findViewById<Button>(R.id.search_bar)
        postingSearchBtn.setOnClickListener{
            Log.d(TAG,"클릭됨")
            val intent = Intent(mContext,PostingSearchActivity::class.java)
            mContext.startActivity(intent)
        }

        val priority = view.findViewById<Button>(R.id.spinner_priority)
        priority.setOnClickListener {
            val bottomSheetPriority = BottomSheetPriority{
                when (it) {
                    //확인해보기
                    0 -> {
                        priority.setText("최신순")
                        GetPostingsOfBoard("pdate")
                    }
                    1 -> {
                        priority.setText("댓글순")
                        GetPostingsOfBoard("ccnt")
                    }

                    2 -> {
                        priority.setText("공감순")
                        GetPostingsOfBoard("hcnt")
                    }

                }
            }
            bottomSheetPriority.show((context as MainActivity).supportFragmentManager,bottomSheetPriority.tag)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

//게시글 작성시 adapter을 refresh하는 함수
fun refreshAdapter(result : Posting){
    postings.add(0,result)
    PostingAdapter(postings)
    Log.d(TAG,"호출됨")
    Log.d(TAG, postings.get(0).toString())
    Postingadapter.notifyDataSetChanged()
}

//뒤로 가기시 바로 갱신을 해주고 싶은데 좀 이상함
fun positionAdapter(position : Int){
    Postingadapter.notifyItemChanged(position)
    postings.get(position)
    Postingadapter.notifyItemChanged(Postingadapter.itemCount, postings.size)
}

//삭제하고 refresh
fun deleteAdapter(position : Int){
    Postingadapter.notifyItemRemoved(position)
    postings.removeAt(position)
    Postingadapter.notifyItemChanged(Postingadapter.itemCount, postings.size)
}


//게시글 정렬함수
fun GetPostingsOfBoard(attribute : String)
{
    //게시글 초기화
    postings.clear()
    RetrofitBuilder.api.getBoardPosting(attribute).enqueue(object : Callback<List<Posting>> {
        override fun onResponse(call: Call<List<Posting>>, response: Response<List<Posting>>) {
            if (response.isSuccessful) {
                postings= response.body() as ArrayList<Posting>
                //결과를 postings에 넣기
                Log.d(TAG,"정렬 함수로 들어옴")
                Postingadapter= PostingAdapter(postings)
                recyclerView.adapter= Postingadapter//adapter 선언

            } else {
                Log.d(TAG, "onResponse 후 실패 에러: ")
                // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
            }
        }
        override fun onFailure(call: Call<List<Posting>>, t: Throwable) {
            Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
        }
    })
}


