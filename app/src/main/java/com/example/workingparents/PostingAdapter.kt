package com.example.workingparents

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

private lateinit var context: Context

class PostingAdapter(val postingList: ArrayList<Posting>): RecyclerView.Adapter<PostingAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    //activty의 oncreate랑 비슷함 만들어놓은 posting.xml을 보여줄거라고 해야함
    ): PostingAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posting,parent,false) //xml을 보여줄거라고 inflater을 사용함.
        //context란 acitivty에 있는 모든 정보를 말하는 것.여기의 parent.context는 adapter와 연결될 activity의 context
        //이렇게 선언하면 view는 posting을 끌고와서 adapter에 붙여주는 역할을 한다
        return CustomViewHolder(view).apply { //클릭 인식
            itemView.setOnClickListener{
                val curPos : Int =adapterPosition //현재 클릭한 리사이클러뷰 포지션
                var postings : Posting = postingList.get(curPos) //현재 position의 recylerview 값들
                context=parent.context
                val intent = Intent(context, PostingActivity::class.java)
                intent.putExtra("rv_pid",postings.pid) //intent로 값 넘겨주기
                intent.putExtra("rv_village",postings.village)
                intent.putExtra("rv_goback",postings.goback)
                intent.putExtra("rv_pcontent",postings.content)
                intent.putExtra("rv_hcnt",postings.hcnt)
                intent.putExtra("rv_ccnt",postings.ccnt)
                context.startActivity(intent)

            }

        } //만든 view로 customviewholder을 호출하여 값을
    }

    override fun onBindViewHolder(holder: PostingAdapter.CustomViewHolder, position: Int) {
        holder.pid.text=postingList.get(position).pid //몇번째인지 가져옴
        holder.ptime.text=postingList.get(position).pdate.toString()
        holder.village.text=postingList.get(position).village
        holder.goback.text=postingList.get(position).goback
        holder.pcontent.text=postingList.get(position).content
        holder.hcnt.text=postingList.get(position).hcnt.toString() //int형이라 바로 못받아옴
        holder.ccnt.text=postingList.get(position).ccnt.toString()


    }

    override fun getItemCount(): Int {
        //리스트들을 쭉 출력할건데 그에 대한 개수를 알려준다
        return postingList.size //리스트의 총 길이
    }

    class CustomViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) { //뷰에 대한 것을 잡아준다. 차에서 음료수 마실 때 컵홀더가 있듯이..
    // 쓰려면 recyclerviewholder을 상속받아야 쓸 수 있다. 여기에 만들어둔 posting data를 활용한다.
        val pid= itemView.findViewById<TextView>(R.id.pid) //특정 xml에서 찾아옴
        val ptime= itemView.findViewById<TextView>(R.id.ptime)
        val village= itemView.findViewById<TextView>(R.id.village)
        val goback= itemView.findViewById<TextView>(R.id.goback)
        val pcontent= itemView.findViewById<TextView>(R.id.pcontent)
        val hcnt= itemView.findViewById<TextView>(R.id.hcnt)
        val ccnt= itemView.findViewById<TextView>(R.id.ccnt)

    }

}