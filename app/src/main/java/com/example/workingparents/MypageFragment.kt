package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text

private lateinit var mContext: Activity

class MypageFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            mContext=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        var view =inflater.inflate(R.layout.fragment_mypage, container, false)

        val couplePageBtn = view.findViewById<LinearLayout>(R.id.couplePage)
        val childPageBtn = view.findViewById<LinearLayout>(R.id.childPage)


        //이상하게 fragment에서 textview띄우려면 선언받아서 다시 재정의해줘야한다.. 왜? 다른 방법들로 해결하려 했지만
        //되지않았음///
        var user_name=view.findViewById<TextView>(R.id.user_name)
        var user_sex=view.findViewById<TextView>(R.id.user_sex)
        var user_id=view.findViewById<TextView>(R.id.user_id)
        var connect_tv=view.findViewById<TextView>(R.id.connect_tv)
        var spouse_state = view.findViewById<TextView>(R.id.spouse_state)
        var spouse_name = view.findViewById<TextView>(R.id.spouse_name)
        var child_tv = view.findViewById<TextView>(R.id.child_tv)
        var child_state = view.findViewById<TextView>(R.id.child_state)
        var child_name = view.findViewById<TextView>(R.id.child_name)


        //예시
        user_name.setText("이지현")
        user_sex.setText("워킹맘")
        user_id.setText("workingparents1234")
        connect_tv.setText("부부연결")
        spouse_state.setText("연결중")
        spouse_name.setText("남석우")
        child_tv.setText("아이연결")
        child_state.setText("연결중")
        child_name.setText("남아영")

        couplePageBtn.setOnClickListener{
            val intent = Intent(mContext,CoupleConnectActivity::class.java)
            mContext.startActivity(intent)
        }

        childPageBtn.setOnClickListener {
            val intent = Intent(mContext, RegisterChildActivity::class.java)
            mContext.startActivity(intent)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun getInstance(): MypageFragment? {
        return MypageFragment()
    }
}