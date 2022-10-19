package com.example.workingparents

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CafeteriaOuterAdapter(private val context: Context, val cafeData:MutableList<CafeteriaByDate>) : RecyclerView.Adapter<CafeteriaOuterAdapter.ViewHolder>() {

    val TAG="Cafeteria"
    //var cafeData= mutableListOf<CafeteriaByDate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaOuterAdapter.ViewHolder {
        Log.d(TAG,"onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cafeteria_date_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cafeData.size
    }

    override fun onBindViewHolder(holder: CafeteriaOuterAdapter.ViewHolder, position: Int) {
            Log.d(TAG,"onBindViewHolder")
            val item: CafeteriaByDate= cafeData[position]
            val dateArray = item.cdate.split("-").toTypedArray()   //2022-10-19
            holder.dateTV.text=dateArray[0]+" 년"+ dateArray[1]+"월"+dateArray[2]+"일"
            holder.innerRecyclerView.layoutManager= holder.innerManager
            Log.d(TAG,cafeData[position].images.toString())

            val imageBytes : MutableMap<Int,ByteArray> = mutableMapOf()

            for((type,image) in item.images){

                RetrofitBuilder.api.loadFilebyName(TAG,image).enqueue(object:
                    retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        if(response.isSuccessful){

                            Log.d(TAG, "onResponse: loadFilebyName 성공")

                            val byteArray : ByteArray? = response.body()?.bytes()
                            if(byteArray!=null){
                                imageBytes.put(type,byteArray)
                                item.imageBytes= imageBytes
                                if(imageBytes.size==item.images.size){
                                    val innerAdapter=CafeteriaInnerAdapter(context,item)
                                    holder.innerRecyclerView.adapter=innerAdapter
                                }
                            }else{
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "onFailure: loadFilebyName 실패"+ t.message)
                    }
                })
            }

    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTV :TextView = itemView.findViewById(R.id.cafe_dateTV)
        val moreBtn: ImageButton = itemView.findViewById(R.id.cafe_moreBtn)
        val innerRecyclerView :RecyclerView= itemView.findViewById(R.id.cafeteria_inner_content_recyclerview)
        val innerManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }
}