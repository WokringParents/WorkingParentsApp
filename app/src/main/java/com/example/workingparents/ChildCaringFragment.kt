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
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.transitionseverywhere.extra.Scale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


const val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD=128

class ChildCaringFragment : Fragment() {


    companion object {

        var FragmentinitFlag = false

        /*UI초기화에 필요한 연도,달,오늘자정보(연도+달) 일주일날짜,요일(월:0~일:6),몇주차*/
        lateinit var year: String
        lateinit var month: String
        var dayArr: Array<String?> = arrayOfNulls(7) //D 형식으로 7일
        var weekOfMonth by Delegates.notNull<Int>()


        /*데이터 조작에 필요한 날짜 정보*/
        var lastDay by Delegates.notNull<Int>()             //이달의 마지막날은 몇 일인지
        var lastWeekOfMonth by Delegates.notNull<Int>()     //총 몇주차(마지막날의 주차)
        var dateArr: Array<String?> = arrayOfNulls(7)  //YYYY-MM-DD 형식으로 7일 모두
        var clickedDayOfWeek by Delegates.notNull<Int>()    //클릭된 요일
        var toDoList = ArrayList<SharingList>()     //일주일간 투두리스트 데이터들
        lateinit var today: String              //YYYY-MM-DD 형식

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //시스템상 다크모드 끄는거
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


        //뷰 여백부분 터치했을때 동작들
        view.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                if (event.action == MotionEvent.ACTION_MOVE) {

                    Log.d(TAG,"내가 프래그먼트의 어느곳을 터치했음")

                    if(isKeyboardShown(view.rootView)){
                        //입력하던 와중 다른 곳을 선택한 경우  --> 입력이 취소되어야한다
                        Log.d(TAG,"키보드 올라온 상태")

                        val imm: InputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(mContext.currentFocus?.windowToken, 0)

                        if(dailyAdapter.todoData.get(dailyAdapter.itemCount -1).inputMode){
                            dailyAdapter.todoData.removeLast()
                            dailyAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                        }

                        if(todayAdapter.todoData.get(todayAdapter.itemCount-1).inputMode){
                            todayAdapter.todoData.removeLast()
                            todayAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                        }
                    }
                }
                return true
            }
        })

/*
        if(!FragmentinitFlag) {
            val long_now = System.currentTimeMillis()
            val curDate = Date(long_now)
            val dateFormat = SimpleDateFormat("yyyy-M-d", Locale("ko", "KR"))
            val str_date = dateFormat.format(curDate)
            getCurrentWeek("2022-1-1")

        }
*/
        getDateInfo(null)
        setWeekLayout(view, container!!)
        setDateUI(view)
        setPlusBtn(view)

        if(UserData.connectedCouple()) { // 부부이면
            getWeekSharingList(dateArr[0]+"00:00:00",dateArr[6]+"23:59:59")
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


    fun getDateInfo(eventDate: String?) {

        val cal = Calendar.getInstance()
        val dateformat = SimpleDateFormat("yyyy-MM-dd ")
        val dayformat = SimpleDateFormat("d")


        if(eventDate!=null){
            val dateArray = eventDate.split("-").toTypedArray()
            year = dateArray[0]
            month = dateArray[1]
            cal[dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()
        }else{
            /* 바로 오늘 정보 */
            year=cal.get(Calendar.YEAR).toString()
            month=(cal.get(Calendar.MONTH)+1).toString()  //0~11로 들고와서 +1필요함
            today=dateformat.format(cal.time)


            //일:1 월:2 화:3 수:4 -- 토:7   SQL이랑 넘버링이 다르네 그지같구나
            cal.get(Calendar.DAY_OF_WEEK)

        }

        cal.firstDayOfWeek = Calendar.MONDAY   // 일주일의 첫날을 월요일로 지정한다 매우매우 중요함!!!!
        weekOfMonth= cal.get(Calendar.WEEK_OF_MONTH)
        lastDay= cal.getActualMaximum(Calendar.DATE)


       /* 해당날이 포함된 1주치 Date 정보 */

        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek // 시작일과 특정날짜의 차이를 구한다
       // 일요일일 때가 dayofmonth가 특수하게 -1로 떠서 그에 맞게 처리해줌
        if (dayOfWeek == -1) {
            cal.add(Calendar.DAY_OF_MONTH, -6)
            clickedDayOfWeek=6
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
            clickedDayOfWeek=dayOfWeek
        }


        for(i in 0..6){
            dayArr[i]=dayformat.format(cal.time)
            dateArr[i]=dateformat.format(cal.time)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }


        /* 해당 날의 n주차 정보, 해당 달의 총 주자 정보 */
        cal[year.toInt(), month.toInt() - 1]= lastDay   //캘린더 정보 마지막날로 수정 (연도, 달은 -1 필요 시스템에선 1월이 0부터 시작함, 날짜)
        lastWeekOfMonth=cal.get(Calendar.WEEK_OF_MONTH)

        /*
        Log.d(TAG, lastDay.toString())
        Log.d(TAG,"week of month:"+ cal.get(Calendar.WEEK_OF_MONTH))  //몇주차인지 알려준다 마지막일 주차보고 이달은 총 몇주차인지 알수있음
        Log.d(TAG,"day of month:"+cal.get(Calendar.DAY_OF_MONTH))     //몇일인지 알려준다
        Log.d(TAG,"day of week:" +cal.get(Calendar.DAY_OF_WEEK))      //일:1 월:2 화:3 수:4 -- 토:7   SQL이랑 넘버링이 다르네 그지같구나
        */

        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 =" +dateArr[0]+ ", 종료 날짜 = "+dateArr[6] +lastWeekOfMonth+"주차 중 "+ weekOfMonth+"주차")

    }

    fun setWeekLayout(view:View, container: ViewGroup){

        var weekLayout= view.findViewById<LinearLayout>(R.id.clickable_layout_week)
        var weekChoiceLayout= view.findViewById<LinearLayout>(R.id.visible_layout_week_choice)

        var weekTV: Array<TextView> = arrayOf(
            view.findViewById<TextView>(R.id.week1),
            view.findViewById<TextView>(R.id.week2),
            view.findViewById<TextView>(R.id.week3),
            view.findViewById<TextView>(R.id.week4),
            view.findViewById<TextView>(R.id.week5),
            view.findViewById<TextView>(R.id.week6))

        view.findViewById<TextView>(R.id.selectedWeek).setText(weekOfMonth.toString()+"주차")
        if(lastWeekOfMonth>5){
            weekTV[5].visibility=View.VISIBLE
        }else if(lastWeekOfMonth<5){
            weekTV[4].visibility=View.GONE
            weekTV[5].visibility=View.GONE
        }

        weekLayout.setOnClickListener{
/*
           val transition: Transition = Slide(Gravity.RIGHT)
           transition.duration = 400
           transition.addTarget(R.id.visible_layout_week_choice)
           if (container != null) {
               TransitionManager.beginDelayedTransition(container,transition)
           }
*/
            val set = TransitionSet().addTransition(Scale(0.7f)).addTransition(Fade())
            // set.setInterpolator(LinearOutSlowInInterpolator())
            set.setInterpolator(FastOutLinearInInterpolator())
            if (container != null) {
                TransitionManager.beginDelayedTransition(container,set)
            }
            weekChoiceLayout.visibility=View.VISIBLE
            weekLayout.visibility=View.GONE
        }

        var dateStrBuilder = StringBuilder()
        dateStrBuilder.append(year)
        dateStrBuilder.append("-")
        dateStrBuilder.append(month)
        dateStrBuilder.append("-")


        for(i : Int in 0..5){

            weekTV[i].setOnClickListener(View.OnClickListener {

                val set = TransitionSet().addTransition(Scale(0.7f)).addTransition(Fade())
                set.setInterpolator(FastOutLinearInInterpolator())
                if (container != null) {
                    TransitionManager.beginDelayedTransition(container,set)
                }
                weekChoiceLayout.visibility=View.GONE
                weekLayout.visibility=View.VISIBLE
                val str=(i+1).toString()+"주차"
                view.findViewById<TextView>(R.id.selectedWeek).setText(str)

                if(lastWeekOfMonth==i+1) {
                    //마지막 주이면 마지막날로 검색
                    getDateInfo(dateStrBuilder.toString()+ lastDay)
                }
                else {
                    //나머지 주간이면  1,8,15,22,29일 검색
                    getDateInfo(dateStrBuilder.toString()+ (1+i*7))
                }

                setDateUI(view)
            })
        }
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

            if(dateArr[i]!! < today ){
                //지난 날이라면   진회색숫자 & 연회색배경
                dayNumTV[i].setTextColor(Color.parseColor("#9e9e9e"))
                dayNumTV[i].setBackgroundResource(R.drawable.pastdatecircle)
                dayNumTV[i].elevation=0.dpToPixels(mContext)
            }
            else if(dateArr[i]!! == today){
                //오늘이면 연베이지숫자 & 메인컬러배경
                dayNumTV[i].setTextColor(Color.parseColor("#F2EDE9"))
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
                    dailyAdapter.todoData.clear()
                    todayAdapter.todoData.clear()

                    for (todo: SharingList in toDoList) {
                        if (todo.dayOfWeek == i) {
                            if(todo.daily)
                                dailyAdapter.todoData.add(todo)
                            else
                                todayAdapter.todoData.add(todo)
                        }
                    }

                    dailyAdapter.notifyDataSetChanged()
                    todayAdapter.notifyDataSetChanged()
                }
            })

        }



    }

    fun setPlusBtn(view: View){

        val fullWeekBtn= view.findViewById<ImageButton>(R.id.fullWeekBtn)
        val oneDayBtn=view.findViewById<ImageButton>(R.id.oneDayBtn)

        fullWeekBtn.setOnClickListener(View.OnClickListener {

            if(isKeyboardShown(view.rootView)){
                Log.d(TAG,"is Show")  //키보드 올라와있을 땐 아무것도 하지마라 제발
            }else{
                Log.d(TAG,"is Hide")
                if(dailyAdapter.itemCount==0 ||!dailyAdapter.todoData.get(dailyAdapter.itemCount-1).inputMode){

                    dailyAdapter.todoData.add(SharingList(UserData.couplenum, getCorrectTimestamp(),"테스트", mdo=false, fdo= false, clickedDayOfWeek , inputMode = true, daily=true))
                    dailyAdapter.notifyItemInserted(dailyAdapter.itemCount)

                }else{
                    dailyAdapter.todoData.removeLast()
                    dailyAdapter.notifyItemRemoved(dailyAdapter.itemCount)
                }
            }
        })

        oneDayBtn.setOnClickListener(View.OnClickListener {

            if (isKeyboardShown(view.rootView)) {
                Log.d(TAG, "is Show")  //키보드 올라와있을 땐 아무것도 하지마라 제발
            } else {

                Log.d(TAG, "is Hide")

                if (todayAdapter.itemCount == 0 || !todayAdapter.todoData.get(todayAdapter.itemCount - 1).inputMode) {
                    todayAdapter.todoData.add(SharingList(UserData.couplenum,getCorrectTimestamp(), "테스트", mdo = false, fdo = false, clickedDayOfWeek, inputMode = true, daily = false))
                    todayAdapter.notifyItemInserted(todayAdapter.itemCount)
                } else {
                    todayAdapter.todoData.removeLast()
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

    private fun getWeekSharingList(startDt: String, endDt: String) {

        RetrofitBuilder.api.getSharingList(UserData.couplenum,startDt,endDt).enqueue(object:Callback<List<SharingList>>{

            override fun onResponse(call: Call<List<SharingList>>, response: Response<List<SharingList>>) {

                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: List<SharingList>? = response.body()

                    toDoList.clear()
                    toDoList = result as ArrayList<SharingList>

                    if (toDoList.size>0) {
                        for(todo: SharingList in toDoList){
                            val strDate=todo.sdate.toString().substring(0,11)
                            Log.d(TAG,strDate +" "+ today + (strDate==today) + todo.daily)

                            if(todo.daily && strDate==today){
                                Log.d(TAG,"같은날이라 추가했는데")
                                dailyAdapter.todoData.add(todo)
                            }
                            else if(!todo.daily &&strDate==today){
                                todayAdapter.todoData.add(todo)
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