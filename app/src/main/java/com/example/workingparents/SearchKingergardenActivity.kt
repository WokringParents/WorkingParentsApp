package com.example.workingparents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.transition.TransitionSet
import kotlinx.android.synthetic.main.activity_search_kingergarden.*
import androidx.transition.*
import com.transitionseverywhere.extra.Scale


class SearchKingergardenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_kingergarden)

        val set = TransitionSet().addTransition(Scale(0.5f)).addTransition(Fade())

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