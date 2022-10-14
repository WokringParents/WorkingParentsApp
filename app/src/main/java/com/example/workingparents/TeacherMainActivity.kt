package com.example.workingparents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.workingparents.Calendar.CalendarFragment
import com.example.workingparents.Goback.GobackFragment
import com.example.workingparents.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherMainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var gobackFragment: GobackFragment? = null
    private var noticeFragment: NoticeFragment? = null
    private var cafeteriaFragment: CafeteriaFragment? = null
    private var teacherMyPageFragment: TeacherMyPageFragment? = null

    private val TAG = "TeacherMain"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_main)


        var bnv_main = findViewById(R.id.teacher_bottom_menu) as BottomNavigationView
        var menu = bnv_main.menu
        bnv_main.itemIconTintList = null

        //바텀바 아이템 꾹누르면 이름 떴던거 없앰
        val longClickListener = View.OnLongClickListener { true }
        findViewById<View>(R.id.first_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.second_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.third_tab).setOnLongClickListener(longClickListener)
        findViewById<View>(R.id.fourth_tab).setOnLongClickListener(longClickListener)

        initBottomNavigation(bnv_main)

    }

    private fun initBottomNavigation(bottomNavi: BottomNavigationView){

        var menu = bottomNavi.menu

        gobackFragment= GobackFragment()
        fragmentManager.beginTransaction().replace(R.id.container, gobackFragment!!).commit()


        bottomNavi.run {

            setOnNavigationItemSelectedListener {
                /*
                  최초 선택 시 fragment add, 선택된 프래그먼트 show, 나머지 프래그먼트 hide
                  다른 프래그먼트로 화면이 전환되었을 때 이전에 만든 프래그먼트가 유지될수있도록 하기위함이다.
                   */
                when (it.itemId) {

                    R.id.first_tab -> {
                        if (gobackFragment == null) {
                            gobackFragment = GobackFragment()
                            fragmentManager.beginTransaction().add(R.id.container, gobackFragment!!).commit()
                        }
                        if (gobackFragment != null) fragmentManager.beginTransaction().show(gobackFragment!!).commit()
                        if (noticeFragment != null) fragmentManager.beginTransaction().hide(noticeFragment!!).commit()
                        if (cafeteriaFragment != null) fragmentManager.beginTransaction().hide(cafeteriaFragment!!).commit()
                        if (teacherMyPageFragment != null) fragmentManager.beginTransaction().hide(teacherMyPageFragment!!).commit()

                        //아이콘 변경
                        it.setIcon(R.drawable.icon_goback2)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)


                    }
                    R.id.second_tab -> {
                        if (noticeFragment == null) {
                            noticeFragment = NoticeFragment()
                            fragmentManager.beginTransaction().add(R.id.container, noticeFragment!!).commit()
                        }
                        if (gobackFragment != null) fragmentManager.beginTransaction().hide(gobackFragment!!).commit()
                        if (noticeFragment != null) fragmentManager.beginTransaction().show(noticeFragment!!).commit()
                        if (cafeteriaFragment != null) fragmentManager.beginTransaction().hide(cafeteriaFragment!!).commit()
                        if (teacherMyPageFragment != null) fragmentManager.beginTransaction().hide(teacherMyPageFragment!!).commit()

                        it.setIcon(R.drawable.icon_notice2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)

                    }
                    R.id.third_tab -> {
                        if (cafeteriaFragment == null) {
                            cafeteriaFragment = CafeteriaFragment()
                            fragmentManager.beginTransaction().add(R.id.container, cafeteriaFragment!!).commit()
                        }
                        if (gobackFragment != null) fragmentManager.beginTransaction().hide(gobackFragment!!).commit()
                        if (noticeFragment != null) fragmentManager.beginTransaction().hide(noticeFragment!!).commit()
                        if (cafeteriaFragment != null) fragmentManager.beginTransaction().show(cafeteriaFragment!!).commit()
                        if (teacherMyPageFragment != null) fragmentManager.beginTransaction().hide(teacherMyPageFragment!!).commit()


                        it.setIcon(R.drawable.icon_cafeteria2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.fourth_tab).setIcon(R.drawable.bottom_mypage)

                    }
                    R.id.fourth_tab -> {

                        if (teacherMyPageFragment == null) {
                            teacherMyPageFragment = TeacherMyPageFragment()
                            fragmentManager.beginTransaction().add(R.id.container, teacherMyPageFragment!!).commit()
                        }

                        if (gobackFragment != null) fragmentManager.beginTransaction().hide(gobackFragment!!).commit()
                        if (noticeFragment != null) fragmentManager.beginTransaction().hide(noticeFragment!!).commit()
                        if (cafeteriaFragment != null) fragmentManager.beginTransaction().hide(cafeteriaFragment!!).commit()
                        if (teacherMyPageFragment != null) fragmentManager.beginTransaction().show(teacherMyPageFragment!!).commit()


                        it.setIcon(R.drawable.bottom_mypage2)
                        menu.findItem(R.id.first_tab).setIcon(R.drawable.icon_goback)
                        menu.findItem(R.id.second_tab).setIcon(R.drawable.icon_notice)
                        menu.findItem(R.id.third_tab).setIcon(R.drawable.icon_cafeteria)
                    }


                }
                true

            }
            selectedItemId = R.id.first_tab
        }

    }
}