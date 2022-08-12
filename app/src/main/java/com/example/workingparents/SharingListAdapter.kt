package com.example.workingparents


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SharingListAdapter(private val context: Context) : RecyclerView.Adapter<SharingListAdapter.ViewHolder>() {

    var datas = mutableListOf<SharingList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sharing_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val sharingContentTV: TextView = itemView.findViewById(R.id.sharingContentTV)
        private val femaleDoBtn: ImageButton = itemView.findViewById(R.id.femaleDoBtn)
        private val maleDoBtn: ImageButton = itemView.findViewById(R.id.maleDoBtn)


        fun bind(item: SharingList) {
            sharingContentTV.text = item.content
            if(item.fdo==1) {
                femaleDoBtn.setImageResource(R.drawable.redcircle)
            }
            if(item.mdo==1) {
                maleDoBtn.setImageResource(R.drawable.bluecircle)
            }
        }

    }


}