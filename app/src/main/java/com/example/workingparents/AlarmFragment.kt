package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_goback.*


class AlarmFragment : Fragment() {
    private lateinit var mContext: Activity

    private val TAG="Alram"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            mContext=context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_alarm, container, false)

        val recyclerAlram = view.findViewById<RecyclerView>(R.id.recyclerAlram)

        val list = ArrayList<AlramData>()


        list.add(AlramData("제목1","내용1"))
        list.add(AlramData("제목2","내용2"))
        list.add(AlramData("제목3","내용3"))

        val adapter = AlramAdapter(list)

        recyclerAlram.addItemDecoration(MyDecoration(2, Color.parseColor("#f2f2f2")))
        recyclerAlram.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recyclerAlram.adapter = adapter


        return view

    }

}