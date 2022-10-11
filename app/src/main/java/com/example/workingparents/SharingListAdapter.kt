package com.example.workingparents


import android.content.Context
import android.os.Build
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
import com.example.workingparents.ChildCaringFragment.Companion.todoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


class SharingListAdapter(private val context: Context) :
    RecyclerView.Adapter<SharingListAdapter.ViewHolder>() {

    val TAG = "ChildCaring"
    var todoData = mutableListOf<SharingList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.sharing_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = todoData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(!todoData[position].inputMode) {

            holder.bindForRead(todoData[position],position)

            if (UserData.sex == "F") {
                holder.femaleDoBtn.setOnClickListener(View.OnClickListener {
                    Log.d(TAG, "여자 수행완료 버튼클릭됨" + todoData[position].fdo)

                    if (!todoData[position].fdo) {
                        //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                        Toast.makeText(context, "엄마 수행완료:)", Toast.LENGTH_SHORT).show()
                        todoData[position].fdo = true
                        holder.bindForRead(todoData[position],position)
                        completeTodo(todoData[position], "F")
                    }

                })
            } else {
                holder.maleDoBtn.setOnClickListener(View.OnClickListener {
                    Log.d(TAG, "남자 수행완료 버튼클릭됨" + todoData[position].mdo)

                    if (!todoData[position].mdo) {
                        //수행완료 보여주고 그림?으로도 괜찮을듯 지그재그에서 즐겨찾기 추가하면 하트뜨는 것처럼
                        Toast.makeText(context, "아빠 수행완료:)", Toast.LENGTH_SHORT).show()
                        todoData[position].mdo = true
                        holder.bindForRead(todoData[position],position)
                        completeTodo(todoData[position], "M")
                    }
                })
            }

        }else{
            //입력모드라면
            holder.bindForWrite(todoData[position],position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val view = view

        val contentTV: TextView = itemView.findViewById(R.id.sharingContentTV)
        val femaleDoBtn: ImageButton = itemView.findViewById(R.id.femaleDoBtn)
        val maleDoBtn: ImageButton = itemView.findViewById(R.id.maleDoBtn)
        val contentET: EditText = itemView.findViewById(R.id.sharingContentEditTxt)

        fun bindForRead(item: SharingList, pos:Int) {

            contentTV.visibility=View.VISIBLE
            contentET.visibility=View.GONE
            contentTV.text = item.content

            //Log.d(TAG, "fdo: "+ item.fdo.toString()+ " mdo: "+ item.mdo.toString())
            if (item.fdo) {
                femaleDoBtn.setImageResource(R.drawable.pink_mom_complete)
            }else{
                femaleDoBtn.setImageResource(R.drawable.gray_mom_incomplete)
            }
            if (item.mdo) {
                maleDoBtn.setImageResource(R.drawable.blue_dad_complete)
            }else{
                maleDoBtn.setImageResource(R.drawable.gray_dad_incomplete)
            }

            contentTV.isClickable=true
            contentTV.setOnClickListener(View.OnClickListener {

              //  Toast.makeText(view.context,"content: " + item.content , Toast.LENGTH_SHORT).show()
                val bottomSheet = BottomSheetSharingList( content = item.content) {
                    when (it) {
                        //확인해보기
                        0 ->{
                            bindForModify(item)
                        }

                        1 -> {
                            Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                            var startDate :String? = null
                            var endDate: String?= null

                            if(item.daily) {
                                //데일리 일때

                                startDate = todoList.find { it.content == item.content }?.sdate.toString()
                                endDate = todoList.findLast { it.content == item.content }?.sdate.toString()

                                /*  val startPos= todoData.indexOf(todoData.find{it.content==item.content})
                                  var cnt=0

                                  todoData.filter{it.content==item.content}.forEach{
                                      it.content=contentET.text.toString()
                                      it.inputMode=false
                                      cnt++}

                                  notifyItemRangeChanged(startPos,cnt)
                                */
                            }

                            Log.d(TAG,"pos:"+ pos.toString() + " "+item.content +"-> index:"+ todoData.indexOf(item))

                            todoData.removeAt(todoData.indexOf(item))
                            notifyItemRemoved(todoData.indexOf(item))

                            Log.d(TAG,"------------삭제시 변경전 todolist-------------")

                            for(x: SharingList in todoList){
                                Log.d(TAG,x.content)
                            }

                           // val filter= Predicate<SharingList> { todo -> todo.couplenum==item.couplenum && todo.sdate==item.sdate }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                todoList.removeIf { it.content==item.content && it.daily==item.daily}
                                Log.d(TAG,"Fragment쪽 해당 toDoList삭제")
                            }

                            for(x: SharingList in todoList){
                                Log.d(TAG,x.content)
                            }

                            deleteSharingList(item,startDate,endDate)  //DB에 변경해준다
                     }
                    }
                }
                bottomSheet.show((context as MainActivity).supportFragmentManager,bottomSheet.tag)
            })
        }

        fun bindForModify(item: SharingList){

            val prevContent= item.content

            contentTV.visibility=View.GONE
            contentET.visibility=View.VISIBLE

            contentET.setText(prevContent)
            contentET.requestFocus()

            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)


            contentET.imeOptions= EditorInfo.IME_ACTION_DONE
            contentET.setOnEditorActionListener{ textView, action, event ->
                var handled = false
                if (action == EditorInfo.IME_ACTION_DONE) {

                    contentET.visibility=View.INVISIBLE //입력란은 없어지고

                    if(contentET.text.toString()=="" || contentET.text.toString()==item.content){
                        //아무것도 입력하지 않았거나 기존내용과 같은데 입력완료 버튼을 눌렀다. --> 입력이 취소되어야한다.
                        Log.d(TAG,"수정중인데 아무것도 입력을 안하면 기존의 내용으로 가만히둠")
                        contentTV.setText(prevContent)
                        todoData[todoData.indexOf(item)].inputMode=false
                        notifyItemChanged(todoData.indexOf(item))
                    }
                    else
                    {
                        var startDate: String? = null
                        var endDate: String? = null

                        for(x: SharingList in todoList){
                            Log.d(TAG,x.content)
                        }

                        if(item.daily){
                        //데일리 일때

                            startDate= todoList.find{it.content==prevContent}?.sdate.toString()
                            endDate =todoList.findLast { it.content==prevContent}?.sdate.toString()

                            Log.d(TAG,"startDate"+startDate+"endDate"+endDate)

                          /*  val startPos= todoData.indexOf(todoData.find{it.content==item.content})
                            var cnt=0

                            todoData.filter{it.content==item.content}.forEach{
                                it.content=contentET.text.toString()
                                it.inputMode=false
                                cnt++}

                            notifyItemRangeChanged(startPos,cnt)
                          */
                        }

                        Log.d(TAG,"------------수정시 변경후 todolist-------------")

                        //fragment쪽에 있는 투두리스트데이터의 content를 모두 수정
                        todoList.filter { it.content == prevContent && it.daily==item.daily }.forEach {
                            it.content = contentET.text.toString()
                            Log.d(TAG,it.content + " " + prevContent)}

                        for(x: SharingList in todoList){
                            Log.d(TAG,x.content)
                        }

                        //사용자가 변경된 내용을 입력한 상태
                        todoData[todoData.indexOf(item)].inputMode=false
                        todoData[todoData.indexOf(item)].content=contentET.text.toString()  //여기서 바뀌는구나
                        notifyItemChanged(todoData.indexOf(item))

                        updateSharingList(todoData[todoData.indexOf(item)],prevContent,startDate,endDate)  //DB에 변경해준다

                    }

                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) //키보드 내러줌

                    handled = true
                }
                handled
            }
        }



        fun bindForWrite(item: SharingList, pos: Int){

            contentTV.visibility=View.GONE
            contentET.visibility=View.VISIBLE

            femaleDoBtn.setImageResource(R.drawable.gray_mom_incomplete)
            maleDoBtn.setImageResource(R.drawable.gray_dad_incomplete)

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
                        todoData.removeAt(pos)
                        notifyDataSetChanged()

                    }else{
                        //사용자가 무언가를 입력한 상태

                        todoData[pos].inputMode=false
                        todoData[pos].content=contentET.text.toString()

                        insertSharingList(todoData[pos])  //DB에 올려준다 !!

                        if(item.daily){

                            for(i in 0..6){
                                if(i>=clickedDayOfWeek){

                                    Log.d(TAG,"오늘자 todo"+item.sdate)
                                    val dateDiff= i- clickedDayOfWeek
                                    Log.d(TAG,i.toString() + " " + clickedDayOfWeek.toString())

                                    /*
                                    var strBuilder = StringBuilder(item.sdate.toString().substring(0,8))
                                    strBuilder.append(item.sdate.date +dateDiff)
                                    strBuilder.append(item.sdate.toString().substring(10))
                                    */

                                    val nextDate = Timestamp.valueOf(item.sdate.toString())
                                    nextDate.date+=dateDiff
                                    //그렇다 꼭 새로 만들어줘야한다.
                                    //toDoList.add(SharingList(item.couplenum,Timestamp.valueOf(strBuilder.toString()),item.content, false,false,i,true))
                                    todoList.add(SharingList(item.couplenum,nextDate,item.content, false,false,i,true))

                                    Log.d(TAG,"결과" +todoList[todoList.size-1])
                                }
                            }
                        }else { //today일 때
                            todoList.add(todoData[pos])
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
            RetrofitBuilder.api.putFemaleDo(todo.couplenum, todo.sdate.toString()).enqueue(object :Callback<Int>{
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
               RetrofitBuilder.api.putMaleDo(todo.couplenum, todo.sdate.toString()).enqueue(object :Callback<Int>{
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


    fun updateSharingList(todo:SharingList, prevContent: String?, startDate:String?, endDate:String?){

      if(todo.daily && startDate!=null && endDate!=null && prevContent!=null){

            RetrofitBuilder.api.putDailyContent(todo.couplenum,prevContent,todo.content, startDate, endDate).enqueue(object:Callback<Int>{

                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        val result: Int? = response.body()
                        if (result != null) {
                            if(result>=1)
                                Log.d(TAG, "onResponse: putDailyContent 성공 "+ result +"개")
                        }
                    }
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: putDailyContent 실패 ")
                }
            })

        }
        else{
            RetrofitBuilder.api.putTodayContent(todo.couplenum,todo.sdate.toString(),todo.content).enqueue(object: Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        val result: Int? = response.body()

                        if (result != null) {
                            if(result==1)
                                Log.d(TAG, "onResponse: putTodayContent 성공 ")
                        }

                    }
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: putTodayContent 실패 ")
                }

            })
        }

    }

    fun deleteSharingList(todo:SharingList,startDate:String?, endDate:String?){

        if(startDate!=null && endDate!=null && todo.daily){

            RetrofitBuilder.api.deleteDailySharingList(todo.couplenum,todo.content,startDate,endDate).enqueue(object :Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        val result: Int? = response.body()
                        if (result != null) {
                           if(result>=1)
                                Log.d(TAG, "onResponse: deleteDailySharingList 성공 "+result +"개")
                        }
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: deleteDailySharingList 실패 ")
                }

            })
        }else{
            RetrofitBuilder.api.deleteTodaySharingList(todo.couplenum,todo.sdate.toString()).enqueue(object: Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        val result: Int? = response.body()
                        if(result==1)
                            Log.d(TAG, "onResponse: deleteSharingList 성공 ")
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d(TAG, "onFailure: deleteSharingList 실패 ")
                }

            })
        }
    }
}