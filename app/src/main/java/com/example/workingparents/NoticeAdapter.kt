package com.example.workingparents

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private val TAG="NoticeAdapter"

class NoticeAdapter(val noticeList: ArrayList<Notice>, var context: Context) : RecyclerView.Adapter<NoticeAdapter.CustomViewHolder>(){

    private var noticeDataList = noticeList

    val type : String="Notice"

    /*
    어뎁터 내에 상속된 두 메서드는 DiffUtill을 사용 시에 보통 비교를 통한 상태변화이기 때문에, 정확히 비교가 되지 않고, 중복될 가능성으로 인해
    아이템의 값이 유니크하지 않을 수 있다는 가능성이 하나, 그러므로, 고유한 아이디와 타입을 부여해주는 두 오버라이딩된 메서드로 고유한 포지션을 재정의하여
    해당 문제를 해결하였다.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
        val contentimage = itemView.findViewById<TextView>(R.id.content_image)
        val contentnoimage = itemView.findViewById<TextView>(R.id.content_noimage)
        val picture = itemView.findViewById<ImageView>(R.id.picture)
        val cardview = itemView.findViewById<CardView>(R.id.cardView_adapter)

    }
    override fun getItemCount(): Int {return noticeList.size}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.CustomViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.notice_picture_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val notice : Notice = noticeDataList!!.get(curPos)
                Log.d(TAG, notice.toString())
                Log.d(TAG, notice.nid.toString())
                context=parent.context
                val intent = Intent(context, NoticeActivity::class.java)
                intent.putExtra("rv_date",notice.ndate.toString())
                intent.putExtra("rv_title",notice.ntitle) //intent로 값 넘겨주기
                intent.putExtra("rv_content",notice.ncontent)
                intent.putExtra("rv_nid",notice.nid.toString())
                intent.putExtra("rv_tid",notice.tid.toString())
                intent.putExtra("rv_position",curPos.toString())
                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: NoticeAdapter.CustomViewHolder, position: Int) {
        holder.title.text=noticeDataList!!.get(position).ntitle
        var ncontent = noticeDataList!!.get(position).ncontent

        RetrofitBuilder.api.loadFilebyName(type,noticeDataList!!.get(position).image).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    val byteArray : ByteArray? = response.body()?.bytes()
                    if(byteArray!=null){
                        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        Log.d(TAG, "onResponse: loadFilebyName 성공")
                        Glide.with(context).load(bmp)
                            .override(holder.picture.width,holder.picture.height)
                            .centerCrop()
                            .into(holder.picture)
                    }
                    holder.contentnoimage.visibility= GONE
                    holder.contentimage.visibility= VISIBLE
                    holder.contentimage.text=ncontent
                }
                else{
                    Log.d(TAG, "onFailure: loadFilebyName 실패")
                    Log.d(TAG, "사진 없음!!!")
                    holder.picture.visibility=INVISIBLE
                    holder.cardview.visibility=INVISIBLE
                    holder.contentimage.visibility= GONE
                    holder.contentnoimage.visibility= VISIBLE
                    holder.contentnoimage.text=ncontent

                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: loadFilebyName 실패"+ t.message)
            }
        })

        var notice_time : String = noticeDataList!!.get(position).ndate.toString()
        val stringBuilder = StringBuilder()
        stringBuilder.append(notice_time.substring(5,7))
        stringBuilder.append("/")
        stringBuilder.append(notice_time.substring(8,10))
        holder.date.text=stringBuilder.toString()

    }

    //변경하기전 뷰 홀더 위치와 변경한 후의 뷰홀더 위치를 받아온다. 받아오고 그것을 반영한 새로운 데이터를 생성
    fun setData(newNoticeList : List<Notice> ){
        Log.d(TAG, "setData 불러짐")
        val diffUtil = DiffUtilCallback(noticeDataList!!, newNoticeList)
        //이 함수에서 areItemsTheSame을 먼저 비교하고, 결과에 따라 아이템 변경 사항을 처리
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        //noticeDataList=newNoticeList
        diffResults.dispatchUpdatesTo(this)
    }

}



