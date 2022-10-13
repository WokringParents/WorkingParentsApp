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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_mypage.view.*

private lateinit var mContext: Activity

class MypageFragment : Fragment() {

    private val TAG="Mypage"

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

        val profile_picture = view.findViewById<ImageView>(R.id.profile_picture)
        val user_name = view.findViewById<TextView>(R.id.user_name)
        val user_sex = view.findViewById<TextView>(R.id.user_sex)
        val user_id = view.findViewById<TextView>(R.id.user_id)

        val couplePageBtn = view.findViewById<LinearLayout>(R.id.couplePage)
        val childPageBtn = view.findViewById<LinearLayout>(R.id.childPage)
        var valiConnectedCouple = false  //부부연결됐는지
        var valiConnectedChild = false  //아이연결됐는지

        Log.d(TAG, "마이페이지 리프래쉬")


        if(UserData.sex=="M") {
            //사용자가 남자일 때
            view.profile_picture.setImageResource(R.drawable.dad_left)
            view.user_name.text = UserData.name
            view.user_sex.text = "워킹대디"
            view.user_id.text = UserData.id

        }else{
            //사용자가 여자일 때
            view.user_name.text = UserData.name
            view.user_id.text = UserData.id
        }

        if(UserData.connectedCouple()){
            //부부연결이 된 상태라면
            Log.d(TAG, "부부 연결 완료")
            valiConnectedCouple= true
            view.spouse_state.text = "연결중"
            view.spouse_state.setTextColor(Color.parseColor("#ff9769"))
            view.spouse_name.text = UserData.spouseName
            if(UserData.sex=="M") {
                view.spouse_image.setImageResource(R.drawable.mom_left)
            }
                view.spouse_image.visibility = View.VISIBLE

        }

        if(!valiConnectedCouple) {
            //부부연결이 안된 상태라면
            Log.d(TAG, "부부연결 안됨")
            couplePageBtn.setOnClickListener {
                val intent = Intent(mContext, CoupleConnectActivity::class.java)
                mContext.startActivity(intent)

            }
        }

        if(UserData.connectedChild()) {
            //아이연결이 된 상태라면
            Log.d(TAG, "아이 연결 완료")
            valiConnectedChild = true
            view.child_state.text = "연결중"
            view.child_state.setTextColor(Color.parseColor("#ff9769"))
            view.child_image.visibility = View.VISIBLE
            view.child_name.text = UserData.childName
            Log.d("아이등록",UserData.childName)
        }

        if(!valiConnectedChild) {
            //아이연결이 안 된 상태라면
            Log.d(TAG, "아이 연결 안됨")
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