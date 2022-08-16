package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
//import java.util.Calendar.getInstance
import kotlin.properties.Delegates

class ChildCaringFragment : Fragment() {

    private lateinit var mContext : Activity
    private lateinit var sharingListAdapter: SharingListAdapter
    private var toDoList = ArrayList<SharingList>()

    /*UI초기화에 필요한 현재의 연도,달,일주일날짜,요일(월:0~일:6)*/
    lateinit var year: String
    lateinit var month: String
    lateinit var day: String
    lateinit var startDt: String
    lateinit var endDt: String
    var todayOfWeek by Delegates.notNull<Int>()
    var dayArr: Array<String?> = arrayOfNulls<String>(7)

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

        val dateFormat = SimpleDateFormat("yyyy-M-d", Locale("ko", "KR"))
        val str_date = dateFormat.format(curDate)

        val calIconBtn = view?.findViewById<ImageButton>(R.id.calIcon)

        if (calIconBtn != null) {
            calIconBtn.setOnClickListener{
                Log.d(TAG,"클릭됨")
                val intent = Intent(mContext,CalendarActivity::class.java)
                mContext.startActivity(intent)
            }
        }
        return view


        getCurrentWeek(str_date)
        setDateUI(view)

        if(UserData.connectedCouple()) {
            getCurWeekSharingList(startDt!!, endDt!!)
        }

        //val t_dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "KR"))

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

        /*   datas.apply{
               add(SharingList(content="인서목욕시키기2", mdo=0, fdo=0))
               add(SharingList(content="인서목욕시키기3", mdo=1, fdo=0))
               add(SharingList(content="인서목욕시키기4", mdo=0, fdo=1))
               add(SharingList(content="인서목욕시키기5", mdo=1, fdo=1))
           }
       */
        Log.d(TAG,"initRecyclerView 함수속 toDoList초기화됨?"+ toDoList.size)

        // sharingListAdapter.datas=toDoList


    }


    fun getCurrentWeek(eventDate: String) {
        val dateArray = eventDate.split("-").toTypedArray()
        //연도-월-일
        year = dateArray[0]
        month = dateArray[1]
        day=dateArray[2]

        val cal = Calendar.getInstance()
        cal[dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()
        cal.firstDayOfWeek = Calendar.MONDAY   // 일주일의 첫날을 월요일로 지정한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek // 시작일과 특정날짜의 차이를 구한다

        // 해당 주차의 첫째날을 지정한다  // 일요일일 때가 dayofmonth가 특수하게 -1로 떠서 그에 맞게 처리해줌
        if (dayOfWeek == -1) {
            cal.add(Calendar.DAY_OF_MONTH, -6)
            todayOfWeek=6
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
            todayOfWeek=dayOfWeek
        }

        val dateformat = SimpleDateFormat("yyyy-MM-dd ")
        val dayformat = SimpleDateFormat("d")

        var dayVal = dayformat.format(cal.time).toInt()

        for (i in 0 until dayArr.count()){
            dayArr[i]=dayVal.toString()
            dayVal++
        }

        // 해당 주차의 첫째 날짜
        startDt = dateformat.format(cal.time)
        startDt+="00:00:00"
        // 해당 주차의 마지막 날짜 지정
        cal.add(Calendar.DAY_OF_MONTH, 6)

        // 해당 주차의 마지막 날짜
        endDt = dateformat.format(cal.time)
        endDt+="23:59:59"

        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 = [$startDt], 종료 날짜 = [$endDt]")


    }


    fun setDateUI(view: View){

        //날짜 dd
        val dayNumTV :Array<TextView> = arrayOf(
            view.findViewById<TextView>(R.id.monDateTV),
            view.findViewById<TextView>(R.id.tueDateTV),
            view.findViewById<TextView>(R.id.wenDateTV),
            view.findViewById<TextView>(R.id.thuDateTV),
            view.findViewById<TextView>(R.id.friDateTV),
            view.findViewById<TextView>(R.id.satDateTV),
            view.findViewById<TextView>(R.id.sunDateTV)
        )

        //월,화,수,,한글
        val dayHangulTV :Array<TextView> = arrayOf(
            view.findViewById<TextView>(R.id.monTextView),
            view.findViewById<TextView>(R.id.tueTextView),
            view.findViewById<TextView>(R.id.wenTextView),
            view.findViewById<TextView>(R.id.thuTextView),
            view.findViewById<TextView>(R.id.friTextView),
            view.findViewById<TextView>(R.id.satTextView),
            view.findViewById<TextView>(R.id.sunTextView),
        )

        //월화수,,이미지버튼
        val dayImgBtn: Array<ImageButton> = arrayOf(
            view.findViewById<ImageButton>(R.id.monBtn),
            view.findViewById<ImageButton>(R.id.tueBtn),
            view.findViewById<ImageButton>(R.id.wenBtn),
            view.findViewById<ImageButton>(R.id.thuBtn),
            view.findViewById<ImageButton>(R.id.friBtn),
            view.findViewById<ImageButton>(R.id.satBtn),
            view.findViewById<ImageButton>(R.id.sunBtn)
        )

        view.findViewById<TextView>(R.id.yearTextView).text=year
        view.findViewById<TextView>(R.id.monthTextView).text=month

        for(i: Int in 0..6){
            dayNumTV[i].text= dayArr[i]
            if(i==todayOfWeek){
                //일단 테스트한다고 오늘이면 글자두껍고 색깔블랙으로 설정함 언제쯤 디자인 넘어올까 ㅜㅡㅜ
                dayNumTV[i].setTypeface(dayNumTV[i].typeface, Typeface.BOLD)
                dayNumTV[i].setTextColor(Color.BLACK)
                dayHangulTV[i].setTypeface(dayHangulTV[i].typeface, Typeface.BOLD)
                dayHangulTV[i].setTextColor(Color.BLACK)
                dayImgBtn[i].setImageResource(R.drawable.fullheart)
            }

        }

    }


    private fun getCurWeekSharingList(startDt: String, endDt: String) {

        RetrofitBuilder.api.getSharingList(UserData.couplenum,startDt,endDt).enqueue(object:Callback<List<SharingList>>{

            override fun onResponse(call: Call<List<SharingList>>, response: Response<List<SharingList>>) {

                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: List<SharingList>? = response.body()
                    toDoList.clear()
                    toDoList= result as ArrayList<SharingList>

                    for(todo: SharingList in toDoList){
                        if(todo.dayOfWeek==todayOfWeek){
                            Log.d(TAG,"datas에 추가함")
                            sharingListAdapter.datas.add(todo)
                        }
                    }
                    sharingListAdapter.notifyDataSetChanged()

                    Log.d(TAG, "onResponse: 공유리스트 불러오기 성공: "+result.size)

                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d(TAG, "onResponse: 공유리스트 불러오기 실패")
                }


            }

            override fun onFailure(call: Call<List<SharingList>>, t: Throwable) {
                Log.d(TAG, "onFailure 공유리스트 불러오기 실패에러: " + t.message.toString())
            }


        })

    }







}