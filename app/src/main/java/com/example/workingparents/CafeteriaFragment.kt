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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class CafeteriaFragment : Fragment() {

    var TAG="Cafeteria"
    lateinit var mContext: Activity

    companion object {

        lateinit var cafeterias: ArrayList<Cafeteria>
        lateinit var cafeOuterRecyclerView:RecyclerView
        lateinit var cafeOuterAdapter:  CafeteriaOuterAdapter
        var cafeteriaByDates: MutableList<CafeteriaByDate> = mutableListOf()
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
        cafeOuterRecyclerView= view.findViewById(R.id.cafeteria_outer_content_recyclerview)

        writen_btn.setOnClickListener{
            val intent = Intent(mContext, WriteCafeteriaActivity::class.java)
            mContext.startActivity(intent)
        }

        cafeOuterRecyclerView.addItemDecoration(MyDecoration(4, Color.parseColor("#f2f2f2")))
        cafeOuterRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        cafeOuterRecyclerView.visibility = View.VISIBLE
        cafeOuterRecyclerView.setHasFixedSize(true)


        RetrofitBuilder.api.getCafeterias(1).enqueue(object: Callback<List<Cafeteria>>{
            override fun onResponse(call: Call<List<Cafeteria>>, response: Response<List<Cafeteria>>) {
              if(response.isSuccessful){
                  cafeterias= response.body() as ArrayList<Cafeteria>
                  Log.d(TAG, cafeterias.toString())

                  if(cafeterias.size>0) {

                      cafeteriaByDates.clear()

                      var prevCdate = "prevDate"
                      for (cafe: Cafeteria in cafeterias) {
                        //새로운 날짜이면 추가
                          if(cafe.cdate!=prevCdate){
                              cafeteriaByDates.add(CafeteriaByDate(cafe.cdate))
                          }
                          cafeteriaByDates.last().images.put(cafe.ctype,cafe.image)
                          cafeteriaByDates.last().contents.put(cafe.ctype,cafe.content)
                          prevCdate=cafe.cdate
                      }
             //         cafeOuterAdapter= CafeteriaOuterAdapter(mContext, cafeteriaByDates)
                      Log.d(TAG, cafeteriaByDates.last().cdate + cafeteriaByDates.last().contents.toString())
                   }

                  cafeOuterAdapter= CafeteriaOuterAdapter(mContext, cafeteriaByDates)
                  cafeOuterRecyclerView.adapter= cafeOuterAdapter


              }else{
                  Log.d(TAG, "onResponse 실패: getCafeterias")
              }
            }

            override fun onFailure(call: Call<List<Cafeteria>>, t: Throwable) {
                Log.d(TAG, "onFailure getCafeterias : " + t.message.toString())

            }

        })

        return view;

    }

}