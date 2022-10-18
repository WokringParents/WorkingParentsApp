package com.example.workingparents

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private val TAG="MA"

class MultiImageAdapter(private val items:ArrayList<Uri>, val context: Context) : RecyclerView.Adapter<MultiImageAdapter.ViewHolder>(){

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.notice_image,parent,false)
        return ViewHolder(inflateView)
    }

    override fun onBindViewHolder(holder: MultiImageAdapter.ViewHolder, position: Int) {

        val item = items[position]
        Glide.with(context).load(item)
            .override(400,300)
            .into(holder.image)

        holder.deletebutton.setOnClickListener {
            Log.d(TAG,position.toString()+"번째 삭제 버튼 눌림")
            deleteImageAdapter(position)
        }
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        private  var view : View= v
        var image = v.findViewById<ImageView>(R.id.notice_image)
        var deletebutton = v.findViewById<ImageView>(R.id.deleteimage)

        fun bind(listener:View.OnClickListener, item:String){
            view.setOnClickListener(listener)
        }

    }

}

