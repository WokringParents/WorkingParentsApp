package com.example.workingparents

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "CmentAdapter"

class CommentAdapter(cmentDataList: ArrayList<Dataitem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var cmentDataList : ArrayList<Dataitem>? = cmentDataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.comment,parent,false)
            return CommentViewHolder(view)
        } else {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.ccomment,parent,false)
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


        //대댓글 꾹 눌렀을 때 본인 글이면 대댓글 삭제하기
        holder.itemView.setOnLongClickListener { v ->
            false
        }

    }

    override fun getItemCount(): Int {
        return cmentDataList!!.size
    }

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cid=itemView.findViewById<TextView>(R.id.cid)
        val ctime=itemView.findViewById<TextView>(R.id.ctime)
        val ccontent=itemView.findViewById<TextView>(R.id.ccontent)
    }

    class CcommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ccid=itemView.findViewById<TextView>(R.id.ccid)
        val cctime=itemView.findViewById<TextView>(R.id.cctime)
        val cccontent=itemView.findViewById<TextView>(R.id.cccontent)
    }



    fun insertCcmentOnCment(pno: Int, cno: Int, ccid: String, ccment: String, position: Int) {

        //cccnt올리기 ccnt올리기
        //DB쪽 - 대댓글테이블에 댓글 올리기, 댓글테이블에 대댓글 수 증가. 포스팅테이블에 댓글 수 증가
        //DataList쪽 - 대댓글 달려고 한 댓글 posotion 뒤에 대댓글 추가해서 리사이클러뷰에 데이터리스트 변화 notify. 이때 기존 대댓글이 있다면 가장 뒤에 추가

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

    }


    //원래 있는 함수 우리가 준 Viewtype을 똑바로 들고 오기 위해서
    override fun getItemViewType(position: Int): Int {
        return cmentDataList!![position].getViewType()
    }

}