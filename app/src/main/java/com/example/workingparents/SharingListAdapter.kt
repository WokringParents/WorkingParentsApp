package com.example.workingparents


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SharingListAdapter(private val context: Context) :
    RecyclerView.Adapter<SharingListAdapter.ViewHolder>() {

    val TAG = "ChildCaring"
    var datas = mutableListOf<SharingList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sharing_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

        if (UserData.sex == "F") {
            holder.femaleDoBtn.setOnClickListener(View.OnClickListener {
                Log.d(TAG, "여자 수행완료 버튼클릭됨" + datas[position].fdo)

                if(!datas[position].fdo){
                    //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                    Toast.makeText(context,"엄마 수행완료:)",Toast.LENGTH_SHORT).show()
                    datas[position].fdo=true
                    holder.bind(datas[position])
                    completeTodo(datas[position],"F")


                }

            })
        } else {
            holder.maleDoBtn.setOnClickListener(View.OnClickListener {
                Log.d(TAG, "남자 수행완료 버튼클릭됨" + datas[position].mdo)

                if(!datas[position].mdo){
                    //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                    Toast.makeText(context,"아빠 수행완료:)",Toast.LENGTH_SHORT).show()
                    datas[position].mdo=true
                    holder.bind(datas[position])
                    completeTodo(datas[position],"M")
                }
            })
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val sharingContentTV: TextView = itemView.findViewById(R.id.sharingContentTV)
        val femaleDoBtn: ImageButton = itemView.findViewById(R.id.femaleDoBtn)
        val maleDoBtn: ImageButton = itemView.findViewById(R.id.maleDoBtn)
        val moreBtn: ImageButton = itemView.findViewById(R.id.sharingListMoreBtn)

        fun bind(item: SharingList) {

            sharingContentTV.text = item.content
            //Log.d(TAG, "fdo: "+ item.fdo.toString()+ " mdo: "+ item.mdo.toString())
            if (item.fdo) {
                femaleDoBtn.setImageResource(R.drawable.redcircle)
            }else{
                femaleDoBtn.setImageResource(R.drawable.graycircle)
            }
            if (item.mdo) {
                maleDoBtn.setImageResource(R.drawable.bluecircle)
            }else{
                maleDoBtn.setImageResource(R.drawable.graycircle)
            }
        }

    }

    fun completeTodo(todo: SharingList ,sex: String){

        if(sex=="F") {
            RetrofitBuilder.api.putFemaleDo(todo.couplenum, todo.sdate).enqueue(object :Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if(response.isSuccessful){
                        val result: Int? = response.body()
                        if(result==1) {
                            Log.d(TAG, "onResponse: putFemaleDo 성공 ")
                        }
                    }else{
                        Log.d(TAG, "onResponse: putFemaleDo 실패 ")
                    }
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: putFemaleDo 실패 ")
                }
            })
        }else if(sex=="M"){

            RetrofitBuilder.api.putMaleDo(todo.couplenum, todo.sdate).enqueue(object :Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if(response.isSuccessful){
                        val result: Int? = response.body()
                        if(result==1) {
                            Log.d(TAG, "onResponse: putMaleDo 성공 ")
                        }
                    }else{
                        Log.d(TAG, "onResponse: putMaleDo 실패 ")
                    }
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: putMaleDo 실패 ")
                }
            })
        }

    }
}