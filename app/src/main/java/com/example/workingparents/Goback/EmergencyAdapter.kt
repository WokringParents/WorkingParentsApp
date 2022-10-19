package com.example.workingparents.Goback

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.FCMRetrofitBuilder
import com.example.workingparents.R
import com.example.workingparents.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_alarm_loading.*
import kotlinx.android.synthetic.main.emergency_item.view.*
import kotlinx.android.synthetic.main.goback_item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class EmergencyAdapter(private val items: ArrayList<EmergencyData>) : RecyclerView.Adapter<EmergencyAdapter.ViewHolder>() {

    private var TAG="Emergency"

    interface OnItemClickListener{
        fun onItemClick(data: EmergencyData, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EmergencyAdapter.ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            if(position!= RecyclerView.NO_POSITION)
            {
                listener?.onItemClick(item,position)
            }
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.emergency_item, parent, false)
        return EmergencyAdapter.ViewHolder(inflatedView)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: EmergencyData) {
            view.emergency_childName.text = item.emergency_childName

            //버튼 클릭 시
            view.emergency_btnCall.setOnClickListener(listener)

            view.emergency_btnMsg.setOnClickListener{

            }

            }

        }

    }