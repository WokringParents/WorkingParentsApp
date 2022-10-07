package com.example.workingparents.Notice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.workingparents.R
import com.example.workingparents.TeacherMainActivity


private lateinit var mContext: Activity

class NoticeFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is TeacherMainActivity){
            mContext=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        var view =inflater.inflate(R.layout.fragment_notice, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)











    }


    fun getInstance(): NoticeFragment? {
        return NoticeFragment()
    }
}