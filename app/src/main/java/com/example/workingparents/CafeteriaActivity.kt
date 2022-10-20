package com.example.workingparents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_cafeteria.*

class CafeteriaActivity : AppCompatActivity() {

    private val TAG="CafeteriaDetail"
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)

        detailCafeteria_back.setOnClickListener{
            onBackPressed()
        }

        val cafeData = intent.getParcelableExtra<CafeteriaByDate>("CafeteriaByDate")

        recyclerView= findViewById(R.id.detailCafeteriaRecyclerView)

        Log.d(TAG, cafeData.toString())

        if (cafeData != null) {
            Log.d(TAG,cafeData.cdate)
            Log.d(TAG,cafeData.images.toString())
            Log.d(TAG,cafeData.contents.toString())
            Log.d(TAG,cafeData.imageBytes.toString())
            Log.d(TAG,cafeData.imageBytes.get(0).toString())
            Log.d(TAG,"cafeData != null")

            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            recyclerView.visibility = View.VISIBLE
            recyclerView.setHasFixedSize(true)
            val detailAdapter= CafeteriaDetailAdapter(this, cafeData)
            recyclerView.adapter= detailAdapter
            detailAdapter.notifyDataSetChanged()

        }

    }


}