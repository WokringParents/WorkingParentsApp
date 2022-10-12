package com.example.workingparents.Goback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GobackAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {

        return when (position) { //switch()문과 동일하다.
            0 -> {GobackFragmentTab()}
            else -> {return GobackFragmentTab2()}
        }
    }

    override fun getCount(): Int {
        return 2 //3개니깐
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "탭메뉴1"
            else -> {return "탭메뉴2"}
        }
    }

}