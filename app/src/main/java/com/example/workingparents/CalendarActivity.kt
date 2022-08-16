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
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS
import kotlinx.android.synthetic.main.activity_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CalendarActivity : AppCompatActivity() {
    companion object {
        var hashMap= HashMap<CalendarDay,String>()
    }

    //val hashMap= HashMap<CalendarDay,String>()

    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var contextMain: Context = this@CalendarActivity

        lateinit var calendar: MaterialCalendarView
        calendar = findViewById(R.id.calendar)

        calendar.setSelectedDate(CalendarDay.today())


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

        val list = ArrayList<CalendarData>()
//Get할 때 CalendarData형식으로 넣어주는 리스트




        RetrofitBuilder.api.getCalendar(UserData.couplenum).enqueue(object: Callback<List<Calendar>> {

            override fun onResponse(call: Call<List<Calendar>>, response: Response<List<Calendar>>) {
                if(response.isSuccessful){

                    var result: List<Calendar>? = response.body()
                    // 정상적으로 통신이 성공된 경우
                    Log.d("Retrofit", "onResponse: 캘린더성공"+result?.toString())

                    if (result != null) {

                        for(i in result){
                            //Get으로 가져온 result

                            var sex:ArrayList<String>? = null
                            if (sex != null) {
                                sex.add(i.csex)
                            }


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

            override fun onFailure(call: Call<List<com.example.workingparents.Calendar>>, t: Throwable) {
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



                RetrofitBuilder.api.getCalendar(UserData.couplenum).enqueue(object: Callback<List<Calendar>> {

                    override fun onResponse(call: Call<List<Calendar>>, response: Response<List<Calendar>>) {
                        if(response.isSuccessful){

                            var result: List<Calendar>? = response.body()
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
                                                CalendarData(
                                                    title,
                                                    content,
                                                    CalendarMode.female
                                                )
                                            )
                                        }else{
                                            list.add(
                                                CalendarData(
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

                    override fun onFailure(call: Call<List<com.example.workingparents.Calendar>>, t: Throwable) {
                        Log.d("Retrofit", "onFailure 캘린더 실패 에러2: " + t.message.toString())

                    }

                })
                Log.d("따라가보자1","1")
                calendar.addDecorator(EventDecorator2(Collections.singleton(date), button, contextMain))
                calendar.addDecorator(EventDecorator2(Collections.singleton(date), button, contextMain))
                // calendar.addDecorator(EventDecorator(date,button,contextMain))
            }

        })


    }




    class CustomDialog(context: Context) {

        private val dialog = Dialog(context)
        private lateinit var csex: String

        interface ButtonClickListener {
            fun onClicked(ctitle: String, ccontect: String, csex: String?)
        }

        private lateinit var onClickedListener: ButtonClickListener

        fun setOnClickedListener(listener: ButtonClickListener) {
            onClickedListener = listener
        }

        fun myDig() {
            dialog.setContentView(R.layout.custom_dialog)

            dialog.window!!.setLayout(
                900,
                WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawableResource(com.example.workingparents.R.drawable.orangeborder)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)

            dialog.show()


            val edit_ctitle = dialog.findViewById<EditText>(com.example.workingparents.R.id.edit_ctitle)
            val edit_ccontent = dialog.findViewById<EditText>(com.example.workingparents.R.id.edit_ccontent)
            val radio_group_dialog = dialog.findViewById<RadioGroup>(com.example.workingparents.R.id.radio_group_dialog)

            val btnDone = dialog.findViewById<Button>(com.example.workingparents.R.id.btnDone)
            val btnCancel = dialog.findViewById<Button>(com.example.workingparents.R.id.btnCancel)


            btnDone.setOnClickListener {
                //완료버튼을 눌렀을 때

                when(radio_group_dialog.checkedRadioButtonId){

                    R.id.momBtnDialog-> csex =  "F"
                    R.id.dadBtnDialog-> csex =  "M"

                }

                //리스너를 이용해서 제목,내용,성별 전송
                onClickedListener.onClicked(edit_ctitle.text.toString(), edit_ccontent.text.toString(),csex)
                dialog.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }



    class TodayDecorator: DayViewDecorator {
        private var date = CalendarDay.today()

//        private var date = CalendarDay.from(2022,11,10)


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
        val button: Button,
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
        val button: Button,
        val context: Context
    ) : DayViewDecorator {

        var sex: String?=null

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

        var dates= dates
        //마지막으로 클릭한 날짜를 알아보기위해 Hashset에서 어레이리스트로 변경함

        var ClickVali:Boolean= false
        //post하고 나서 점을 찍는걸로 바꿔줌
        //이렇게 하지 않으면 post안했는데도 그냥 클릭했을 때도 점이 바뀜

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
                        override fun onClicked(ctitle: String, ccontect: String, csex: String?) {
                            //다이어로그에서 값을 받아옴

                            Log.d("따라가보자1000","1000")

                            var title = ctitle
                            var contect = ccontect
                            sex = csex


                            Log.d("Dialog", "제목은" + title + "내용은" + contect + "성별은" + csex)

                            //여기서 다이어로그 내용을 받아왔지
                            //그럼 Post를 여기에서 해주자

                            var cdate= dates.toString().substring(13,17)+"-0"+(dates.toString().substring(18,19).toInt()+1).toString()+"-"+dates.toString().substring(20,22)+" 00:00:00"
                            Log.d("따라가보자2000","2000")
                            Log.d("뭐가","cdate:"+cdate)
                            //[CalendarDay{2022-7-17}]을 잘라서 2022-8-17 00(뒤에서 빈칸으로 split을 해주므로 뒤에 빈칸이 있어야함)을 만들어야함
                            //참고로 dates는 이번달이 8월이면 7월로 준다...
                            //그래도 좀 효율적으로 해보려고 했는데 그냥.. 비효율적으로 하기로 했다..

                            RetrofitBuilder.api.postCalender(UserData.couplenum, cdate, ctitle, ccontect ,csex)
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
                                            ClickVali= true


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

            if(ClickVali) {
                Log.d("따라가보자6","6")

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
                        if (sex == "F") {

                            hashMap[CalendarDay.from(
                                firstdate.toInt(),
                                (CalendarFromDate.get(1).toInt()),
                                CalendarFromDate.get(2).toInt()
                            )] = "F"

                            view.addSpan(CustmMultipleDotSpan(8.0f, FamaleColor))
                        } else if (sex == "M") {
                            hashMap[CalendarDay.from(
                                firstdate.toInt(),
                                (CalendarFromDate.get(1).toInt()),
                                CalendarFromDate.get(2).toInt()
                            )] = "M"
                            view.addSpan(CustmMultipleDotSpan(8.0f, MaleColor))
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
                        view.addSpan(CustmMultipleDotSpan(8.0f, CoupleColor))

                        Log.d("HashMap", "여기들어가긴함?7")
                    }
                } else {
                    Log.d("따라가보자8","8")
                    if (sex == "F") {
                        hashMap[CalendarDay.from(
                            firstdate.toInt(),
                            (CalendarFromDate.get(1).toInt()),
                            CalendarFromDate.get(2).toInt()
                        )] = "F"
                        view.addSpan(CustmMultipleDotSpan(8.0f, FamaleColor))
                    } else if (sex == "M") {
                        hashMap[CalendarDay.from(
                            firstdate.toInt(),
                            (CalendarFromDate.get(1).toInt()),
                            CalendarFromDate.get(2).toInt()
                        )] = "M"
                        view.addSpan(CustmMultipleDotSpan(8.0f, MaleColor))
                    }
                }
            }
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