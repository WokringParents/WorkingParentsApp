package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        var valiNotconnectedCouple = false  //부부연결됐는지
        var valiNotconnectedChild = false  //아이연결됐는지

        if(!UserData.connectedCouple()){
            //부부연결이 안된 상태라면
            valiNotconnectedCouple= true
            view.spouse_state.text ="연결안됨"
            view.spouse_image.visibility = View.GONE
            view.spouse_name.text="상대방과 함께해요"

        }

        if(valiNotconnectedCouple) {
            //부부연결이 안된 상태라면
            couplePageBtn.setOnClickListener {
                val intent = Intent(mContext, CoupleConnectActivity::class.java)
                mContext.startActivity(intent)
            }
        }

        if(!UserData.connectedChild())
            //아이연결이 안된 상태라면
            valiNotconnectedChild= true
        view.child_state.text ="연결안됨"
        view.child_image.visibility = View.GONE
        view.child_name.text="상대방과 함께해요"

        if(!valiNotconnectedChild) {
            //아이연결이 안된 상태라면
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