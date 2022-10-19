package com.example.workingparents

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CafeteriaInnerAdapter(context: Context,cafeData: CafeteriaByDate) : RecyclerView.Adapter<CafeteriaInnerAdapter.ViewHolder>() {

    private val context=context
    val cafeData : CafeteriaByDate =cafeData
    val TAG="Cafeteria"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CafeteriaInnerAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cafeteria_content_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CafeteriaInnerAdapter.ViewHolder, position: Int) {
        val imageByte : ByteArray? = cafeData.imageBytes[position]
        val content: String? = cafeData.contents[position]
        var type: String?=null

       when(position){
           0-> type="오전간식"
           1-> type="점심"
           2-> type="오후간식"
           3-> type="저녁"
       }
        val bmp = BitmapFactory.decodeByteArray(imageByte, 0, imageByte!!.size)
        Glide.with(context).load(bmp)
            .override(holder.imageV.width,holder.imageV.height)
            .centerCrop()
            .into(holder.imageV)

        holder.contetTV.text=content
        holder.typeTV.text= type

    }

    override fun getItemCount(): Int { return cafeData.images.size }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageV = itemView.findViewById<ImageView>(R.id.cafe_item_image)
        val typeTV = itemView.findViewById<TextView>(R.id.cafe_item_ctype)
        val contetTV = itemView.findViewById<TextView>(R.id.cafe_item_content)

    }


}