package com.example.workingparents

import android.app.AlertDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "CmentAdapter"
private var context: Context? = null
//굳이 context만드는 이유는.. 다른 함수에서 this가 표시하는 곳이 postingActivtiy인걸 알려주기 위해서..

class CommentAdapter(cmentDataList: ArrayList<Dataitem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var cmentDataList : ArrayList<Dataitem>? = cmentDataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.comment,parent,false)
            context = parent.context
            return CommentViewHolder(view)
        } else {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.ccomment,parent,false)
            context = parent.context
            return CcommentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CommentViewHolder) {
            holder.cid.text= cmentDataList?.get(position)?.getComment()?.cid
            holder.ccontent.text=cmentDataList?.get(position)?.getComment()?.cment.toString()


            //ctime 알려주는 함수
            //현재시간을 YY-MM-DD 00:00 으로 자른다
            var comment_time : String = cmentDataList?.get(position)?.getComment()?.cdate.toString()
            val stringBuilder = StringBuilder()
            stringBuilder.append(comment_time.substring(2,4))
            stringBuilder.append("/")
            stringBuilder.append(comment_time.substring(5,7))
            stringBuilder.append("/")
            stringBuilder.append(comment_time.substring(8,10))
            stringBuilder.append(" ")
            stringBuilder.append(comment_time.substring(11,16))

            var ctimecheck : String =stringBuilder.toString()

            Log.d(TAG, "초기" + ctimecheck)
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")) //시간을 가져와서
            val sdf = SimpleDateFormat("yy/MM/dd HH:mm") //이형태로 바꿈
            val nowTime = sdf.format(System.currentTimeMillis())

            var sameTime = true
            for (i in 0..8) { //0~8만을 비교하는 이유는 yy-mm-dd 까지 비교문을 돌려서 비교
                if (ctimecheck.get(i) != nowTime[i]) {
                    sameTime = false
                }
            } //03:01 02:01    03-02= 1    61-08= 53     1시간전 까지만 해준다.

            if (sameTime) {
                //여전히 true 라면 같은 날
                // 같은 1시간차이안에 있는지 체크        --> 몇분전인지 체크해서 출력
                val hourDifference: Int = (nowTime.substring(9,11)).toInt() - (ctimecheck.substring(9,11)).toInt()
                Log.d(TAG, "hourDifference 결과"+nowTime.substring(9,11)+"-"+ctimecheck.substring(9,11)+"="+hourDifference.toString())
                if (hourDifference == 0) {
                    //같은 시간대라면
                    val minuteDifference: Int =
                        nowTime.substring(12).toInt() - ctimecheck.substring(12).toInt()
                    if (minuteDifference == 0) {
                        holder.ctime.text= "방금 전"
                        postingTime="방금 전"
                        Log.d(TAG, "방금 전에 들어옴")
                    } else {
                        holder.ctime.text=minuteDifference.toString() + "분 전"

                    }
                } else if (hourDifference == 1) {

                    //시간대에는 차이가있지만 1시간보다 더 경과한 시간차가 아닌경우
                    val nowMinute = nowTime.substring(12,14).toInt() + 60
                    val minuteDifference: Int = nowMinute - ctimecheck.substring(12,14).toInt()
                    if (minuteDifference <= 60) {  //한시간 이내인데
                        if (minuteDifference == 60) {
                            holder.ctime.text="1시간 전"

                        } else {
                            holder.ctime.text=minuteDifference.toString() + "분 전"

                        }
                    }
                }
                else
                {
                    holder.ctime.text=hourDifference.toString()+"시간 전"
                    Log.d(TAG,"0도 1 도 아님")

                }
            } else {
                holder.ctime.text=ctimecheck.substring(3)
                Log.d(TAG,"같은 날 아님")
            }

            val comment = cmentDataList!![position].getComment()

            //더보기 버튼 누르면
            holder.cbtn.setOnClickListener(View.OnClickListener {
                Log.d(TAG, "cbtn 눌림")
                Log.d(TAG, "지금 위치"+position.toString())

                val list: Array<String>
                list = if (comment!!.cid.equals(UserData.id)) {
                    arrayOf("대댓글 작성", "삭제")
                } else {
                    arrayOf("대댓글 작성")
                }

                val listbuilder=AlertDialog.Builder(context)
                    .setItems(list, DialogInterface.OnClickListener { dialog, which ->
                        if(which==0){
                            val builder = AlertDialog.Builder(context)
                                .setMessage("대댓글을 작성하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{ dialog, which ->

                                        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                        //댓글달기 클릭시 키보드+입력창 올라옴
                                            PostingActivity.input.post(Runnable {
                                                PostingActivity.input.setFocusableInTouchMode(true)
                                                PostingActivity.input.requestFocus()
                                                imm.showSoftInput(PostingActivity.input, 0)
                                            })

                                        PostingActivity.sendbtn.setOnClickListener {
                                            var ccment = PostingActivity.input.text.toString()
                                            insertCcmentOnCment(PostingActivity.pno,comment.cno,UserData.id,ccment,position)
                                            PostingActivity.input.setText(null)
                                            imm.hideSoftInputFromWindow(PostingActivity.input.windowToken,0)

                                        }
                                    })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                                    })
                            builder.show()
                        }
                        else if(which==1){
                            val builder = AlertDialog.Builder(context)
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{ dialog, which ->
                                        deleteCmentOnPosting(comment!!.pno, comment!!.cno, comment!!.cccnt, position)
                                        Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show()
                                    })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                                    })
                            builder.show()
                        }

                    } )
                listbuilder.show()

                /*
                //bottomsheet으로 대댓글달기, 삭제
                val bottomSheet = BottomSheetCment() {
                    when (it) {
                        //확인해보기
                        0 -> {
                            Toast.makeText(context, "대댓글", Toast.LENGTH_SHORT).show()
                        }

                        1 -> {

                            val builder = AlertDialog.Builder(context)
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{ dialog, which ->
                                        deleteCmentOnPosting(comment!!.pno, comment!!.cno, comment!!.cccnt, position)
                                        Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show()
                                    })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                                    })
                            builder.show()
                        }
                    }
                }
                bottomSheet.show((context as PostingActivity).supportFragmentManager,bottomSheet.tag)

                 */
            })

        }

        else if(holder is CcommentViewHolder){

            holder.ccid.text=cmentDataList?.get(position)?.getCcomment()?.ccid
            holder.cccontent.text=cmentDataList?.get(position)?.getCcomment()?.ccment.toString()

            var ccomment_time : String = cmentDataList?.get(position)?.getCcomment()?.ccdate.toString()
            val stringBuilder = StringBuilder()
            stringBuilder.append(ccomment_time.substring(2,4))
            stringBuilder.append("/")
            stringBuilder.append(ccomment_time.substring(5,7))
            stringBuilder.append("/")
            stringBuilder.append(ccomment_time.substring(8,10))
            stringBuilder.append(" ")
            stringBuilder.append(ccomment_time.substring(11,16))

            var cctimecheck : String =stringBuilder.toString()

            Log.d(TAG, "초기" + cctimecheck)
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")) //시간을 가져와서
            val sdf = SimpleDateFormat("yy/MM/dd HH:mm") //이형태로 바꿈
            val nowTime = sdf.format(System.currentTimeMillis())

            var sameTime = true
            for (i in 0..8) { //0~8만을 비교하는 이유는 yy-mm-dd 까지 비교문을 돌려서 비교
                if (cctimecheck.get(i) != nowTime[i]) {
                    sameTime = false
                }
            } //03:01 02:01    03-02= 1    61-08= 53     1시간전 까지만 해준다.

            if (sameTime) {
                //여전히 true 라면 같은 날
                // 같은 1시간차이안에 있는지 체크        --> 몇분전인지 체크해서 출력
                val hourDifference: Int = (nowTime.substring(9,11)).toInt() - (cctimecheck.substring(9,11)).toInt()
                Log.d(TAG, "hourDifference 결과"+nowTime.substring(9,11)+"-"+cctimecheck.substring(9,11)+"="+hourDifference.toString())
                if (hourDifference == 0) {
                    //같은 시간대라면
                    val minuteDifference: Int =
                        nowTime.substring(12).toInt() - cctimecheck.substring(12).toInt()
                    if (minuteDifference == 0) {
                        holder.cctime.text= "방금 전"
                        postingTime="방금 전"
                        Log.d(TAG, "방금 전에 들어옴")
                    } else {
                        holder.cctime.text=minuteDifference.toString() + "분 전"

                    }
                } else if (hourDifference == 1) {

                    //시간대에는 차이가있지만 1시간보다 더 경과한 시간차가 아닌경우
                    val nowMinute = nowTime.substring(12,14).toInt() + 60
                    val minuteDifference: Int = nowMinute - cctimecheck.substring(12,14).toInt()
                    if (minuteDifference <= 60) {  //한시간 이내인데
                        if (minuteDifference == 60) {
                            holder.cctime.text="1시간 전"

                        } else {
                            holder.cctime.text=minuteDifference.toString() + "분 전"

                        }
                    }
                }
                else
                {
                    holder.cctime.text=hourDifference.toString()+"시간 전"
                    Log.d(TAG,"0도 1 도 아님")

                }
            } else {
                holder.cctime.text=cctimecheck.substring(3)
                Log.d(TAG,"같은 날 아님")
            }

        }


    }

    override fun getItemCount(): Int {
        return cmentDataList!!.size
    }

    //댓글 뷰홀더
    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cbtn=itemView.findViewById<ImageButton>(R.id.cbtn)
        val cid=itemView.findViewById<TextView>(R.id.cid)
        val ctime=itemView.findViewById<TextView>(R.id.ctime)
        val ccontent=itemView.findViewById<TextView>(R.id.ccontent)
    }

    //대댓글 뷰홀더
    class CcommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ccid=itemView.findViewById<TextView>(R.id.ccid)
        val cctime=itemView.findViewById<TextView>(R.id.cctime)
        val cccontent=itemView.findViewById<TextView>(R.id.cccontent)
    }


    //댓글 삭제 함수
    fun deleteCmentOnPosting(pno: Int, cno: Int, cccnt: Int, position: Int) {
        if (cccnt > 0) {
            //댓글의 position 뒤에 대댓글 개수만큼 삭제해준다.
            for (i in 1..cccnt) {
                Log.d("tag", "대댓글발견" + cmentDataList!![position + 1].getCcomment()!!.ccment.toString() + "ccno삭제함")
                //ccnt값도 내려주기
                updateCmentCnt(pno, "minus")
                cmentDataList!!.removeAt(position + 1)
            }
        }
        cmentDataList!!.removeAt(position)
        notifyDataSetChanged()

        RetrofitBuilder.api.deleteComment(pno, cno).enqueue(object : Callback<Int> {
            //Comment DB에서 삭제임 (delete on Cascade라 DB의 대댓글에선 작동으로 삭제됨)
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result!!.toInt() == 1) {
                        Log.d("tag", "댓글삭제성공\n")
                        //ccnt값도 내려주기
                        updateCmentCnt(pno, "minus")
                    }
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
            }

        })
    }


    fun insertCcmentOnCment(pno: Int, cno: Int, ccid: String, ccment: String, position: Int) {

        //cccnt올리기 ccnt올리기
        //DB쪽 - 대댓글테이블에 댓글 올리기, 댓글테이블에 대댓글 수 증가. 포스팅테이블에 댓글 수 증가
        //DataList쪽 - 대댓글 달려고 한 댓글 posotion 뒤에 대댓글 추가해서 리사이클러뷰에 데이터리스트 변화 notify. 이때 기존 대댓글이 있다면 가장 뒤에 추가

        Log.d(TAG,"대댓글 삽입 진입")

        //1) ccno 숫자 만들기
        var current_comment : Comment? = cmentDataList?.get(position)?.getComment()
        val cccnt: Int = current_comment!!.cccnt

        var ccno = 1
        if (cccnt == 0) {
            ccno = 1 //1로 그대로 둔다
        } else {
            for (i in 1..cccnt) {
                val ccomment: Ccomment? = cmentDataList?.get(position+i)?.getCcomment()
                if (ccno < ccomment!!.cno) {
                    ccno = ccomment.cno
                }
            }
            ccno++ //최댓값보다 1증가
        }

        RetrofitBuilder.api.postCcomment(pno,cno,ccno,ccid,ccment).enqueue(object : Callback<Ccomment> {
                override fun onResponse(call: Call<Ccomment>, response: Response<Ccomment>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "55555555555555")

                        var result: Ccomment? = response.body()
                        // 정상적으로 통신이 성공된 경우
                        Log.d(TAG, "onResponse: 댓글 성공" + result?.toString())


                        //2) 데이터리스트에 추가해주기
                        //대댓글 없는 댓글이라면 댓글바로 뒤에 추가        //대댓글 있는 댓글이라면 가장 뒤 대댓글 뒤에 추가
                        var dataitem= Dataitem()
                        dataitem.Ccommentitem(result,2)
                        cmentDataList!!.add(position + cccnt + 1,dataitem)


                        //3) ccnt, cccnt 값 1증가 업데이트 - DB
                        updateCmentCnt(pno, "plus")
                        updateCcmentCnt(pno, cno, "plus")

                        //3) cccnt 값 1증가 업데이트 - dataList
                        cmentDataList!![position].getComment()!!.cccnt++

                        //4) 리사이클러뷰 다시 구성
                        notifyItemRangeChanged(position + 1, cmentDataList!!.size)


                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    }
                }
                override fun onFailure(call: Call<Ccomment>, t: Throwable) {
                    Log.d(TAG, "onFailure 댓글 실패 : " + t.message.toString())
                }

            })

    }

    //원래 있는 함수 우리가 준 Viewtype을 똑바로 들고 오기 위해서
    override fun getItemViewType(position: Int): Int {
        return cmentDataList!![position].getViewType()
    }

}