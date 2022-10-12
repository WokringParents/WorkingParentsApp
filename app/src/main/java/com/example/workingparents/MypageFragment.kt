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
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_mypage.view.*

private lateinit var mContext: Activity

class MypageFragment : Fragment() {

    private val TAG="마이페이지연결"

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
        var valiConnectedCouple = false  //부부연결됐는지
        var valiConnectedChild = false  //아이연결됐는지

        if(UserData.connectedCouple()){
            //부부연결이 된 상태라면
            valiConnectedCouple= true
            view.spouse_state.text = "연결중"
            view.spouse_state.setTextColor(Color.parseColor("#ff9769"))
            view.spouse_image.visibility = View.VISIBLE
            view.spouse_name.text = UserData.spouseName

        }

        if(!valiConnectedCouple) {
            //부부연결이 안된 상태라면
            couplePageBtn.setOnClickListener {
                val intent = Intent(mContext, CoupleConnectActivity::class.java)
                mContext.startActivity(intent)

            }
        }

        if(UserData.connectedChild()) {
            //아이연결이 된 상태라면
            valiConnectedChild = true
            view.child_state.text = "연결중"
            view.child_state.setTextColor(Color.parseColor("#ff9769"))
            view.child_image.visibility = View.VISIBLE
            view.child_name.text = UserData.childName
            Log.d("아이등록",UserData.childName)
        }

        if(!valiConnectedChild) {
            //아이연결이 안 된 상태라면
            childPageBtn.setOnClickListener {
                val intent = Intent(mContext, RegisterChildActivity::class.java)
                mContext.startActivity(intent)
            }
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