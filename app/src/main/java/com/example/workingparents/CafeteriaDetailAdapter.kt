package com.example.workingparents

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class CafeteriaDetailAdapter(val context: Context, val cafeData:CafeteriaByDate):RecyclerView.Adapter<CafeteriaDetailAdapter.ViewHolder>() {


    val TAG="CafeteriaDetail"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CafeteriaDetailAdapter.ViewHolder {
        Log.d(TAG,"onCreateViewHolder")

        val view = LayoutInflater.from(context).inflate(R.layout.cafeteria_detail_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageV = itemView.findViewById<ImageView>(R.id.cafe_detail_item_image)
        val typeTV= itemView.findViewById<TextView>(R.id.cafe_detailtypeTV)
        val contentTV= itemView.findViewById<TextView>(R.id.cafe_detailcontentTV)

    }

    override fun onBindViewHolder(holder: CafeteriaDetailAdapter.ViewHolder, position: Int) {

        Log.d(TAG,"onBindViewHolder")

        val sortMap = cafeData.contents.toSortedMap()
        var i= 0
        var keyType :Int =-1

        for((key,value) in sortMap){
            if(i==position){
                keyType=key
                break
            }
            i++
        }

        val imageByte : ByteArray? = cafeData.imageBytes[keyType]
        val content: String? = cafeData.contents[keyType]
        var type: String?=null

        when(keyType){
            0-> type="오전간식"
            1-> type="점심"
            2-> type="오후간식"
            3-> type="저녁"
        }

        val bmp = BitmapFactory.decodeByteArray(imageByte, 0, imageByte!!.size)

        Glide.with(context).load(bmp)
            .override(holder.imageV.width,bmp.height)
            .centerCrop()
            .into(holder.imageV)

        holder.contentTV.text=content
        holder.typeTV.text= type

    }

    override fun getItemCount(): Int {
        return cafeData.images.size
    }


}