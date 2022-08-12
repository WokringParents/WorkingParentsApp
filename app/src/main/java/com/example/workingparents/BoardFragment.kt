package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.databinding.FragmentBoardBinding
import kotlinx.android.synthetic.main.fragment_board.*

private val TAG="Board"
private lateinit var mContext:Activity


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

        val sampleList = arrayListOf(
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","안녕하세요! 이번에 만촌동에 새로 이사오게 된 유치원생맘 입니다. ㅎㅎ 다름이 아니라 제가 퇴근이 늦어져서 오후반에 애를 맡기는 것보다 집에 조부모님께서 봐주시는 게 나을 것 같은데 거동이 불편하셔서 집까지 하원을 해주실 학부모님을 찾고 있습니다! 개인 메시지 주시면 더 자세한 설명드릴게요~",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
            Posting("코딩꾸버","5분전","만촌동","등원","실험용",0,0),
        )

        var view =inflater.inflate(R.layout.fragment_board, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_posting)

        //divider
        recyclerView.addItemDecoration(MyDecoration(10, Color.parseColor("#f2f2f2")))
        recyclerView.layoutManager= LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        //리사이클러뷰 선언
        recyclerView.visibility=View.VISIBLE
        recyclerView.setHasFixedSize(true) //리사이클러뷰 성능 개선?
        recyclerView.adapter=PostingAdapter(sampleList) //adapter 선언

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

}