package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class CafeteriaFragment : Fragment() {

    var TAG="Cafeteria"
    lateinit var mContext: Activity

    companion object {

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is TeacherMainActivity){
            mContext=context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = inflater.inflate(R.layout.fragment_teacher_cafeteria, container, false)

        val writen_btn = view.findViewById<ImageButton>(R.id.writecafeteria_btn)

        writen_btn.setOnClickListener{
            val intent = Intent(mContext, WriteCafeteriaActivity::class.java)
            mContext.startActivity(intent)
        }
        return view;

    }

}