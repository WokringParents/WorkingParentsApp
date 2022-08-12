package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_child_caring.*
import java.text.SimpleDateFormat
import java.util.*
import java.time.DayOfWeek;

class ChildCaringFragment : Fragment() {

    private lateinit var mContext : Activity
    private lateinit var sharingListAdapter: SharingListAdapter
    val datas = mutableListOf<SharingList>()
    private var TAG ="ChildCaring"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view =inflater.inflate(R.layout.fragment_child_caring, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.sharingListRecyclerView)

        val long_now= System.currentTimeMillis()
        val curDate= Date(long_now)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val str_date = dateFormat.format(curDate)
        week(str_date)

      //val t_dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "KR"))

      //  week("2022-8-1")
        initRecycler(recyclerView)

        return view



    }

    companion object {

    }

    private fun initRecycler(recyclerView: RecyclerView){


        // recyclerView.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL) )


        val manager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager // LayoutManager 등록
        recyclerView.visibility=View.VISIBLE
        sharingListAdapter= SharingListAdapter(mContext)
        recyclerView.adapter=sharingListAdapter

        datas.apply{
            add(SharingList(content="인서목욕시키기2", mdo=0, fdo=0))
            add(SharingList(content="인서목욕시키기3", mdo=1, fdo=0))
            add(SharingList(content="인서목욕시키기4", mdo=0, fdo=1))
            add(SharingList(content="인서목욕시키기5", mdo=1, fdo=1))

            sharingListAdapter.datas=datas
            sharingListAdapter.notifyDataSetChanged()
        }


    }


    fun week(eventDate: String) {
        val dateArray = eventDate.split("-").toTypedArray()

        val cal = Calendar.getInstance()
        cal[dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()

        // 일주일의 첫날을 월요일로 지정한다
        cal.firstDayOfWeek = Calendar.MONDAY

        // 시작일과 특정날짜의 차이를 구한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek

        // 해당 주차의 첫째날을 지정한다
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)

        val sf = SimpleDateFormat("yyyy-MM-dd")

        // 해당 주차의 첫째 날짜
        val startDt = sf.format(cal.time)

        // 해당 주차의 마지막 날짜 지정
        cal.add(Calendar.DAY_OF_MONTH, 6)

        // 해당 주차의 마지막 날짜
        val endDt = sf.format(cal.time)
        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 = [$startDt], 종료 날짜 = [$endDt]")
    }
}