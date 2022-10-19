package com.example.workingparents

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

private val TAG = "NDAdapter"


class NoticeDetailAdapter(noticeDatalist: ArrayList<NoticeData>,var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var detailData : ArrayList<NoticeData>? = noticeDatalist
    val type : String="Notice"
    override fun getItemCount(): Int {return detailData!!.size}
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
            holder.notice_content.text=detailData?.get(position)?.getnType()
        }
        else if(holder is ImageViewHolder){
            RetrofitBuilder.api.loadFilebyName(type,detailData!!.get(position).getnType()!!).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful){
                        val byteArray : ByteArray? = response.body()?.bytes()
                        if(byteArray!=null) {
                            val bmp =
                                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                            Log.d(TAG, "onResponse: loadFilebyName 성공")
                            Glide.with(context).load(bmp)
                                .override(holder.notice_detailimage.width, bmp.height)
                                .into(holder.notice_detailimage)
                            Log.d(TAG, "onFailure: loadFilebyName 성공")
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "onFailure: loadFilebyName 실패"+ t.message)
                }
            })

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
        return detailData!![position].getViewType()!!
    }
}