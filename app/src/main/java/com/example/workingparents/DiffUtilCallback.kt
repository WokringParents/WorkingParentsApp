package com.example.workingparents

import android.app.Person
import android.text.method.TextKeyListener
import android.text.method.TextKeyListener.clear
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import java.util.*
import java.util.Collections.addAll
import kotlin.collections.ArrayList

private val TAG = "Diff"

class DiffUtilCallback(
    private val oldList : List<Notice>,
    private val newList : List<Notice>

) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {return oldList.size}
    override fun getNewListSize(): Int {return newList.size}


    //두 항목의 데이터가 같은지 확인한다.
    // 해당 메서드는 areItemsTheSame()에서 true 인 경우에만 호출합니다.
    // 같은 id값을 가졌더라도 내부의 값이 달려졌다면 변경된 것이므로 그것을 확인해야 한다.
    // 최종적으로 false인 item에 대해서만 onBindViewHolder 메서드가 호출됩니다.
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].nid==newList[newItemPosition].nid
    }

    //두 객체가 동일한 항목을 나타내는지 확인합니다. 만일 true라면 다음 비교를 하고,
    // false라면 리스트 갱신 시 화면이 깜빡거리는 현상이 발생할 수 있습니다.
    // 즉, areItemsTheSame을 잘못 정의한다면 다시 새로 만들게 되어서 notifyDataSerChanged()와 다를 바 없이 집니다.
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        Log.d(TAG, "areContentsTheSame 불러짐")
        return when{
            oldList[oldItemPosition].nid!=newList[newItemPosition].nid->{false}
            oldList[oldItemPosition].tid!=newList[newItemPosition].tid->{false}
            oldList[oldItemPosition].ndate!=newList[newItemPosition].ndate->{false}
            oldList[oldItemPosition].ntitle!=newList[newItemPosition].ntitle->{false}
            oldList[oldItemPosition].ncontent!=newList[newItemPosition].ncontent->{false}
            oldList[oldItemPosition].image!=newList[newItemPosition].image->{false}
            else->{true}

        }
    }
}



