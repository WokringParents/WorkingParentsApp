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