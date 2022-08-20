package com.example.workingparents

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_child_caring.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates
import java.util.Calendar


const val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD=128

class ChildCaringFragment : Fragment() {


    companion object {

        var FragmentinitFlag = false

        /*UI초기화에 필요한 현재의 연도,달,일주일날짜,요일(월:0~일:6)*/
        lateinit var year: String
        lateinit var month: String
        lateinit var day: String
        var todayOfWeek by Delegates.notNull<Int>()
        var dayArr: Array<String?> = arrayOfNulls<String>(7) //D 형식으로 7일


        /*데이터 조작에 필요한 날짜 정보*/
        var dateArr: Array<String?> = arrayOfNulls(7)  //YYYY-MM-DD 형식으로 7일 모두
        var clickedDayOfWeek by Delegates.notNull<Int>()    //클릭된 요일
        var toDoList = ArrayList<SharingList>()     //일주일간 투두리스트 데이터들
    }


    private lateinit var mContext : Activity
    private lateinit var dailyAdapter: SharingListAdapter
    private lateinit var todayAdapter: SharingListAdapter
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

        var dailyRecyclerView = view.findViewById<RecyclerView>(R.id.dailyListRecyclerView)
        var todayRecyclerView= view.findViewById<RecyclerView>(R.id.todayListRecyclerView)

        var weekSpinner= view.findViewById<Spinner>(R.id.weekSpinner)

        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(mContext, R.array.spinner_entries, R.layout.spinner_item)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        weekSpinner.adapter=spinnerAdapter;
        weekSpinner.setSelection(2)
        weekSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        Log.d(TAG, "1주차 선택됨")
                    }
                    1 -> {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



        view.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                if (event.action == MotionEvent.ACTION_MOVE) {

                    Log.d(TAG,"내가 프래그먼트의 어느곳을 터치했음")

                    if(isKeyboardShown(view.rootView)){
                        //입력하던 와중 다른 곳을 선택한 경우  --> 입력이 취소되어야한다
                        Log.d(TAG,"키보드 올라온 상태")

                        val imm: InputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(mContext.currentFocus?.windowToken, 0)

                        if(dailyAdapter.datas.get(dailyAdapter.itemCount -1).inputMode){
                            dailyAdapter.datas.removeLast()
                            dailyAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                        }

                        if(todayAdapter.datas.get(todayAdapter.itemCount-1).inputMode){
                            todayAdapter.datas.removeLast()
                            todayAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                        }
                    }
                }
                return true
            }
        })


        if(!FragmentinitFlag) {
            val long_now = System.currentTimeMillis()
            val curDate = Date(long_now)
            val dateFormat = SimpleDateFormat("yyyy-M-d", Locale("ko", "KR"))
            val str_date = dateFormat.format(curDate)
            getCurrentWeek(str_date)
        }

        setDateUI(view)
        if(UserData.connectedCouple()) { // 부부이면
            getCurWeekSharingList(dateArr[0]+"00:00:00",dateArr[6]+"23:59:59")

        }else{
            //부부연결 해달라는 그림을 보여줌
        }
        initRecycler(dailyRecyclerView, todayRecyclerView)

        return view
    }



    private fun initRecycler(dailyRecyclerView: RecyclerView, todayRecyclerView: RecyclerView){


        // recyclerView.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL) )

        FragmentinitFlag= true

        val dailyManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        val todayManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        dailyRecyclerView.layoutManager = dailyManager // LayoutManager 등록
        dailyRecyclerView.visibility=View.VISIBLE
        dailyAdapter= SharingListAdapter(mContext)
        dailyRecyclerView.adapter=dailyAdapter

        Log.d(TAG,"initRecyclerView 함수속 dailyList초기화됨? ")

        todayRecyclerView.layoutManager=todayManager
        todayRecyclerView.visibility=View.VISIBLE
        todayAdapter= SharingListAdapter(mContext)
        todayRecyclerView.adapter=todayAdapter


        Log.d(TAG,"initRecyclerView 함수속 todayList초기화됨? ")
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
            clickedDayOfWeek=6
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
            todayOfWeek=dayOfWeek
            clickedDayOfWeek=dayOfWeek
        }

        val dateformat = SimpleDateFormat("yyyy-MM-dd ")
        val dayformat = SimpleDateFormat("d")


        for(i in 0..6){
            dayArr[i]=dayformat.format(cal.time)
            dateArr[i]=dateformat.format(cal.time)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        /*

           var dayVal = dayformat.format(cal.time).toInt()

           for (i in 0 until dayArr.count()){
               dayArr[i]=dayVal.toString()
               dayVal++
           }
         */

        /*
           // 해당 주차의 첫째 날짜
           startDt = dateformat.format(cal.time)
           startDt+="00:00:00"
            // 해당 주차의 마지막 날짜 지정
           cal.add(Calendar.DAY_OF_MONTH, 6)

           // 해당 주차의 마지막 날짜
           endDt = dateformat.format(cal.time)
           endDt+="23:59:59"
   */

        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 =" +dateArr[0]+ ", 종료 날짜 = "+dateArr[6])



    }

    fun Int.dpToPixels(context: Context):Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics)



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


        view.findViewById<TextView>(R.id.yearTextView).text=year
        view.findViewById<TextView>(R.id.monthTextView).text=month

        for(i: Int in 0..6){
            dayNumTV[i].text= dayArr[i]

            if(i< todayOfWeek){
                //지난 날이라면   진회색숫자 & 연회색배경
                dayNumTV[i].setTextColor(Color.parseColor("#9e9e9e"))
                dayNumTV[i].setBackgroundResource(R.drawable.pastdatecircle)
                dayNumTV[i].elevation=0.dpToPixels(mContext)
            }
            else if(i==todayOfWeek){

                //오늘 요일과 동일하다면   연베이지숫자 & 메인컬러배경
                dayNumTV[i].setTextColor(Color.parseColor("#eeeeee"))
                dayNumTV[i].setBackgroundResource(R.drawable.todaydatecircle)
                dayNumTV[i].elevation=0.dpToPixels(mContext)

            }else{

                //다가올 날들이라면
                dayNumTV[i].setTextColor(Color.parseColor("#000000"))
                dayNumTV[i].setBackgroundResource(R.drawable.comingdatecircle)
                dayNumTV[i].elevation=6.dpToPixels(mContext)
            }

        }

        for(i : Int in 0..6){

            dayNumTV[i].setOnClickListener(View.OnClickListener {

                for(j in 0..6) {

                    if (j == i) {
                        //클릭된 한글 요일 색깔 바꾸고 볼드
                        dayHangulTV[j].setTypeface(dayNumTV[i].typeface, Typeface.BOLD)
                        dayHangulTV[j].setTextColor(Color.parseColor("#FF9769"))  //Main컬러로
                    } else {
                        //나머지 색은 블랙으로 돌려주기
                        dayHangulTV[j].setTypeface(dayNumTV[i].typeface, Typeface.NORMAL)
                        dayHangulTV[j].setTextColor(Color.parseColor("#000000"))
                    }

                }


                clickedDayOfWeek=i

                if(isKeyboardShown(view.rootView)){
                    Log.d(TAG,"is Show")  //키보드 올라와있을 땐 뭐 하지마라 제발
                }else {

                    Log.d(TAG, "$i 요일 클릭됨")
                    dailyAdapter.datas.clear()
                    todayAdapter.datas.clear()

                    for (todo: SharingList in toDoList) {
                        if (todo.dayOfWeek == i) {
                            if(todo.daily)
                                dailyAdapter.datas.add(todo)
                            else
                                todayAdapter.datas.add(todo)
                        }
                    }

                    dailyAdapter.notifyDataSetChanged()
                    todayAdapter.notifyDataSetChanged()
                }
            })

        }


        val fullWeekBtn= view.findViewById<ImageButton>(R.id.fullWeekBtn)
        val oneDayBtn=view.findViewById<ImageButton>(R.id.oneDayBtn)

        fullWeekBtn.setOnClickListener(View.OnClickListener {

            if(isKeyboardShown(view.rootView)){
                Log.d(TAG,"is Show")  //키보드 올라와있을 땐 아무것도 하지마라 제발
            }else{
                Log.d(TAG,"is Hide")
                if(dailyAdapter.itemCount==0 ||!dailyAdapter.datas.get(dailyAdapter.itemCount-1).inputMode){

                    dailyAdapter.datas.add(SharingList(UserData.couplenum, getCorrectTimestamp(),"테스트", mdo=false, fdo= false, clickedDayOfWeek , inputMode = true, daily=true))
                    dailyAdapter.notifyItemInserted(dailyAdapter.itemCount)

                }else{
                    dailyAdapter.datas.removeLast()
                    dailyAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                }
            }
        })

        oneDayBtn.setOnClickListener(View.OnClickListener {

            if (isKeyboardShown(view.rootView)) {
                Log.d(TAG, "is Show")  //키보드 올라와있을 땐 아무것도 하지마라 제발
            } else {

                Log.d(TAG, "is Hide")

                if (todayAdapter.itemCount == 0 || !todayAdapter.datas.get(todayAdapter.itemCount - 1).inputMode) {
                    todayAdapter.datas.add(SharingList(UserData.couplenum,getCorrectTimestamp(), "테스트", mdo = false, fdo = false, clickedDayOfWeek, inputMode = true, daily = false))
                    todayAdapter.notifyItemInserted(todayAdapter.itemCount)
                } else {
                    todayAdapter.datas.removeLast()
                    todayAdapter.notifyItemRemoved(todayAdapter.itemCount)
                }
            }
        })
    }

    fun getCorrectTimestamp(): Timestamp{

        val date=Date(System.currentTimeMillis())
        val onlyTimeFormat = SimpleDateFormat("kk:mm:ss",Locale.KOREA)
        val onlyTime= onlyTimeFormat.format(date)

        var strBuilder = StringBuilder(dateArr[clickedDayOfWeek])
        strBuilder.append(onlyTime)

        return Timestamp.valueOf(strBuilder.toString())

    }

    private fun getCurWeekSharingList(startDt: String, endDt: String) {

        RetrofitBuilder.api.getSharingList(UserData.couplenum,startDt,endDt).enqueue(object:Callback<List<SharingList>>{

            override fun onResponse(call: Call<List<SharingList>>, response: Response<List<SharingList>>) {

                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: List<SharingList>? = response.body()

                    toDoList.clear()
                    toDoList = result as ArrayList<SharingList>

                    if (toDoList.size>0) {
                        for(todo: SharingList in toDoList){
                            Log.d(TAG,todo.sdate.toString())
                            if(todo.daily && todo.dayOfWeek== todayOfWeek){
                                dailyAdapter.datas.add(todo)
                            }
                            else if(!todo.daily && todo.dayOfWeek==todayOfWeek){
                                todayAdapter.datas.add(todo)
                            }
                        }
                        dailyAdapter.notifyDataSetChanged()
                        todayAdapter.notifyDataSetChanged()
                    }
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

    //키보드 올라와있는 상태인지 아닌지 체크해주는 함수
    private fun isKeyboardShown(rootView: View):Boolean{

        val r= Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm: DisplayMetrics=rootView.resources.displayMetrics
        val heightDiff: Int= rootView.bottom-r.bottom

        return heightDiff> SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD*dm.density
    }




}