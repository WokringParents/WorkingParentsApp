package com.example.workingparents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

private var context: Context? = null
private val TAG = "NDAdapter"


class NoticeDetailAdapter(noticeDatalist: ArrayList<NoticeData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var noticeDataList : ArrayList<NoticeData>? = noticeDatalist
    override fun getItemCount(): Int {return noticeDataList!!.size}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_detail_content, parent, false)
            context = parent.context
            return ContentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_detail_image, parent, false)
            context = parent.context
            return ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ContentViewHolder) {
            holder.notice_content.text=noticeDataList?.get(position)?.getnType()

        }
        else if(holder is ImageViewHolder){
            if(noticeDataList?.get(position)?.geticnt()==0)
            {
                holder.notice_detailimage.visibility= GONE
            }
            else{
                for(i in 0 until noticeDataList?.get(position)?.geticnt()!!){

                }
            }
        }

    }

    //내용 뷰홀더
    class ContentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val notice_content=itemView.findViewById<TextView>(R.id.notice_content)
    }

    //이미지 뷰홀더
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val notice_detailimage=itemView.findViewById<ImageView>(R.id.notice_detailimage)
    }

    //원래 있는 함수 우리가 준 Viewtype을 똑바로 들고 오기 위해서
    override fun getItemViewType(position: Int): Int {
        return noticeDataList!![position].getViewType()!!
    }

}