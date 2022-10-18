package com.example.workingparents.Calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.R
import kotlinx.android.synthetic.main.calendar_dialog_update_delete.view.*
import kotlinx.android.synthetic.main.calendar_female.view.*
import kotlinx.android.synthetic.main.schedule_male.view.*
import kotlinx.android.synthetic.main.schedule_male.view.calendarcontent
import kotlinx.android.synthetic.main.schedule_male.view.startTime

/*
캘린더 리사이클러뷰 전체로직
액티비티에서 어댑터가 불려지면 뷰홀더 객체를 만든 뒤 뷰에 데이터를 설정함
아마 내생각에...
onCreateViewHolder에서 항목에 사용할 뷰를 생성하고 뷰홀더를 반환함
이때 ViewHolder클래스를 호출하여 뷰에 있는 아이템이랑 데이터클래스에 있는 변수랑 바인딩해줌
onBindViewHolder에서 항목뷰에 데이터를 연결함

1. 액티비티에서 다이얼로그를 부르기 위해 액티비티로 이벤트를 전달하여 클릭이벤트를 발생시키기
- 어댑터에 인터페이스 생성하여 호출하는 곳에서 인터페이스 정의하여 이벤트처리 방식을 사용


 */

class CalendarAdapter(private val context: Context, private val items: ArrayList<CalendarRecyclerData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: CalendarRecyclerData, pos: Int)
        fun onItemEditClick(data: CalendarRecyclerData, pos: Int)
        fun onItemDeleteClick(data: CalendarRecyclerData, pos: Int)

    }

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    fun updateItem(pos: Int, usr: CalendarRecyclerData) {
        items[pos] = usr
    }

    fun deleteItem(pos: Int) {
        items.removeAt(pos)
    }
    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return items.get(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == CalendarMode.female) {
            val inflatedView =
                LayoutInflater.from(parent.context).inflate(R.layout.schedule_female, parent, false)
            return ViewHolder(inflatedView)
        } else {
            val inflatedView =
                LayoutInflater.from(parent.context).inflate(R.layout.schedule_male, parent, false)
            return ViewHolder(inflatedView)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            if(position!= RecyclerView.NO_POSITION)
            {
                clickListener?.onItemClick(item,position)
            }
        }

        //CalendarData= items.get(position).title


        holder.run {
            itemView.tag = items[position]

            itemView.btnUpdate?.setOnClickListener {
                clickListener?.onItemEditClick(items[position], position)
            }
            itemView.btnDelete?.setOnClickListener {
                clickListener?.onItemDeleteClick(items[position], position)
            }
        }


        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }

    }


    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        //각 항목의 뷰를 재활용하기 위해 보관하는 클래스
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: CalendarRecyclerData) {
            view.startTime.text = item.startTime
            view.calendarcontent.text = item.ccontent
            //뷰에 있는 아이템이랑 데이터클래스에 있는 변수랑 바인딩해줌
            view.setOnClickListener(listener)
        }
    }


    }
