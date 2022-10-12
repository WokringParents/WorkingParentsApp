package com.example.workingparents.Goback

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.workingparents.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_goback.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GobackFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var tab1:GobackFragmentTab
    lateinit var tab2:GobackFragmentTab2

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_goback, container, false)

        tab1  = GobackFragmentTab() //프레그먼트 GobackFragmentTab 객체화
        tab2 = GobackFragmentTab2()

        /*
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

        })

         */
        return view
    }

    private fun replaceView(tab: Fragment) {

        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment?.let {



        }


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GobackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}