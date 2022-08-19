package com.example.workingparents


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.ChildCaringFragment.Companion.clickedDayOfWeek
import com.example.workingparents.ChildCaringFragment.Companion.dateArr
import com.example.workingparents.ChildCaringFragment.Companion.toDoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


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


        if(!datas[position].inputMode) {

            holder.bindForRead(datas[position])

            if (UserData.sex == "F") {
                holder.femaleDoBtn.setOnClickListener(View.OnClickListener {
                    Log.d(TAG, "여자 수행완료 버튼클릭됨" + datas[position].fdo)

                    if (!datas[position].fdo) {
                        //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                        Toast.makeText(context, "엄마 수행완료:)", Toast.LENGTH_SHORT).show()
                        datas[position].fdo = true
                        holder.bindForRead(datas[position])
                        completeTodo(datas[position], "F")
                    }

                })
            } else {
                holder.maleDoBtn.setOnClickListener(View.OnClickListener {
                    Log.d(TAG, "남자 수행완료 버튼클릭됨" + datas[position].mdo)

                    if (!datas[position].mdo) {
                        //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                        Toast.makeText(context, "아빠 수행완료:)", Toast.LENGTH_SHORT).show()
                        datas[position].mdo = true
                        holder.bindForRead(datas[position])
                        completeTodo(datas[position], "M")
                    }
                })
            }

        }else{
            //입력모드라면
            holder.bindForWrite(datas[position],position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val view = view

        val contentTV: TextView = itemView.findViewById(R.id.sharingContentTV)
        val femaleDoBtn: ImageButton = itemView.findViewById(R.id.femaleDoBtn)
        val maleDoBtn: ImageButton = itemView.findViewById(R.id.maleDoBtn)
        val contentET: EditText = itemView.findViewById(R.id.sharingContentEditTxt)

        fun bindForRead(item: SharingList) {

            contentTV.visibility=View.VISIBLE
            contentET.visibility=View.GONE
            contentTV.text = item.content

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

            contentTV.isClickable=true
            contentTV.setOnClickListener(View.OnClickListener {

              //  Toast.makeText(view.context,"content: " + item.content , Toast.LENGTH_SHORT).show()
                val bottomSheet = BottomSheetFragment( content = item.content) {
                    when (it) {
                        //확인해보기
                        0 -> Toast.makeText(context, "수정", Toast.LENGTH_SHORT).show()

                        1 -> Toast.makeText(context, "삭제", Toast.LENGTH_SHORT).show()
                    }
                }
                bottomSheet.show((context as MainActivity).supportFragmentManager,bottomSheet.tag)

            })
        }


        fun bindForWrite(item: SharingList, pos: Int){

            contentTV.visibility=View.GONE
            contentET.visibility=View.VISIBLE

            femaleDoBtn.setImageResource(R.drawable.graycircle)
            maleDoBtn.setImageResource(R.drawable.graycircle)

            contentET.hint="입력"
            contentET.setText("")

            contentET.requestFocus()
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

            //무언가를 입력하고 나서 완료버튼 눌렀을 때
            contentET.imeOptions= EditorInfo.IME_ACTION_DONE
            contentET.setOnEditorActionListener{ textView, action, event ->
                var handled = false
                if (action == EditorInfo.IME_ACTION_DONE) {

                    contentET.visibility=View.INVISIBLE //입력란은 없어지고

                    if(contentET.text.toString()==""){
                        //아무것도 입력하지 않았는데 입력완료 버튼을 눌렀다. --> 입력이 취소되어야한다.
                        Log.d(TAG,"아무것도 입력안되었을때 확인용")
                        datas.removeAt(pos)
                        notifyDataSetChanged()

                    }else{
                        //사용자가 무언가를 입력한 상태

                        datas[pos].inputMode=false
                        datas[pos].content=contentET.text.toString()

                        insertSharingList(datas[pos])  //DB에 올려준다 !!
                        if(item.daily){

                            for(i in 0..6){
                                if(i>=clickedDayOfWeek){

                                    Log.d(TAG,"오늘자 todo"+item.sdate)
                                    val dateDiff= i- clickedDayOfWeek
                                    Log.d(TAG,i.toString() + " " + clickedDayOfWeek.toString())

                                    var strBuilder = StringBuilder(item.sdate.toString().substring(0,8))
                                    strBuilder.append(item.sdate.date +dateDiff)
                                    strBuilder.append(item.sdate.toString().substring(10))

                                    val nextDate = Timestamp.valueOf(item.sdate.toString())
                                    nextDate.date+=dateDiff
                                    //그렇다 꼭 새로 만들어줘야한다.
                                  //  toDoList.add(SharingList(item.couplenum,Timestamp.valueOf(strBuilder.toString()),item.content, false,false,i,true))
                                    toDoList.add(SharingList(item.couplenum,nextDate,item.content, false,false,i,true))

                                    Log.d(TAG,"결과" +toDoList[toDoList.size-1])
                                }
                            }
                        }else {
                            toDoList.add(datas[pos])

                        }

                        notifyItemChanged(pos)
                    }

                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) //키보드 내러줌

                    handled = true
                }
                handled
            }
        }

    }

    fun insertSharingList(todo:SharingList){

        var sdate=todo.sdate.toString()
        sdate= sdate.substring(0,sdate.length-2)

        RetrofitBuilder.api.postSharingList(todo.couplenum,sdate,todo.content,todo.daily, dateArr[6].toString()
        ).enqueue(object: Callback<Int>{

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.isSuccessful){
                    val result: Int? = response.body()
                    Log.d(TAG,result.toString())
                    if(result!! >= 1) {  //DailyList는 일요일이 아니라면 2행이상 post하니까
                        Log.d(TAG, "onResponse: PostSharingList 성공 ")
                    }
                }else{
                    Log.d(TAG, "onResponse: postSharingList 실패 ")
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure: postSharingList 실패 ")
            }

        })
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