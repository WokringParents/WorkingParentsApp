package com.example.workingparents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_female.view.*

class CalendarAdapter(private val items: ArrayList<CalendarRecyclerData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> ID : ${item.title}, Name : ${item.content}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == CalendarMode.female) {
            val inflatedView =
                LayoutInflater.from(parent.context).inflate(R.layout.calendar_female, parent, false)
            return CalendarAdapter.ViewHolder(inflatedView)
        } else {
            val inflatedView =
                LayoutInflater.from(parent.context).inflate(R.layout.calendar_male, parent, false)
            return CalendarAdapter.ViewHolder(inflatedView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items.get(position).viewType

    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        //각 항목의 뷰를 재활용하기 위해 보관하는 클래스
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: CalendarRecyclerData) {
            view.calendartitle.text = item.title
            view.calendarcontent.text = item.content
            //뷰에 있는 아이템이랑 데이터클래스에 있는 변수랑 바인딩해줌
            view.setOnClickListener(listener)
        }
    }
}