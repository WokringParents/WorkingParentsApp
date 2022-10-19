package com.example.workingparents.Goback

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.net.Uri
import com.example.workingparents.Goback.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.*
import com.example.workingparents.Calendar.CalendarAdapter
import com.example.workingparents.Calendar.CalendarRecyclerData
import com.example.workingparents.WriteCafeteriaActivity.Companion.mContext
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_goback.*
import kotlinx.android.synthetic.main.fragment_goback.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class GobackFragment : Fragment() {

    companion object{

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TeacherMainActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_goback, container, false)
        var listGoback = ArrayList<GobackData>()
        val listEmergency = ArrayList<EmergencyData>()

        var TAG = "GoBack"

        //레트로핏으로 아이 이름 가져오기

        RetrofitBuilder.api.getAllChild().enqueue(object : Callback<List<Child>> {
            override fun onResponse(call: Call<List<Child>>, response: Response<List<Child>>) {
                if (response.isSuccessful) {
                    var result: List<Child>? = response.body()

                    if (result != null) {
                        for (i in result) {
                            listGoback.add(GobackData(i.name, i.couplenum))
                            Log.d(TAG, "1번째" + listGoback.get(0).goback_childName)
                            listEmergency.add(EmergencyData(i.name, i.couplenum))
                        }
                    }

                    Log.d(TAG, "2번째" + listGoback.get(0).goback_childName)

                } else {
                    Log.d(TAG, "onResponse 후 실패 에러: ")
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
                var adapter = GobackAdapter(listGoback)
                recyclerGoback.addItemDecoration(MyDecoration(2, Color.parseColor("#f2f2f2")))
                recyclerGoback.layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                recyclerGoback.adapter = adapter

            }

            override fun onFailure(call: Call<List<Child>>, t: Throwable) {
                Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
            }

        })

        val btnGoback = view.findViewById<Button>(R.id.btnGoback)
        val btnEmergency = view.findViewById<Button>(R.id.btnEmergency)

        val recyclerGoback = view.findViewById<RecyclerView>(R.id.recyclerGoback)
        val recyclerEmergency = view.findViewById<RecyclerView>(R.id.recyclerEmergency)

        val GobackUnderline = view.findViewById<View>(R.id.GobackUnderline)
        val EmergencyUnderline = view.findViewById<View>(R.id.EmergencyUnderline)


        btnGoback.setOnClickListener {

            recyclerEmergency.setVisibility(View.GONE)
            EmergencyUnderline.setVisibility(View.GONE)

            recyclerGoback.setVisibility(View.VISIBLE)
            GobackUnderline.setVisibility(View.VISIBLE)

        }


        btnEmergency.setOnClickListener {

            recyclerGoback.setVisibility(View.GONE)
            GobackUnderline.setVisibility(View.GONE)

            recyclerEmergency.setVisibility(View.VISIBLE)
            EmergencyUnderline.setVisibility(View.VISIBLE)

            Log.d(TAG, "1번")
            //divider
            val adapter = EmergencyAdapter(listEmergency)
            recyclerEmergency.addItemDecoration(MyDecoration(2, Color.parseColor("#f2f2f2")))
            recyclerEmergency.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)


            adapter.setOnItemClickListener(object : EmergencyAdapter.OnItemClickListener {
                override fun onItemClick(data: EmergencyData, pos: Int) {


                    RetrofitBuilder.api.getPnumberForGoback(data.emergency_couplenum)
                        .enqueue(object : Callback<List<String>> {
                            override fun onResponse(
                                call: Call<List<String>>,
                                response: Response<List<String>>
                            ) {
                                if (response.isSuccessful) {
                                    var result: List<String>? = response.body()
                                    if (result != null) {

                                        val dialog = CustomDialog(mContext)
                                        dialog.myDig()

                                        dialog.setOnClickedListener(object: CustomDialog.ButtonClickListener {
                                            override fun onClicked(
                                                valiMomBtn: Boolean,
                                                valiDadBtn: Boolean
                                            ) {
                                                if (valiMomBtn == true) {
                                                    // 어디에 전화를 걸건지 text 정보 받기
                                                    val input = result.get(0)
                                                    // Uri를 이용해서 정보 저장
                                                    val myUri = Uri.parse("tel:${input}")
                                                    // 전환할 정보 설정 - ACTION_DIAL
                                                    val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                                                    // 이동
                                                    startActivity(myIntent)

                                                }

                                                if (valiDadBtn == true) {
                                                    // 어디에 전화를 걸건지 text 정보 받기
                                                    val input = result.get(1)
                                                    // Uri를 이용해서 정보 저장
                                                    val myUri = Uri.parse("tel:${input}")
                                                    // 전환할 정보 설정 - ACTION_DIAL
                                                    val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                                                    // 이동
                                                    startActivity(myIntent)

                                                }
                                            }

                                        })


                                    }
                                } else {
                                    Log.d(TAG, "onResponse 후 실패 에러: ")
                                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                                }

                            }

                            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                                Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
                            }

                        })

                }
            })
            recyclerEmergency.adapter = adapter
            Log.d(TAG, "5번")


        }


        return view
    }

    class CustomDialog(val context: Context) {

        private val dialog = Dialog(context)

        interface ButtonClickListener {
            //버튼클릭 인터페이스
            fun onClicked(
                valiMomBtn: Boolean,
                valiDadBtn: Boolean
            )
        }
        private lateinit var onClickedListener: ButtonClickListener


        fun setOnClickedListener(listener: ButtonClickListener) {
            onClickedListener = listener
        }


        fun myDig() {
            //일정 수정 및 삭제를 위한 다이얼로그를 생성하는 함수

            dialog.setContentView(com.example.workingparents.R.layout.emergency_dialog_call)

            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawableResource(com.example.workingparents.R.drawable.orangeborder)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)

            dialog.show()

            val CallMomBtn =
                dialog.findViewById<Button>(com.example.workingparents.R.id.CallMomBtn)
            val CallDadyBtn = dialog.findViewById<Button>(R.id.CallDadyBtn)

            CallMomBtn.setOnClickListener() {

                onClickedListener.onClicked(true,false)

            }

            CallDadyBtn.setOnClickListener() {

                onClickedListener.onClicked(false,true)

            }

        }
    }
}
