package com.example.workingparents

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

private lateinit var context: Context
private val TAG = "PostingAdapter"
lateinit var postingTime :String

class PostingAdapter(postingList: ArrayList<Posting>): RecyclerView.Adapter<PostingAdapter.CustomViewHolder>() {

    var postingDataList : ArrayList<Posting>? = postingList

    //class viewholder에서 잡아주고
    //bind viewholder에서 불러오고
    //oncreadteViewholder에서 생성한다
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    //activty의 oncreate랑 비슷함 만들어놓은 posting.xml을 보여줄거라고 해야함
    ): PostingAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posting,parent,false) //xml을 보여줄거라고 inflater을 사용함.
        //context란 acitivty에 있는 모든 정보를 말하는 것.여기의 parent.context는 adapter와 연결될 activity의 context
        //이렇게 선언하면 view는 posting을 끌고와서 adapter에 붙여주는 역할을 한다
        return CustomViewHolder(view).apply { //클릭 인식
            itemView.setOnClickListener{
                val curPos : Int =adapterPosition //현재 클릭한 리사이클러뷰 포지션
                var postings : Posting = postingDataList!!.get(curPos) //현재 position의 recylerview 값들
                context=parent.context
                val intent = Intent(context, PostingActivity::class.java)
                intent.putExtra("rv_pno",postings.pno.toString())
                intent.putExtra("rv_pid",postings.pid) //intent로 값 넘겨주기
                intent.putExtra("rv_village",postings.village)
                intent.putExtra("rv_goback",postings.goback)
                intent.putExtra("rv_pcontent",postings.content)
                intent.putExtra("rv_pdate", postings.pdate.toString())
                intent.putExtra("rv_hcnt",postings.hcnt.toString())
                intent.putExtra("rv_ccnt",postings.ccnt.toString())
                context.startActivity(intent)

            }

        } //만든 view로 customviewholder을 호출하여 값을
    }

    override fun onBindViewHolder(holder: PostingAdapter.CustomViewHolder, position: Int) {
        holder.pid.text=postingDataList!!.get(position).pid //몇번째인지 가져옴
        //holder.ptime.text=postingDataList!!.get(position).pdate.toString()
        holder.village.text= postingDataList!!.get(position).village
        holder.goback.text=postingDataList!!.get(position).goback
        holder.pcontent.text=postingDataList!!.get(position).content
        holder.hcnt.text=postingDataList!!.get(position).hcnt.toString() //int형이라 바로 못받아옴
        holder.ccnt.text=postingDataList!!.get(position).ccnt.toString()

        //stringBuilder를 굳이 이렇게 쓴 이유는 string은 자르고 붙이는데에 많은 메모리가 들어서 자르고 붙일떈
        //stringBuilder를 사용한다고 한다
        var post_time : String = postingDataList?.get(position)?.pdate.toString()
        val stringBuilder = StringBuilder()
        stringBuilder.append(post_time.substring(2,4))
        stringBuilder.append("/")
        stringBuilder.append(post_time.substring(5,7))
        stringBuilder.append("/")
        stringBuilder.append(post_time.substring(8,10))
        stringBuilder.append(" ")
        stringBuilder.append(post_time.substring(11,16))

        var ptimecheck : String =stringBuilder.toString()

        Log.d(TAG, "초기" + ptimecheck)
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")) //시간을 가져와서
        val sdf = SimpleDateFormat("yy/MM/dd HH:mm") //이형태로 바꿈
        val nowTime = sdf.format(System.currentTimeMillis())

        var sameDay = true
        for (i in 0..8) { //0~8만을 비교하는 이유는 yy-mm-dd 까지 비교문을 돌려서 비교
            if (ptimecheck.get(i) != nowTime[i]) {
                sameDay = false
            }
        } //03:01 02:01    03-02= 1    61-08= 53     1시간전 까지만 해준다.

        if (sameDay) {
            //여전히 true 라면 같은 날
            // 같은 1시간차이안에 있는지 체크        --> 몇분전인지 체크해서 출력
            val hourDifference: Int = (nowTime.substring(9,11)).toInt() - (ptimecheck.substring(9,11)).toInt()
            Log.d(TAG, "hourDifference 결과"+nowTime.substring(9,11)+"-"+ptimecheck.substring(9,11)+"="+hourDifference.toString())
            if (hourDifference == 0) {
                //같은 시간대라면
                val minuteDifference: Int =
                    nowTime.substring(12).toInt() - ptimecheck.substring(12).toInt()
                if (minuteDifference == 0) {
                    holder.ptime.text= "방금 전"
                    postingTime="방금 전"
                    Log.d(TAG, "방금 전에 들어옴")
                    Log.d(TAG,postingDataList!!.get(position).content)
                } else {
                    holder.ptime.text=minuteDifference.toString() + "분 전"
                    Log.d(TAG,postingDataList!!.get(position).content)

                }
            } else if (hourDifference == 1) {

                //시간대에는 차이가있지만 1시간보다 더 경과한 시간차가 아닌경우
                val nowMinute = nowTime.substring(12,14).toInt() + 60
                val minuteDifference: Int = nowMinute - ptimecheck.substring(12,14).toInt()
                if (minuteDifference <= 60) {  //한시간 이내인데
                    if (minuteDifference == 60) {
                        holder.ptime.text="1시간 전"
                        Log.d(TAG,postingDataList!!.get(position).content)

                    } else {
                        holder.ptime.text=minuteDifference.toString() + "분 전"
                        Log.d(TAG,postingDataList!!.get(position).content)

                    }
                }
            }
            else
            {
                holder.ptime.text=hourDifference.toString()+"시간 전"
                Log.d(TAG,postingDataList!!.get(position).content)
                Log.d(TAG,"0도 1 도 아님")

            }
        } else {
            holder.ptime.text=ptimecheck.substring(3)
            Log.d(TAG,"같은 날 아님")
            Log.d(TAG,postingDataList!!.get(position).content)

        }


    }

    override fun getItemCount(): Int {
        //리스트들을 쭉 출력할건데 그에 대한 개수를 알려준다
        return postingDataList!!.size //리스트의 총 길이
    }

    class CustomViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) { //뷰에 대한 것을 잡아준다. 차에서 음료수 마실 때 컵홀더가 있듯이..
    // 쓰려면 recyclerviewholder을 상속받아야 쓸 수 있다. 여기에 만들어둔 posting data를 활용한다
        val pid= itemView.findViewById<TextView>(R.id.pid) //특정 xml에서 찾아옴
        val ptime= itemView.findViewById<TextView>(R.id.ptime)
        val village= itemView.findViewById<TextView>(R.id.village)
        val goback= itemView.findViewById<TextView>(R.id.goback)
        val pcontent= itemView.findViewById<TextView>(R.id.pcontent)
        val hcnt= itemView.findViewById<TextView>(R.id.hcnt)
        val ccnt= itemView.findViewById<TextView>(R.id.ccnt)

    }

}