package com.example.workingparents


import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.LineBackgroundSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS
import kotlinx.android.synthetic.main.activity_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.*

class CalendarActivity : AppCompatActivity() {
    companion object {
        var hashMap= HashMap<CalendarDay,String>()
        var couplenum:Int =1
        val sex:String = "M"
    }

    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var contextMain: Context = this@CalendarActivity

        lateinit var calendar: MaterialCalendarView
        calendar = findViewById(R.id.calendar)

/*
val sundayDecorator = SundayDecorator()
val saturdayDecorator = SaturdayDecorator()

val todayDecorator = TodayDecorator()


var startTimeCalendar = CalendarData.getInstance()
var endTimeCalendar = CalendarData.getInstance()

val currentYear = startTimeCalendar.get(CalendarData.YEAR)
val currentMonth = startTimeCalendar.get(CalendarData.MONTH)
val currentDate = startTimeCalendar.get(CalendarData.DATE)

val stCalendarDay = CalendarDay.from(currentYear, currentMonth, currentDate)
val enCalendarDay = CalendarDay.from(endTimeCalendar.get(CalendarData.YEAR), endTimeCalendar.get(CalendarData.MONTH), endTimeCalendar.get(CalendarData.DATE))

val minMaxDecorator = MinMaxDecorator(stCalendarDay, enCalendarDay)

calendar.addDecorators(minMaxDecorator, sundayDecorator, saturdayDecorator, todayDecorator)
// calendar.setDateTextAppearance(R.style.CustomDateTextAppearance)
// calendar.setWeekDayTextAppearance(R.style.CustomWeekDayAppearance)
// calendar.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
*/

val CoupleColor  =  intArrayOf(
  //  Color.rgb(255, 184, 203),
  //  Color.rgb(155, 205, 255)
    Color.rgb(255, 168, 177) ,
    Color.rgb(158, 193, 255)
)

val FamaleColor  =  intArrayOf(
   // Color.rgb(255, 184, 203)
    Color.rgb(255, 168, 177)
)

val MaleColor  =  intArrayOf(
    //Color.rgb(155, 205, 255)
    Color.rgb(158, 193, 255)
)


val list = ArrayList<CalendarRecyclerData>()
//Get할 때 CalendarData형식으로 넣어주는 리스트




RetrofitBuilder.api.getCalendar(couplenum).enqueue(object: Callback<List<CalendarData>> {

    override fun onResponse(call: Call<List<CalendarData>>, response: Response<List<CalendarData>>) {
        if(response.isSuccessful){

            var result: List<CalendarData>? = response.body()
            // 정상적으로 통신이 성공된 경우
            Log.d("Retrofit", "onResponse: 캘린더성공"+result?.toString())

            if (result != null) {

                for(i in result){
                    //Get으로 가져온 result


                    var splitdate = i.cdate.split(" ")
                    //result의 날짜를 Calendar 형식이 되도록 문자열을 잘라줌

                    Log.d("Calendar성공","splitdate:"+splitdate.get(0))

                    var CalendarFromDate: List<String> = splitdate.get(0).toString().split("-")
                    //자른 문자열을 다시 잘라줌

                    if(hashMap.containsKey(CalendarDay.from(CalendarFromDate.get(0).toInt(),(CalendarFromDate.get(1).toInt()-1), CalendarFromDate.get(2).toInt()))) {
                        //기존에 이미 같은 키값(날짜)이 있다면
                        Log.d("HashMap","여기들어가긴함?")
                        if(hashMap.get(CalendarDay.from(CalendarFromDate.get(0).toInt(),(CalendarFromDate.get(1).toInt()-1), CalendarFromDate.get(2).toInt())) == i.csex){
                            //근데 같은 성별이라면
                            hashMap[CalendarDay.from(
                                CalendarFromDate.get(0).toInt(),
                                (CalendarFromDate.get(1).toInt() - 1),
                                CalendarFromDate.get(2).toInt()
                            )] = i.csex
                            Log.d("HashMap","여기들어가긴함?2")
                        }else{
                            //아니라면
                            hashMap[CalendarDay.from(
                                CalendarFromDate.get(0).toInt(),
                                (CalendarFromDate.get(1).toInt() - 1),
                                CalendarFromDate.get(2).toInt()
                            )] = "C"
                            Log.d("HashMap","여기들어가긴함?3")

                        }
                    }else {

                        hashMap[CalendarDay.from(
                            CalendarFromDate.get(0).toInt(),
                            (CalendarFromDate.get(1).toInt() - 1),
                            CalendarFromDate.get(2).toInt()
                        )] = i.csex
                        Log.d("HashMap", "여기들어가긴함?4    ")

                        Log.d(
                            "Calendar날짜",
                            "이게맞나" + CalendarFromDate.get(0)
                                .toInt() + (CalendarFromDate.get(1)
                                .toInt() - 1) + CalendarFromDate.get(2).toInt()
                        )
                            .toString()
                    }
                }

            }

            for(map in hashMap){
                Log.d("HashMap","key:"+map.key+"values:"+map.value)
                if(map.value=="F"){
                    calendar.addDecorator(
                        EventDecorator(
                            Collections.singleton(map.key),
                            button,
                            contextMain,
                            FamaleColor
                        )
                    )
                }else if(map.value=="M"){
                    calendar.addDecorator(
                        EventDecorator(
                            Collections.singleton(map.key),
                            button,
                            contextMain,
                            MaleColor
                        )
                    )

                }else if(map.value=="C"){
                    calendar.addDecorator(
                        EventDecorator(
                            Collections.singleton(map.key),
                            button,
                            contextMain,
                            CoupleColor
                        )
                    )
                }}
        }else{
            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
            Log.d("Retrofit", "onResponse: 캘린더실패")
        }
    }

    override fun onFailure(call: Call<List<com.example.workingparents.CalendarData>>, t: Throwable) {
        Log.d("Retrofit", "onFailure 캘린더 실패 에러1: " + t.message.toString())

    }


})




calendar.setOnDateChangedListener(object : OnDateSelectedListener {
    //사용자가 날짜를 눌렀을 때

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        list.clear()
        Log.d("예아", date.toString())
        var Calendardate= "${date.year}-0${date.month+1}-${date.day}"
        //Get을 이용해서 List로 받아온 다음에 같으면 리싸이클러뷰 ㄱㄱ

        //var couplenum:Int

        // if(UserData.connectedCouple()){
        //    couplenum= UserData.couplenum
        //}

        calendar.addDecorator(EventDecorator2(Collections.singleton(date), button, contextMain,calendar))


        RetrofitBuilder.api.getCalendar(couplenum).enqueue(object: Callback<List<CalendarData>> {

            override fun onResponse(call: Call<List<CalendarData>>, response: Response<List<CalendarData>>) {
                if(response.isSuccessful){

                    var result: List<CalendarData>? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d("따라가보자1","1")
                    Log.d("Retrofit", "onResponse: 캘린더성공"+result?.toString())

                    if (result != null) {

                        for(i in result){

                            var title= i.ctitle
                            var content= i.ccontent
                            var sex = i.csex
                            var splitdate = i.cdate.toString().split(" ")
                            Log.d("Calendar성공","Calendardate:"+Calendardate)
                            Log.d("Calendar성공","splitdate:"+splitdate.get(0))
                            //splitdate가 레트로핏으로 받아온거

                            if(splitdate.get(0).equals(Calendardate)) {

                                Log.d("Calendar성공", "일단 같은지는 확인완료:")
                                //이제 문자열 잘 잘랐으니.. 라사이클러뷰를 넣자

                                if(sex=="F") {
                                    list.add(
                                        CalendarRecyclerData(
                                            title,
                                            content,
                                            CalendarMode.female
                                        )
                                    )
                                }else{
                                    list.add(
                                        CalendarRecyclerData(
                                            title,
                                            content,
                                            CalendarMode.male
                                        )
                                    )
                                }
                            }

                        }
                        val adapter = CalendarAdapter(list)
                        recyclerCalendar.adapter = adapter

                    }
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("Retrofit", "onResponse: 캘린더실패")
                }
            }

            override fun onFailure(call: Call<List<com.example.workingparents.CalendarData>>, t: Throwable) {
                Log.d("Retrofit", "onFailure 캘린더 실패 에러2: " + t.message.toString())

            }

        })
        Log.d("따라가보자1","1")

        // calendar.addDecorator(EventDecorator2(Collections.singleton(date), button, contextMain))
        // calendar.addDecorator(EventDecorator(date,button,contextMain))
    }

})


}




class CustomDialog(context: Context) {

private val dialog = Dialog(context)

interface ButtonClickListener {
    fun onClicked(ctitle: String, ccontent: String)
}

private lateinit var onClickedListener: ButtonClickListener

fun setOnClickedListener(listener: ButtonClickListener) {
    onClickedListener = listener
}

fun myDig() {
    dialog.setContentView(com.example.workingparents.R.layout.calendar_dialog)
    dialog.window!!.setLayout(
        900,
        WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window!!.setBackgroundDrawableResource(com.example.workingparents.R.drawable.orangeborder)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setCancelable(true)

    dialog.show()

    val edit_ctitle = dialog.findViewById<EditText>(com.example.workingparents.R.id.edit_ctitle)
    val edit_ccontent = dialog.findViewById<EditText>(com.example.workingparents.R.id.edit_ccontent)

    val btnDone = dialog.findViewById<Button>(com.example.workingparents.R.id.btnDone)
    val btnCancel = dialog.findViewById<Button>(com.example.workingparents.R.id.btnCancel)



    btnDone.setOnClickListener() {
        //완료버튼을 눌렀을 때

        //리스너를 이용해서 제목,내용,성별 전송
        onClickedListener.onClicked(edit_ctitle.text.toString(), edit_ccontent.text.toString())

        dialog.dismiss()
    }

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

}

}



class TodayDecorator: DayViewDecorator {
private var date = CalendarDay.today()

override fun shouldDecorate(day: CalendarDay?): Boolean {
    return day?.equals(date)!!
}

override fun decorate(view: DayViewFacade?) {
    view?.addSpan(StyleSpan(Typeface.BOLD))
    view?.addSpan(RelativeSizeSpan(1.2f))
    view?.addSpan(ForegroundColorSpan(Color.parseColor("#FF9769")))
}
}

class EventDecorator(
dates: Collection<CalendarDay>,
val button: ImageButton,
val context: Context,
val color: IntArray
) : DayViewDecorator {


var dates= dates
//마지막으로 클릭한 날짜를 알아보기위해 Hashset에서 어레이리스트로 변경함


override fun shouldDecorate(day: CalendarDay): Boolean {

    return dates.contains(day)
}

override fun decorate(view: DayViewFacade) {
    view.addSpan(CustmMultipleDotSpan(8.0f, color))

}


class CustmMultipleDotSpan : LineBackgroundSpan {
    private val radius: Float
    private var color = IntArray(0)

    constructor() {
        radius = DEFAULT_RADIUS
        color[0] = 0
    }

    constructor(color: Int) {
        radius = DEFAULT_RADIUS
        this.color[0] = 0
    }

    constructor(radius: Float) {
        this.radius = radius
        color[0] = 0
    }

    constructor(radius: Float, color: IntArray) {
        this.radius = radius
        this.color = color
    }

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int
    ) {
        val total = if (color.size > 5) 5 else color.size
        var leftMost = (total - 1) * -10
        for (i in 0 until total) {
            val oldColor = paint.color
            if (color[i] != 0) {
                paint.color = color[i]
            }
            canvas.drawCircle(
                ((left + right) / 2 - leftMost).toFloat(),
                bottom + radius,
                radius,
                paint
            )
            paint.color = oldColor
            leftMost = leftMost + 20
        }
    }
}

}




class EventDecorator2(
dates: Collection<CalendarDay>,
val button: ImageButton,
val context: Context,
val calendar: MaterialCalendarView?
) : DayViewDecorator {

val CoupleColor  =  intArrayOf(
    Color.rgb(255, 184, 203),
    Color.rgb(155, 205, 255)
)

val FamaleColor  =  intArrayOf(
    Color.rgb(255, 184, 203)
)

val MaleColor  =  intArrayOf(
    Color.rgb(155, 205, 255)
)

val OrangeColor  =  intArrayOf(
    Color.rgb(255, 218, 197)

)

var dates= dates
//마지막으로 클릭한 날짜를 알아보기위해 Hashset에서 어레이리스트로 변경함


override fun shouldDecorate(day: CalendarDay): Boolean {
    return dates.contains(day)
}

override fun decorate(view: DayViewFacade) {
    Log.d("따라가보자2","2")

    button.setOnClickListener(object : View.OnClickListener {

        override fun onClick(p0: View?) {
            //일정추가 버튼을 클릭했을 때
            //완료 클릭했을때 Dialog 생성및 post수행
            Log.d("따라가보자3","3")

            var dialog = CustomDialog(context)
            dialog.myDig()
            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(ctitle: String, ccontent: String) {
                    //다이어로그에서 값을 받아옴

                    Log.d("따라가보자1000","1000")

                    var title = ctitle
                    var content = ccontent


                    Log.d("Dialog", "제목은" + title + "내용은" + content + "성별은" + sex)

                    //여기서 다이어로그 내용을 받아왔지
                    //그럼 Post를 여기에서 해주자

                    var cdate= dates.toString().substring(13,17)+"-0"+(dates.toString().substring(18,19).toInt()+1).toString()+"-"+dates.toString().substring(20,22)+" 00:00:00"
                    Log.d("따라가보자2000","2000")
                    Log.d("뭐가","cdate:"+cdate)
                    //[CalendarDay{2022-7-17}]을 잘라서 2022-8-17 00(뒤에서 빈칸으로 split을 해주므로 뒤에 빈칸이 있어야함)을 만들어야함
                    //참고로 dates는 이번달이 8월이면 7월로 준다...
                    //그래도 좀 효율적으로 해보려고 했는데 그냥.. 비효율적으로 하기로 했다..

                    RetrofitBuilder.api.postCalender(couplenum, cdate, ctitle, ccontent ,sex)
                        .enqueue(object : Callback<Int> {
                            override fun onResponse(
                                call: Call<Int>,
                                response: Response<Int>
                            ) {
                                if (response.isSuccessful) {
                                    //성공하면 이미 전적이 있던걸로 취급하기
                                    Log.d("따라가보자4","4")
                                    var result: Int? = response.body()
                                    // 정상적으로 통신이 성공된 경우
                                    Log.d(
                                        "Calendar",
                                        "onResponse: 성공했나" + result?.toString()
                                    )

                                    Log.d("따라가보자1","1")



                                    var splitdate = dates.toString().split("}")
                                    //result의 날짜를 Calendar 형식이 되도록 문자열을 잘라줌

                                    var CalendarFromDate: List<String> = splitdate.get(0).toString().split("-")
                                    //자른 문자

                                    val firstdate =
                                        CalendarFromDate.get(0).substring(CalendarFromDate.get(0).lastIndexOf("{") + 1)
                                    Log.d(
                                        "Date제발",
                                        "firstdate:" + firstdate + "CalendarFromDate.get(1):" + CalendarFromDate.get(1) + "CalendarFromDate.get(2):" + CalendarFromDate.get(
                                            2
                                        )
                                    )
                                    Log.d("Date제발", "CalendarFromDate:" + CalendarFromDate)
                                    Log.d("따라가보자7","7")

                                    if (hashMap.containsKey(
                                            CalendarDay.from(
                                                firstdate.toInt(),
                                                (CalendarFromDate.get(1).toInt()),
                                                CalendarFromDate.get(2).toInt()
                                            )
                                        )
                                    ) {
                                        Log.d("점세개머선일이고","기존에 같은날짜가 있다면1")
                                        //기존에 이미 같은 키 값(날짜)이 있다면
                                        Log.d("HashMap", "여기들어가긴함?5")
                                        if (hashMap.get(
                                                CalendarDay.from(
                                                    firstdate.toInt(),
                                                    (CalendarFromDate.get(1).toInt()),
                                                    CalendarFromDate.get(2).toInt()
                                                )
                                            ) == sex
                                        ) {
                                            //근데 같은 성별이라면
                                            Log.d("점세개머선일이고","근데 같은성별이야2")
                                            if (sex == "F") {

                                                hashMap[CalendarDay.from(
                                                    firstdate.toInt(),
                                                    (CalendarFromDate.get(1).toInt()),
                                                    CalendarFromDate.get(2).toInt()
                                                )] = "F"
                                                Log.d("점세개머선일이고","근데 여자야3")
                                                calendar!!.addDecorator(
                                                    EventDecorator(
                                                        Collections.singleton(CalendarDay.from(
                                                            firstdate.toInt(),
                                                            (CalendarFromDate.get(1).toInt()),
                                                            CalendarFromDate.get(2).toInt()
                                                        )),
                                                        button,
                                                        context,
                                                        FamaleColor
                                                    )
                                                )
                                                Log.d("점세개머선일이고","근데 여자야 점도 찍었어4")
                                                //view.addSpan(CustmMultipleDotSpan(8.0f, FamaleColor))
                                            } else if(sex == "M"){
                                                hashMap[CalendarDay.from(
                                                    firstdate.toInt(),
                                                    (CalendarFromDate.get(1).toInt()),
                                                    CalendarFromDate.get(2).toInt()
                                                )] = "M"
                                                Log.d("점세개머선일이고","근데 남자야5")
                                                calendar!!.addDecorator(
                                                    EventDecorator(
                                                        Collections.singleton(CalendarDay.from(
                                                            firstdate.toInt(),
                                                            (CalendarFromDate.get(1).toInt()),
                                                            CalendarFromDate.get(2).toInt()
                                                        )),
                                                        button,
                                                        context,
                                                        MaleColor
                                                    )
                                                )
                                                Log.d("점세개머선일이고","근데 남자야 점도찍었어6")
                                                //  view.addSpan(CustmMultipleDotSpan(8.0f, MaleColor))
                                            }
                                            Log.d("HashMap", "여기들어가긴함?6")
                                        } else {
                                            //아니라면
                                            // view.addSpan(CustmMultipleDotSpan(8.0f, CoupleColor))

                                            hashMap[CalendarDay.from(
                                                firstdate.toInt(),
                                                (CalendarFromDate.get(1).toInt()),
                                                CalendarFromDate.get(2).toInt()
                                            )] = "C"
                                            Log.d("점세개머선일이고","근데 커플이야7")


                                            calendar!!.addDecorator(
                                                EventDecorator(
                                                    Collections.singleton(CalendarDay.from(
                                                        firstdate.toInt(),
                                                        (CalendarFromDate.get(1).toInt()),
                                                        CalendarFromDate.get(2).toInt()
                                                    )),
                                                    button,
                                                    context,
                                                    OrangeColor
                                                )
                                            )

                                            calendar!!.addDecorator(
                                                EventDecorator(
                                                    Collections.singleton(CalendarDay.from(
                                                        firstdate.toInt(),
                                                        (CalendarFromDate.get(1).toInt()),
                                                        CalendarFromDate.get(2).toInt()
                                                    )),
                                                    button,
                                                    context,
                                                    OrangeColor
                                                )
                                            )

                                            calendar!!.addDecorator(
                                                EventDecorator(
                                                    Collections.singleton(CalendarDay.from(
                                                        firstdate.toInt(),
                                                        (CalendarFromDate.get(1).toInt()),
                                                        CalendarFromDate.get(2).toInt()
                                                    )),
                                                    button,
                                                    context,
                                                    CoupleColor
                                                )
                                            )
                                            calendar!!.invalidateDecorators()

                                            Log.d("점세개머선일이고","근데 커플이야 점도 찍었어8")

                                            // view.addSpan(CustmMultipleDotSpan(8.0f, CoupleColor))

                                            Log.d("HashMap", "여기들어가긴함?7")
                                        }
                                    } else {
                                        Log.d("점세개머선일이고","아직 날짜가 없어")
                                        Log.d("따라가보자8","8")
                                        if (sex == "F") {
                                            hashMap[CalendarDay.from(
                                                firstdate.toInt(),
                                                (CalendarFromDate.get(1).toInt()),
                                                CalendarFromDate.get(2).toInt()
                                            )] = "F"

                                            Log.d("점세개머선일이고","아직 날짜가 없어 근데여자야")
                                            calendar!!.addDecorator(
                                                EventDecorator(
                                                    Collections.singleton(CalendarDay.from(
                                                        firstdate.toInt(),
                                                        (CalendarFromDate.get(1).toInt()),
                                                        CalendarFromDate.get(2).toInt()
                                                    )),
                                                    button,
                                                    context,
                                                    FamaleColor
                                                )
                                            )
                                            Log.d("점세개머선일이고","아직 날짜가 없어 근데여자야 점도찍었어")
                                            //  view.addSpan(CustmMultipleDotSpan(8.0f, FamaleColor))
                                        } else {
                                            Log.d("점세개머선일이고","아직 날짜가 없어 근데남자야")
                                            hashMap[CalendarDay.from(
                                                firstdate.toInt(),
                                                (CalendarFromDate.get(1).toInt()),
                                                CalendarFromDate.get(2).toInt()
                                            )] = "M"
                                            calendar!!.addDecorator(
                                                EventDecorator(
                                                    Collections.singleton(CalendarDay.from(
                                                        firstdate.toInt(),
                                                        (CalendarFromDate.get(1).toInt()),
                                                        CalendarFromDate.get(2).toInt()
                                                    )),
                                                    button,
                                                    context,
                                                    MaleColor
                                                )
                                            )


                                            Log.d("점세개머선일이고","아직 날짜가 없어 근데남자야 점도 찍었어")
                                            //  view.addSpan(CustmMultipleDotSpan(8.0f, MaleColor))
                                        }
                                    }

                                } else {
                                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)

                                    Log.d(
                                        "Calendar",
                                        "response 실패 에러"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<Int>, t: Throwable) {
                                Log.d(
                                    "Calendar",
                                    "onFailure 회원가입 실패 에러: " + t.message.toString()
                                )

                            }

                        })

                }
            })

        }

    })


//위에서 만들어준 hashMap이 있으니까...
    //새로 들어온 애도 비교를 해주면 되지 않을까...
    Log.d("따라가보자5","5")

}

class CustmMultipleDotSpan : LineBackgroundSpan {
    private val radius: Float
    private var color = IntArray(0)

    constructor() {
        radius = DEFAULT_RADIUS
        color[0] = 0
    }

    constructor(color: Int) {
        radius = DEFAULT_RADIUS
        this.color[0] = 0
    }

    constructor(radius: Float) {
        this.radius = radius
        color[0] = 0
    }

    constructor(radius: Float, color: IntArray) {
        this.radius = radius
        this.color = color
    }

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int
    ) {
        val total = if (color.size >3 ) 3 else color.size
        Log.d("Colors","color사이즈:"+color.size)
        var leftMost = (total - 1) * -10
        for (i in 0 until total) {
            val oldColor = paint.color
            if (color[i] != 0) {
                paint.color = color[i]
            }
            canvas.drawCircle(
                ((left + right) / 2 - leftMost).toFloat(),
                bottom + radius,
                radius,
                paint
            )
            paint.color = oldColor
            leftMost = leftMost + 20
        }
    }
}

}

}

class MinMaxDecorator(min:CalendarDay, max:CalendarDay):DayViewDecorator {
val maxDay = max
val minDay = min
override fun shouldDecorate(day: CalendarDay?): Boolean {
return (day?.month == maxDay.month && day.day > maxDay.day)
        || (day?.month == minDay.month && day.day < minDay.day)
}
override fun decorate(view: DayViewFacade?) {
view?.addSpan(object:ForegroundColorSpan(Color.parseColor("#FF000000")){})
view?.setDaysDisabled(false)
}
}

/*
class SaturdayDecorator:DayViewDecorator {
private val calendar = CalendarData.getInstance()
override fun shouldDecorate(day: CalendarDay?): Boolean {
day?.copyTo(calendar)
val weekDay = calendar.get(CalendarData.DAY_OF_WEEK)
return weekDay == CalendarData.SATURDAY
}
override fun decorate(view: DayViewFacade?) {
view?.addSpan(object:ForegroundColorSpan(Color.BLUE){})
}
}


class SundayDecorator:DayViewDecorator {
private val calendar = CalendarData.getInstance()
override fun shouldDecorate(day: CalendarDay?): Boolean {
day?.copyTo(calendar)
val weekDay = calendar.get(CalendarData.DAY_OF_WEEK)
return weekDay == CalendarData.SUNDAY
}
override fun decorate(view: DayViewFacade?) {
view?.addSpan(object:ForegroundColorSpan(Color.RED){})
}
}
*/