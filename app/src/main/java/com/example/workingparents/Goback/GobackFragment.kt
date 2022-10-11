package com.example.workingparents.Goback

import android.content.Context
import com.example.workingparents.Goback.*
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

        val fragmentadapter = GobackAdapter(getParentFragmentManager())
        viewpager_main.adapter = fragmentadapter

        tab_layout.setupWithViewPager(viewpager_main)

        return view
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