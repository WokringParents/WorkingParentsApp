package com.example.workingparents

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.currentComposer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private val TAG="NoticeAdapter"

class NoticeAdapter(val noticeList: ArrayList<Notice>, val context: Context) : RecyclerView.Adapter<NoticeAdapter.CustomViewHolder>(){

    var noticeDataList : ArrayList<Notice>? = noticeList

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
        val content = itemView.findViewById<TextView>(R.id.content)
        val picture = itemView.findViewById<ImageView>(R.id.picture)

    }
    override fun getItemCount(): Int {return noticeList.size}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.CustomViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.notice_picture_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val notice : Notice = noticeDataList!!.get(curPos)
            }
        }
    }

    override fun onBindViewHolder(holder: NoticeAdapter.CustomViewHolder, position: Int) {
        holder.title.text=noticeDataList!!.get(position).ntitle
        holder.content.text=noticeDataList!!.get(position).ncontent

        RetrofitBuilder.api.loadFilebyName(noticeDataList!!.get(position).image).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful){
                    val byteArray : ByteArray? = response.body()?.bytes()
//                    val matrix = Matrix()
//                    matrix.postRotate(90F)
                    if(byteArray!=null){
                        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//                        if (bmp.height < bmp.width) {
//                            holder.picture.setImageBitmap(Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix,true));
//                        }else {holder.picture.setImageBitmap(bmp)}
//                        Log.d(TAG, "onResponse: loadFilebyName 성공")
                        Glide.with(context).load(bmp)
                            .override(400,300)
                            .into(holder.picture)
                    }
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

}

