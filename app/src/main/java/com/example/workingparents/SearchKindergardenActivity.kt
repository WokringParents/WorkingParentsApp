package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.transition.TransitionSet
import androidx.transition.*
import com.transitionseverywhere.extra.Scale
import kotlinx.android.synthetic.main.activity_register_child.*
import kotlinx.android.synthetic.main.activity_search_kindergarden.*


class SearchKindergardenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_kindergarden)

        //뒤로가기
        searchkg_back.setOnClickListener{
            onBackPressed()
        }

        val set = TransitionSet().addTransition(Scale(0.7f)).addTransition(Fade())

        cardview.visibility=INVISIBLE
        search_kg.setOnClickListener {

            set.setInterpolator(FastOutLinearInInterpolator())
            TransitionManager.beginDelayedTransition(cardview,set)
            cardview.visibility= VISIBLE
            select1.setOnClickListener {
                selectkg("동신유치원")
                finish()
            }
            select2.setOnClickListener {
                selectkg("동산초등학교 병설유치원")
                finish()
            }
            select3.setOnClickListener {
                selectkg("동도초등학교 병설유치원")
                finish()
            }
        }
    }
}